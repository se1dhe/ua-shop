# CyfraHub 🇺🇦 | Двомовний P2P Маркетплейс Ігрових Цінностей та Софту

> **CyfraHub** — українська біржа цифрових та ігрових товарів (модель FunPay/Gumroad) із захистом угод через P2P Escrow, миттєвою автовидачею ключів, підтримкою двох мов (UA/EN), Telegram-ботом, Java 21 (Spring Boot 3) бекендом та кросплатформним клієнтом на Tauri v2 (macOS, iOS, Android, Web).

---

## 🚀 Стек технологій

- **Бекенд**: Java 21 (LTS), Spring Boot 3.3, Spring Security 6 (JWT), Spring Data JPA, Flyway, WebSocket STOMP.
- **База даних та кеш**: PostgreSQL 16, Redis 7.
- **Telegram Бот**: Інтеграція сповіщень про замовлення, повідомлень у чаті угод та миттєвого підтвердження.
- **Клієнт**: Tauri v2 + React 18 + TypeScript + Vite + Tailwind CSS + Lucide Icons + i18next (Українська та English).
- **Інфраструктура**: Docker, Docker Compose, Nginx, Ngrok, Railway Cloud.

---

## 📦 Запуск усього стеку через Docker Compose

Усі сервіси (PostgreSQL, Redis, Java Backend, Frontend, Ngrok для Telegram Webhook) запускаються однією командою:

```bash
docker compose up --build -d
```

### Доступні адреси:
- **Web Інтерфейс**: [http://localhost:3000](http://localhost:3000)
- **Backend API**: [http://localhost:8080](http://localhost:8080)
- **Ngrok Web Interface (для вебхуків)**: [http://localhost:4040](http://localhost:4040)

---

## 🛡️ Механізм Безпечної Угоди (P2P Escrow)

1. **Замовлення та Холд**: Покупець обирає товар (ігрову валюту, скін або софт) та оплачує його. Кошти заморожуються платформою в Escrow.
2. **Чат угоди в реальному часі**: Відкривається захищений діалог між покупцем та продавцем. Повідомлення дублюються продавцю в Telegram-бот.
3. **Передача товару**: Продавець передає цінності в грі (або софт автоматично видається системою) та тисне *"Я передав товар"*.
4. **Підтвердження та виплата**: Покупець підтверджує отримання $\to$ кошти миттєво зараховуються на баланс продавця (за вирахуванням комісії 8%).
5. **Арбітраж**: Якщо виникає конфлікт — кожна зі сторін може відкрити спір, і служба підтримки перевіряє докази.

---

## 🤖 Налаштування Telegram Бота

- Токен бота: `5951537041:AAFpdvTLMSCwESlcMcj9N8A46hhBU7bm97A`
- Ім'я бота: `@CyfraHubBot`
- Встановлення вебхука для продакшену:
  ```bash
  curl -F "url=https://<ВАШ_ДОМЕН_RAILWAY>/api/telegram/webhook" https://api.telegram.org/bot5951537041:AAFpdvTLMSCwESlcMcj9N8A46hhBU7bm97A/setWebhook
  ```

---

## 🖥️ Збірка нативного додатку Tauri v2 (macOS / Desktop)

```bash
cd frontend
npm install
npm run tauri build
```

---

## 🌐 Деплой на Railway

1. На Railway проекті `beauty-booking-bot` старий сервіс повністю видалено.
2. Бази даних `Postgres` та `Redis` налаштовані та активні.
3. Підключення репозиторію:
   ```bash
   railway service source connect --repo se1dhe/ua-shop --branch main
   ```
4. Або деплой через CLI:
   ```bash
   railway up
   ```
