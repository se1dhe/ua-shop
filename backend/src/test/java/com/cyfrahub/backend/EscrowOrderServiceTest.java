package com.cyfrahub.backend;

import com.cyfrahub.backend.application.dto.OrderDto;
import com.cyfrahub.backend.application.service.EscrowOrderService;
import com.cyfrahub.backend.domain.model.*;
import com.cyfrahub.backend.domain.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class EscrowOrderServiceTest {

    @Autowired
    private EscrowOrderService escrowOrderService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private TradeLotRepository lotRepository;

    private User buyer;
    private User seller;
    private TradeLot lot;
    private Wallet buyerWallet;
    private Wallet sellerWallet;

    @BeforeEach
    void setup() {
        buyer = userRepository.save(User.builder()
                .email("buyer@cyfrahub.com")
                .username("buyer_ua")
                .passwordHash("hash")
                .role("ROLE_USER")
                .build());

        seller = userRepository.save(User.builder()
                .email("seller@cyfrahub.com")
                .username("seller_ua")
                .passwordHash("hash")
                .role("ROLE_USER")
                .build());

        buyerWallet = walletRepository.save(Wallet.builder()
                .user(buyer)
                .balanceAvailable(new BigDecimal("1000.00"))
                .balanceFrozen(BigDecimal.ZERO)
                .build());

        sellerWallet = walletRepository.save(Wallet.builder()
                .user(seller)
                .balanceAvailable(BigDecimal.ZERO)
                .balanceFrozen(BigDecimal.ZERO)
                .build());

        Category cat = categoryRepository.save(Category.builder()
                .code("CURRENCY")
                .nameUa("Ігрова валюта")
                .nameEn("Game Currency")
                .type(CategoryType.GAME_CURRENCY)
                .build());

        Game game = gameRepository.save(Game.builder()
                .category(cat)
                .code("wow")
                .nameUa("World of Warcraft")
                .nameEn("World of Warcraft")
                .build());

        lot = lotRepository.save(TradeLot.builder()
                .seller(seller)
                .game(game)
                .titleUa("100,000 WoW Gold")
                .titleEn("100,000 WoW Gold")
                .tradeType(TradeType.MANUAL_P2P)
                .pricePerUnit(new BigDecimal("200.00"))
                .minAmount(1)
                .availableAmount(5)
                .build());
    }

    @Test
    void testFullEscrowTradeLifecycle() {
        // 1. Buyer creates and pays for order
        OrderDto.CreateOrderRequest req = new OrderDto.CreateOrderRequest(lot.getId(), 2);
        OrderDto.OrderResponse order = escrowOrderService.createAndPayOrder(buyer.getId(), req);

        assertNotNull(order);
        assertEquals("PAID_HELD", order.getStatus());
        assertEquals(new BigDecimal("400.00"), order.getTotalPrice());

        // Buyer balance must be held
        Wallet buyerW = walletRepository.findByUserId(buyer.getId()).orElseThrow();
        assertEquals(new BigDecimal("600.00"), buyerW.getBalanceAvailable());
        assertEquals(new BigDecimal("400.00"), buyerW.getBalanceFrozen());

        // 2. Seller marks transferred
        OrderDto.OrderResponse transferred = escrowOrderService.markTransferred(seller.getId(), order.getId());
        assertEquals("TRANSFERRED_BY_SELLER", transferred.getStatus());

        // 3. Buyer confirms completion
        OrderDto.OrderResponse completed = escrowOrderService.confirmCompletion(buyer.getId(), order.getId());
        assertEquals("COMPLETED", completed.getStatus());

        // Buyer frozen funds released
        buyerW = walletRepository.findByUserId(buyer.getId()).orElseThrow();
        assertEquals(new BigDecimal("600.00"), buyerW.getBalanceAvailable());
        assertEquals(BigDecimal.ZERO, buyerW.getBalanceFrozen());

        // Seller receives net earnings (400 - 8% fee = 368.00)
        Wallet sellerW = walletRepository.findByUserId(seller.getId()).orElseThrow();
        assertEquals(new BigDecimal("368.00"), sellerW.getBalanceAvailable());
    }

    @Test
    void testDisputeLifecycle() {
        OrderDto.CreateOrderRequest req = new OrderDto.CreateOrderRequest(lot.getId(), 1);
        OrderDto.OrderResponse order = escrowOrderService.createAndPayOrder(buyer.getId(), req);

        OrderDto.OrderResponse disputed = escrowOrderService.openDispute(buyer.getId(), order.getId(), "Продавець не вийшов на зв'язок");
        assertEquals("DISPUTED", disputed.getStatus());
    }
}
