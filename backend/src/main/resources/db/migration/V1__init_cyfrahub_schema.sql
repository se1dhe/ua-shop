-- CyfraHub PostgreSQL Initial Schema
-- Supports P2P Escrow, Multi-Language (UA/EN), Instant & Manual Delivery

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    username VARCHAR(100) UNIQUE NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'ROLE_USER',
    telegram_chat_id BIGINT,
    telegram_username VARCHAR(100),
    rating NUMERIC(3, 2) DEFAULT 5.00,
    reviews_count INT DEFAULT 0,
    is_online BOOLEAN DEFAULT false,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS wallets (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT UNIQUE NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    balance_available NUMERIC(12, 2) NOT NULL DEFAULT 0.00,
    balance_frozen NUMERIC(12, 2) NOT NULL DEFAULT 0.00,
    currency VARCHAR(10) NOT NULL DEFAULT 'UAH',
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS categories (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    name_ua VARCHAR(100) NOT NULL,
    name_en VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,
    icon VARCHAR(100),
    sort_order INT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS games (
    id BIGSERIAL PRIMARY KEY,
    category_id BIGINT NOT NULL REFERENCES categories(id) ON DELETE CASCADE,
    code VARCHAR(100) UNIQUE NOT NULL,
    name_ua VARCHAR(150) NOT NULL,
    name_en VARCHAR(150) NOT NULL,
    image_url VARCHAR(500),
    is_active BOOLEAN DEFAULT true,
    sort_order INT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS trade_lots (
    id BIGSERIAL PRIMARY KEY,
    seller_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    game_id BIGINT NOT NULL REFERENCES games(id) ON DELETE CASCADE,
    title_ua VARCHAR(255) NOT NULL,
    title_en VARCHAR(255) NOT NULL,
    description_ua TEXT,
    description_en TEXT,
    trade_type VARCHAR(50) NOT NULL DEFAULT 'MANUAL_P2P', -- MANUAL_P2P, INSTANT_AUTO
    price_per_unit NUMERIC(12, 2) NOT NULL,
    min_amount INT NOT NULL DEFAULT 1,
    available_amount INT NOT NULL DEFAULT 1,
    server_name VARCHAR(100),
    side_name VARCHAR(100),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS orders (
    id BIGSERIAL PRIMARY KEY,
    order_number VARCHAR(64) UNIQUE NOT NULL,
    buyer_id BIGINT NOT NULL REFERENCES users(id),
    seller_id BIGINT NOT NULL REFERENCES users(id),
    lot_id BIGINT NOT NULL REFERENCES trade_lots(id),
    amount INT NOT NULL DEFAULT 1,
    total_price NUMERIC(12, 2) NOT NULL,
    platform_fee NUMERIC(12, 2) NOT NULL,
    seller_net_earnings NUMERIC(12, 2) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'CREATED', 
    -- Statuses: CREATED, PAID_HELD, TRANSFERRED_BY_SELLER, COMPLETED, DISPUTED, CANCELLED, REFUNDED
    secret_payload TEXT, -- Digital key or link for INSTANT_AUTO lots
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS chat_messages (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    sender_id BIGINT NOT NULL REFERENCES users(id),
    message TEXT NOT NULL,
    is_system BOOLEAN DEFAULT false,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS reviews (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT UNIQUE NOT NULL REFERENCES orders(id),
    buyer_id BIGINT NOT NULL REFERENCES users(id),
    seller_id BIGINT NOT NULL REFERENCES users(id),
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Seed Initial Categories
INSERT INTO categories (code, name_ua, name_en, type, icon, sort_order) VALUES
('CURRENCY', 'Ігрова валюта', 'Game Currency', 'GAME_CURRENCY', 'coins', 1),
('ITEMS', 'Предмети та скіни', 'Items & Skins', 'GAME_ITEMS', 'shield', 2),
('SOFTWARE', 'Софт та ключі', 'Software & Keys', 'SOFTWARE_KEYS', 'key', 3),
('ACCOUNTS', 'Акаунти та підписки', 'Accounts & Subscriptions', 'ACCOUNTS', 'user-check', 4),
('SERVICES', 'Бустинг та послуги', 'Boosting & Services', 'SERVICES', 'zap', 5)
ON CONFLICT (code) DO NOTHING;

-- Seed Initial Games
INSERT INTO games (category_id, code, name_ua, name_en, image_url, sort_order) VALUES
(1, 'wow', 'World of Warcraft (Gold)', 'World of Warcraft (Gold)', 'https://images.unsplash.com/photo-1542751371-adc38448a05e?w=200', 1),
(1, 'roblox', 'Roblox (Robux)', 'Roblox (Robux)', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=200', 2),
(1, 'brawl-stars', 'Brawl Stars (Гемси)', 'Brawl Stars (Gems)', 'https://images.unsplash.com/photo-1511512578047-dfb367046420?w=200', 3),
(2, 'cs2', 'Counter-Strike 2 (Скіни)', 'Counter-Strike 2 (Skins)', 'https://images.unsplash.com/photo-1563089145-599997674d42?w=200', 4),
(2, 'dota2', 'Dota 2 (Предмети)', 'Dota 2 (Items)', 'https://images.unsplash.com/photo-1538481199705-c710c4e965fc?w=200', 5),
(3, 'windows-office', 'Windows 11 / MS Office', 'Windows 11 / MS Office', 'https://images.unsplash.com/photo-1517694712202-14dd9538aa97?w=200', 6),
(3, 'vpn-proxies', 'VPN та Проксі сервіси', 'VPN & Proxies', 'https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5?w=200', 7),
(4, 'discord-nitro', 'Discord Nitro & Telegram Stars', 'Discord Nitro & Telegram Stars', 'https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=200', 8)
ON CONFLICT (code) DO NOTHING;
