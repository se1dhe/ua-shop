package com.cyfrahub.backend.infrastructure.config;

import com.cyfrahub.backend.domain.model.*;
import com.cyfrahub.backend.domain.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final CategoryRepository categoryRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final TradeLotRepository lotRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
            CategoryRepository categoryRepository,
            GameRepository gameRepository,
            UserRepository userRepository,
            WalletRepository walletRepository,
            TradeLotRepository lotRepository,
            PasswordEncoder passwordEncoder) {
        this.categoryRepository = categoryRepository;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.lotRepository = lotRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (categoryRepository.count() == 0) {
            log.info("Initializing demo categories, games, and lots...");

            Category cur = categoryRepository.save(new Category(null, "CURRENCY", "Ігрова валюта", "Game Currency", CategoryType.GAME_CURRENCY, "Coins", 1));
            Category items = categoryRepository.save(new Category(null, "ITEMS", "Скіни та предмети", "Skins & Items", CategoryType.GAME_ITEMS, "Shield", 2));
            Category soft = categoryRepository.save(new Category(null, "SOFTWARE", "Софт та ключі", "Software & Keys", CategoryType.SOFTWARE_KEYS, "Key", 3));
            Category acc = categoryRepository.save(new Category(null, "ACCOUNTS", "Підписки та акаунти", "Subscriptions & Accounts", CategoryType.ACCOUNTS, "UserCheck", 4));

            Game wow = gameRepository.save(new Game(null, cur, "wow", "World of Warcraft (Gold)", "World of Warcraft (Gold)", "https://images.unsplash.com/photo-1542751371-adc38448a05e?w=300", true, 1));
            Game roblox = gameRepository.save(new Game(null, cur, "roblox", "Roblox (Robux)", "Roblox (Robux)", "https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=300", true, 2));
            Game brawl = gameRepository.save(new Game(null, cur, "brawl-stars", "Brawl Stars (Гемси)", "Brawl Stars (Gems)", "https://images.unsplash.com/photo-1511512578047-dfb367046420?w=300", true, 3));
            Game cs2 = gameRepository.save(new Game(null, items, "cs2", "Counter-Strike 2", "Counter-Strike 2", "https://images.unsplash.com/photo-1563089145-599997674d42?w=300", true, 4));
            Game win = gameRepository.save(new Game(null, soft, "windows-office", "Windows 11 / MS Office", "Windows 11 / MS Office", "https://images.unsplash.com/photo-1517694712202-14dd9538aa97?w=300", true, 5));

            // Demo verified seller
            User seller = userRepository.save(User.builder()
                    .email("seller@cyfrahub.com")
                    .username("CyberTrader_UA")
                    .passwordHash(passwordEncoder.encode("password123"))
                    .telegramUsername("@cyfra_seller")
                    .role("ROLE_SELLER")
                    .rating(new BigDecimal("4.98"))
                    .reviewsCount(340)
                    .isOnline(true)
                    .build());

            walletRepository.save(Wallet.builder()
                    .user(seller)
                    .balanceAvailable(new BigDecimal("5000.00"))
                    .balanceFrozen(BigDecimal.ZERO)
                    .currency("UAH")
                    .build());

            // Lots
            lotRepository.save(TradeLot.builder()
                    .seller(seller)
                    .game(wow)
                    .titleUa("100,000 WoW Gold [Гордунни / Альянс]")
                    .titleEn("100,000 WoW Gold [Gordunni / Alliance]")
                    .descriptionUa("Миттєва передача через гільдійський банк або трейд.")
                    .descriptionEn("Instant trade or guild bank within 5 mins.")
                    .tradeType(TradeType.MANUAL_P2P)
                    .pricePerUnit(new BigDecimal("185.00"))
                    .minAmount(1)
                    .availableAmount(20)
                    .serverName("Gordunni")
                    .sideName("Alliance")
                    .build());

            lotRepository.save(TradeLot.builder()
                    .seller(seller)
                    .game(win)
                    .titleUa("Windows 11 Pro Офіційний Ліцензійний Ключ")
                    .titleEn("Windows 11 Pro Official Retail License Key")
                    .descriptionUa("Миттєва автоматична видача ключа в чат та на пошту одразу після оплати.")
                    .descriptionEn("Instant auto-delivery right after payment.")
                    .tradeType(TradeType.INSTANT_AUTO)
                    .pricePerUnit(new BigDecimal("290.00"))
                    .minAmount(1)
                    .availableAmount(50)
                    .serverName("Global")
                    .sideName("Retail")
                    .build());

            lotRepository.save(TradeLot.builder()
                    .seller(seller)
                    .game(roblox)
                    .titleUa("1,000 Robux (Комісія 30% покрита)")
                    .titleEn("1,000 Robux (30% Tax Covered)")
                    .descriptionUa("Швидка передача через Gamepass у вашому плейсі.")
                    .descriptionEn("Fast transfer via Gamepass in your place.")
                    .tradeType(TradeType.MANUAL_P2P)
                    .pricePerUnit(new BigDecimal("340.00"))
                    .minAmount(1)
                    .availableAmount(12)
                    .serverName("Global")
                    .sideName("Gamepass")
                    .build());

            log.info("Demo data initialized successfully!");
        }
    }
}
