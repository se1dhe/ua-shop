package com.cyfrahub.backend.infrastructure.telegram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Service
public class TelegramNotificationService {

    private static final Logger log = LoggerFactory.getLogger(TelegramNotificationService.class);
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    @Value("${app.telegram.bot-token:5951537041:AAFpdvTLMSCwESlcMcj9N8A46hhBU7bm97A}")
    private String botToken;

    @Value("${app.telegram.bot-username:CyfraHubBot}")
    private String botUsername;

    public void notifyOrderCreated(Long telegramChatId, String orderNumber, String lotTitle, String amount) {
        if (telegramChatId == null) {
            log.info("No Telegram chatId configured for seller. Order #{}", orderNumber);
            return;
        }
        String message = String.format("🎉 *Нове замовлення #%s!*\n\n📦 *Товар*: %s\n🔢 *Кількість*: %s\n\nКошти покупця заморожено сервісом безпечних угод CyfraHub. Будь ласка, передайте товар покупцеві.",
                orderNumber, lotTitle, amount);
        sendMessage(telegramChatId, message);
    }

    public void notifyOrderCompleted(Long telegramChatId, String orderNumber, String earnings) {
        if (telegramChatId == null) return;
        String message = String.format("💰 *Замовлення #%s завершено!*\n\nПокупець підтвердив отримання товару. Кошти за вирахуванням комісії платформи зараховано на ваш баланс: *+%s UAH*.",
                orderNumber, earnings);
        sendMessage(telegramChatId, message);
    }

    public void notifyNewChatMessage(Long telegramChatId, String orderNumber, String sender, String text) {
        if (telegramChatId == null) return;
        String message = String.format("💬 *Нове повідомлення по замовленню #%s*\n\n👤 *Від*: %s\n📩 *Текст*: %s",
                orderNumber, sender, text);
        sendMessage(telegramChatId, message);
    }

    private void sendMessage(Long chatId, String text) {
        try {
            if (botToken == null || botToken.isBlank()) {
                log.warn("Telegram bot token is not configured");
                return;
            }

            String url = String.format("https://api.telegram.org/bot%s/sendMessage?chat_id=%d&text=%s&parse_mode=Markdown",
                    botToken, chatId, URLEncoder.encode(text, StandardCharsets.UTF_8));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(5))
                    .GET()
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(res -> {
                        if (res.statusCode() == 200) {
                            log.info("Telegram notification delivered to chatId {}", chatId);
                        } else {
                            log.warn("Telegram API returned status {}: {}", res.statusCode(), res.body());
                        }
                    })
                    .exceptionally(ex -> {
                        log.error("Failed to send Telegram notification to {}: {}", chatId, ex.getMessage());
                        return null;
                    });
        } catch (Exception e) {
            log.error("Error initiating Telegram message sending: {}", e.getMessage());
        }
    }
}
