package com.cyfrahub.backend.application.service;

import com.cyfrahub.backend.application.dto.ChatMessageDto;
import com.cyfrahub.backend.application.dto.OrderDto;
import com.cyfrahub.backend.domain.model.*;
import com.cyfrahub.backend.domain.repository.*;
import com.cyfrahub.backend.infrastructure.telegram.TelegramNotificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class EscrowOrderService {

    private final OrderRepository orderRepository;
    private final TradeLotRepository lotRepository;
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final TelegramNotificationService telegramService;

    @Value("${app.commission.platform-fee-percent:8.0}")
    private Double platformFeePercent = 8.0;

    public EscrowOrderService(
            OrderRepository orderRepository,
            TradeLotRepository lotRepository,
            UserRepository userRepository,
            WalletRepository walletRepository,
            ChatMessageRepository chatMessageRepository,
            SimpMessagingTemplate messagingTemplate,
            TelegramNotificationService telegramService) {
        this.orderRepository = orderRepository;
        this.lotRepository = lotRepository;
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.messagingTemplate = messagingTemplate;
        this.telegramService = telegramService;
    }

    @Transactional
    public OrderDto.OrderResponse createAndPayOrder(Long buyerId, OrderDto.CreateOrderRequest request) {
        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new IllegalArgumentException("Buyer not found"));
        TradeLot lot = lotRepository.findById(request.getLotId())
                .orElseThrow(() -> new IllegalArgumentException("Trade lot not found"));

        if (!lot.getIsActive() || lot.getAvailableAmount() < request.getAmount()) {
            throw new IllegalStateException("Requested amount is not available");
        }
        if (lot.getSeller().getId().equals(buyerId)) {
            throw new IllegalArgumentException("Cannot purchase your own lot");
        }

        BigDecimal unitPrice = lot.getPricePerUnit();
        BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(request.getAmount()));
        BigDecimal feeRate = BigDecimal.valueOf(platformFeePercent).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        BigDecimal platformFee = totalPrice.multiply(feeRate).setScale(2, RoundingMode.HALF_UP);
        BigDecimal sellerNetEarnings = totalPrice.subtract(platformFee);

        Wallet buyerWallet = walletRepository.findByUserId(buyerId)
                .orElseThrow(() -> new IllegalStateException("Buyer wallet not found"));

        // Deduct from available and hold in Escrow
        buyerWallet.holdFunds(totalPrice);
        walletRepository.save(buyerWallet);

        // Deduct available stock
        lot.setAvailableAmount(lot.getAvailableAmount() - request.getAmount());
        if (lot.getAvailableAmount() == 0) {
            lot.setIsActive(false);
        }
        lotRepository.save(lot);

        String orderNumber = "CYF-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Order order = Order.builder()
                .orderNumber(orderNumber)
                .buyer(buyer)
                .seller(lot.getSeller())
                .lot(lot)
                .amount(request.getAmount())
                .totalPrice(totalPrice)
                .platformFee(platformFee)
                .sellerNetEarnings(sellerNetEarnings)
                .status(OrderStatus.PAID_HELD)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();

        if (lot.getTradeType() == TradeType.INSTANT_AUTO) {
            // Instant Auto-Delivery flow
            order.setStatus(OrderStatus.COMPLETED);
            order.setSecretPayload("LICENSE-KEY-AUTO-" + UUID.randomUUID().toString().toUpperCase());

            Wallet sellerWallet = walletRepository.findByUserId(lot.getSeller().getId())
                    .orElseThrow(() -> new IllegalStateException("Seller wallet not found"));
            buyerWallet.releaseFrozenFundsTo(sellerWallet, totalPrice, platformFee);
            walletRepository.save(buyerWallet);
            walletRepository.save(sellerWallet);
        }

        order = orderRepository.save(order);

        // Create initial System Message in Chat
        String initialMsg = (lot.getTradeType() == TradeType.INSTANT_AUTO)
                ? "Автоматична видача: Товар успішно доставлено покупцю. Угода завершена."
                : "Кошти покупця успішно заморожено сервісом CyfraHub. Продавець має передати товар у грі/чаті.";

        ChatMessage systemMessage = ChatMessage.builder()
                .order(order)
                .sender(buyer)
                .message(initialMsg)
                .isSystem(true)
                .createdAt(OffsetDateTime.now())
                .build();
        chatMessageRepository.save(systemMessage);

        // Notify Seller via Telegram
        telegramService.notifyOrderCreated(
                lot.getSeller().getTelegramChatId(),
                order.getOrderNumber(),
                lot.getTitleUa(),
                request.getAmount().toString()
        );

        return mapToResponse(order, buyerId);
    }

    @Transactional
    public OrderDto.OrderResponse markTransferred(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        if (!order.getSeller().getId().equals(userId)) {
            throw new IllegalStateException("Only seller can mark order as transferred");
        }
        if (order.getStatus() != OrderStatus.PAID_HELD) {
            throw new IllegalStateException("Order cannot be marked transferred in current status");
        }

        order.setStatus(OrderStatus.TRANSFERRED_BY_SELLER);
        order.setUpdatedAt(OffsetDateTime.now());
        order = orderRepository.save(order);

        addSystemMessage(order, "Продавець підтвердив передачу товару. Покупцю необхідно перевірити та підтвердити отримання.");
        return mapToResponse(order, userId);
    }

    @Transactional
    public OrderDto.OrderResponse confirmCompletion(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        if (!order.getBuyer().getId().equals(userId)) {
            throw new IllegalStateException("Only buyer can confirm order completion");
        }
        if (order.getStatus() != OrderStatus.PAID_HELD && order.getStatus() != OrderStatus.TRANSFERRED_BY_SELLER) {
            throw new IllegalStateException("Order cannot be completed in current status");
        }

        Wallet buyerWallet = walletRepository.findByUserId(order.getBuyer().getId())
                .orElseThrow(() -> new IllegalStateException("Buyer wallet not found"));
        Wallet sellerWallet = walletRepository.findByUserId(order.getSeller().getId())
                .orElseThrow(() -> new IllegalStateException("Seller wallet not found"));

        buyerWallet.releaseFrozenFundsTo(sellerWallet, order.getTotalPrice(), order.getPlatformFee());
        walletRepository.save(buyerWallet);
        walletRepository.save(sellerWallet);

        order.setStatus(OrderStatus.COMPLETED);
        order.setUpdatedAt(OffsetDateTime.now());
        order = orderRepository.save(order);

        addSystemMessage(order, "Покупець підтвердив отримання товару! Кошти успішно перераховані на баланс продавця.");

        telegramService.notifyOrderCompleted(
                order.getSeller().getTelegramChatId(),
                order.getOrderNumber(),
                order.getSellerNetEarnings().toString()
        );

        return mapToResponse(order, userId);
    }

    @Transactional
    public OrderDto.OrderResponse openDispute(Long userId, Long orderId, String reason) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        if (!order.getBuyer().getId().equals(userId) && !order.getSeller().getId().equals(userId)) {
            throw new IllegalStateException("Only buyer or seller can dispute an order");
        }
        if (order.getStatus() == OrderStatus.COMPLETED || order.getStatus() == OrderStatus.REFUNDED) {
            throw new IllegalStateException("Completed or refunded order cannot be disputed");
        }

        order.setStatus(OrderStatus.DISPUTED);
        order.setUpdatedAt(OffsetDateTime.now());
        order = orderRepository.save(order);

        addSystemMessage(order, "⚠️ Відкрито спір! Виплату заморожено. До діалогу підключається служба безпеки та арбітраж CyfraHub. Причина: " + reason);
        return mapToResponse(order, userId);
    }

    @Transactional(readOnly = true)
    public List<OrderDto.OrderResponse> getBuyerOrders(Long buyerId) {
        return orderRepository.findByBuyerIdOrderByCreatedAtDesc(buyerId)
                .stream().map(o -> mapToResponse(o, buyerId)).toList();
    }

    @Transactional(readOnly = true)
    public List<OrderDto.OrderResponse> getSellerOrders(Long sellerId) {
        return orderRepository.findBySellerIdOrderByCreatedAtDesc(sellerId)
                .stream().map(o -> mapToResponse(o, sellerId)).toList();
    }

    @Transactional(readOnly = true)
    public OrderDto.OrderResponse getOrderById(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        if (!order.getBuyer().getId().equals(userId) && !order.getSeller().getId().equals(userId)) {
            throw new IllegalStateException("Access denied to this order");
        }
        return mapToResponse(order, userId);
    }

    private void addSystemMessage(Order order, String text) {
        ChatMessage msg = ChatMessage.builder()
                .order(order)
                .sender(order.getBuyer())
                .message(text)
                .isSystem(true)
                .createdAt(OffsetDateTime.now())
                .build();
        chatMessageRepository.save(msg);

        // Notify via WebSocket
        messagingTemplate.convertAndSend("/topic/order." + order.getId(),
                ChatMessageDto.ChatMessageResponse.builder()
                        .id(msg.getId())
                        .orderId(order.getId())
                        .senderId(0L)
                        .senderUsername("SYSTEM")
                        .message(text)
                        .isSystem(true)
                        .createdAt(msg.getCreatedAt())
                        .build());
    }

    private OrderDto.OrderResponse mapToResponse(Order order, Long currentUserId) {
        boolean canSeePayload = order.getBuyer().getId().equals(currentUserId)
                && (order.getStatus() == OrderStatus.PAID_HELD || order.getStatus() == OrderStatus.COMPLETED);

        return OrderDto.OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .buyerId(order.getBuyer().getId())
                .buyerUsername(order.getBuyer().getUsername())
                .sellerId(order.getSeller().getId())
                .sellerUsername(order.getSeller().getUsername())
                .lotId(order.getLot().getId())
                .lotTitleUa(order.getLot().getTitleUa())
                .lotTitleEn(order.getLot().getTitleEn())
                .gameNameUa(order.getLot().getGame().getNameUa())
                .gameNameEn(order.getLot().getGame().getNameEn())
                .tradeType(order.getLot().getTradeType().name())
                .amount(order.getAmount())
                .totalPrice(order.getTotalPrice())
                .platformFee(order.getPlatformFee())
                .sellerNetEarnings(order.getSellerNetEarnings())
                .status(order.getStatus().name())
                .secretPayload(canSeePayload ? order.getSecretPayload() : null)
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }
}
