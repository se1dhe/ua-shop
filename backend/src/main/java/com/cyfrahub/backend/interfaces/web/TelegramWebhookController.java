package com.cyfrahub.backend.interfaces.web;

import com.cyfrahub.backend.domain.model.User;
import com.cyfrahub.backend.domain.repository.UserRepository;
import com.cyfrahub.backend.infrastructure.telegram.TelegramNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/telegram")
public class TelegramWebhookController {

    private static final Logger log = LoggerFactory.getLogger(TelegramWebhookController.class);

    private final UserRepository userRepository;
    private final TelegramNotificationService telegramService;

    public TelegramWebhookController(UserRepository userRepository, TelegramNotificationService telegramService) {
        this.userRepository = userRepository;
        this.telegramService = telegramService;
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleTelegramUpdate(@RequestBody Map<String, Object> update) {
        try {
            if (update.containsKey("message")) {
                @SuppressWarnings("unchecked")
                Map<String, Object> message = (Map<String, Object>) update.get("message");
                @SuppressWarnings("unchecked")
                Map<String, Object> chat = (Map<String, Object>) message.get("chat");
                @SuppressWarnings("unchecked")
                Map<String, Object> from = (Map<String, Object>) message.get("from");

                Long chatId = ((Number) chat.get("id")).longValue();
                String text = message.containsKey("text") ? (String) message.get("text") : "";
                String telegramUsername = from != null && from.containsKey("username") ? (String) from.get("username") : null;

                log.info("Incoming Telegram message from {}: {}", chatId, text);

                if (text.startsWith("/start")) {
                    String[] parts = text.split(" ");
                    if (parts.length > 1) {
                        try {
                            Long userId = Long.parseLong(parts[1].trim());
                            User user = userRepository.findById(userId).orElse(null);
                            if (user != null) {
                                user.setTelegramChatId(chatId);
                                if (telegramUsername != null) {
                                    user.setTelegramUsername(telegramUsername);
                                }
                                userRepository.save(user);
                                telegramService.sendWelcomeWithWebApp(chatId,
                                        "✅ *Ваш Telegram успішно прив'язано до акаунту " + user.getUsername() + "!*\n\nТепер ви отримуватимете всі сповіщення про угоди та повідомлення сюди.\n\n👇 Натисніть кнопку нижче, щоб відкрити магазин прямо в Telegram:");
                                return ResponseEntity.ok("OK");
                            }
                        } catch (NumberFormatException ignored) {}
                    }
                    telegramService.sendWelcomeWithWebApp(chatId,
                            "👋 *Вітаємо в CyfraHub!* 🎮\n\nУкраїнський маркетплейс цифрових товарів та ігрових цінностей (P2P Escrow захист):\n• 🪙 Ігрова валюта (WoW, Roblox, Brawl Stars)\n• 🛡️ Скіни та предмети (CS2, Dota 2)\n• 🔑 Ліцензійні ключі та софт\n• ⚡ Моментальна доставка та чат всередині угоди\n\n👇 *Натисніть кнопку нижче, щоб відкрити магазин:*");
                }
            }
        } catch (Exception e) {
            log.error("Error processing Telegram webhook: {}", e.getMessage());
        }
        return ResponseEntity.ok("OK");
    }
}
