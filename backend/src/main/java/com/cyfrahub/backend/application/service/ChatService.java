package com.cyfrahub.backend.application.service;

import com.cyfrahub.backend.application.dto.ChatMessageDto;
import com.cyfrahub.backend.domain.model.ChatMessage;
import com.cyfrahub.backend.domain.model.Order;
import com.cyfrahub.backend.domain.model.User;
import com.cyfrahub.backend.domain.repository.ChatMessageRepository;
import com.cyfrahub.backend.domain.repository.OrderRepository;
import com.cyfrahub.backend.domain.repository.UserRepository;
import com.cyfrahub.backend.infrastructure.telegram.TelegramNotificationService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final TelegramNotificationService telegramService;

    public ChatService(
            ChatMessageRepository chatMessageRepository,
            OrderRepository orderRepository,
            UserRepository userRepository,
            SimpMessagingTemplate messagingTemplate,
            TelegramNotificationService telegramService) {
        this.chatMessageRepository = chatMessageRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
        this.telegramService = telegramService;
    }

    @Transactional(readOnly = true)
    public List<ChatMessageDto.ChatMessageResponse> getOrderMessages(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        if (!order.getBuyer().getId().equals(userId) && !order.getSeller().getId().equals(userId)) {
            throw new IllegalStateException("Access denied to this order chat");
        }

        return chatMessageRepository.findByOrderIdOrderByCreatedAtAsc(orderId)
                .stream().map(this::mapToResponse).toList();
    }

    @Transactional
    public ChatMessageDto.ChatMessageResponse sendMessage(Long senderId, Long orderId, String text) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        if (!order.getBuyer().getId().equals(senderId) && !order.getSeller().getId().equals(senderId)) {
            throw new IllegalStateException("Access denied to this order chat");
        }

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));

        ChatMessage message = ChatMessage.builder()
                .order(order)
                .sender(sender)
                .message(text.trim())
                .isSystem(false)
                .createdAt(OffsetDateTime.now())
                .build();

        message = chatMessageRepository.save(message);

        ChatMessageDto.ChatMessageResponse response = mapToResponse(message);

        // Broadcast to WebSocket subscribers of this order
        messagingTemplate.convertAndSend("/topic/order." + orderId, response);

        // If message is from Buyer, notify Seller via Telegram
        User recipient = order.getBuyer().getId().equals(senderId) ? order.getSeller() : order.getBuyer();
        if (recipient.getTelegramChatId() != null) {
            telegramService.notifyNewChatMessage(
                    recipient.getTelegramChatId(),
                    order.getOrderNumber(),
                    sender.getUsername(),
                    text
            );
        }

        return response;
    }

    private ChatMessageDto.ChatMessageResponse mapToResponse(ChatMessage msg) {
        return ChatMessageDto.ChatMessageResponse.builder()
                .id(msg.getId())
                .orderId(msg.getOrder().getId())
                .senderId(msg.getIsSystem() ? 0L : msg.getSender().getId())
                .senderUsername(msg.getIsSystem() ? "SYSTEM" : msg.getSender().getUsername())
                .message(msg.getMessage())
                .isSystem(msg.getIsSystem())
                .createdAt(msg.getCreatedAt())
                .build();
    }
}
