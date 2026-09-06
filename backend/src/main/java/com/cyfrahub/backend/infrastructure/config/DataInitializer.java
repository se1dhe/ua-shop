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
        if (lotRepository.count() == 0) {
            log.info("Populating demo lots for gaming and digital marketplace...");

            User seller = userRepository.findByEmail("seller@cyfrahub.com").orElseGet(() -> {
                User s = userRepository.save(User.builder()
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
                        .user(s)
                        .balanceAvailable(new BigDecimal("5000.00"))
                        .balanceFrozen(BigDecimal.ZERO)
                        .currency("UAH")
                        .build());
                return s;
            });

            Game wow = gameRepository.findByCode("wow").orElse(null);
            Game roblox = gameRepository.findByCode("roblox").orElse(null);
            Game win = gameRepository.findByCode("windows-office").orElse(null);
            Game cs2 = gameRepository.findByCode("cs2").orElse(null);

            if (wow != null) {
                lotRepository.save(TradeLot.builder()
                        .seller(seller)
                        .game(wow)
                        .titleUa("100,000 WoW Gold [Гордунни / Альянс]")
                        .titleEn("100,000 WoW Gold [Gordunni / Alliance]")
                        .descriptionUa("Швидка передача через гільдійський банк або трейд за 5 хвилин.")
                        .descriptionEn("Fast transfer via guild bank or direct trade within 5 mins.")
                        .tradeType(TradeType.MANUAL_P2P)
                        .pricePerUnit(new BigDecimal("185.00"))
                        .minAmount(1)
                        .availableAmount(25)
                        .serverName("Gordunni")
                        .sideName("Alliance")
                        .isActive(true)
                        .build());
            }

            if (win != null) {
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
                        .isActive(true)
                        .build());
            }

            if (roblox != null) {
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
                        .availableAmount(15)
                        .serverName("Global")
                        .sideName("Gamepass")
                        .isActive(true)
                        .build());
            }

            if (cs2 != null) {
                lotRepository.save(TradeLot.builder()
                        .seller(seller)
                        .game(cs2)
                        .titleUa("AK-47 | Redline (Field-Tested) з 4 наліпками")
                        .titleEn("AK-47 | Redline (Field-Tested) with 4 stickers")
                        .descriptionUa("Миттєвий трейд у Steam одразу після холдування оплати.")
                        .descriptionEn("Instant Steam trade offer right after escrow payment.")
                        .tradeType(TradeType.MANUAL_P2P)
                        .pricePerUnit(new BigDecimal("820.00"))
                        .minAmount(1)
                        .availableAmount(3)
                        .serverName("Steam Trade")
                        .sideName("Covert")
                        .isActive(true)
                        .build());
            }

            log.info("Initialized {} demo lots successfully!", lotRepository.count());
        }
    }
}
