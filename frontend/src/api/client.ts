import { Category, TradeLot, Order, ChatMessage, Wallet, User } from '../types';

const API_BASE = '/api';

export const apiClient = {
  getToken(): string | null {
    return localStorage.getItem('cyfrahub_token');
  },

  setToken(token: string) {
    localStorage.setItem('cyfrahub_token', token);
  },

  clearToken() {
    localStorage.removeItem('cyfrahub_token');
    localStorage.removeItem('cyfrahub_user');
  },

  getUser(): User | null {
    const raw = localStorage.getItem('cyfrahub_user');
    return raw ? JSON.parse(raw) : null;
  },

  setUser(user: User) {
    localStorage.setItem('cyfrahub_user', JSON.stringify(user));
  },

  async request<T>(endpoint: string, options: RequestInit = {}): Promise<T> {
    const token = this.getToken();
    const headers: Record<string, string> = {
      'Content-Type': 'application/json',
      ...(options.headers as Record<string, string> || {})
    };

    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }

    const res = await fetch(`${API_BASE}${endpoint}`, {
      ...options,
      headers
    });

    if (!res.ok) {
      const errorText = await res.text();
      throw new Error(errorText || `API error: ${res.status}`);
    }

    return res.json();
  },

  // Auth
  async login(email: string, password: string): Promise<User> {
    const data = await this.request<User>('/auth/login', {
      method: 'POST',
      body: JSON.stringify({ email, password })
    });
    if (data.token) {
      this.setToken(data.token);
      this.setUser(data);
    }
    return data;
  },

  async register(email: string, password: string, username: string, telegramUsername?: string): Promise<User> {
    const data = await this.request<User>('/auth/register', {
      method: 'POST',
      body: JSON.stringify({ email, password, username, telegramUsername })
    });
    if (data.token) {
      this.setToken(data.token);
      this.setUser(data);
    }
    return data;
  },

  // Catalog
  async getCategories(): Promise<Category[]> {
    try {
      return await this.request<Category[]>('/catalog/categories');
    } catch {
      return getMockCategories();
    }
  },

  async getLotsByGame(gameId: number): Promise<TradeLot[]> {
    try {
      return await this.request<TradeLot[]>(`/lots/game/${gameId}`);
    } catch {
      return getMockLots(gameId);
    }
  },

  // Orders & Escrow
  async createOrder(lotId: number, amount: number): Promise<Order> {
    return await this.request<Order>('/orders', {
      method: 'POST',
      body: JSON.stringify({ lotId, amount })
    });
  },

  async getBuyerOrders(): Promise<Order[]> {
    return await this.request<Order[]>('/orders/buyer');
  },

  async getOrder(orderId: number): Promise<Order> {
    return await this.request<Order>(`/orders/${orderId}`);
  },

  async markTransferred(orderId: number): Promise<Order> {
    return await this.request<Order>(`/orders/${orderId}/transfer`, { method: 'POST' });
  },

  async confirmOrder(orderId: number): Promise<Order> {
    return await this.request<Order>(`/orders/${orderId}/confirm`, { method: 'POST' });
  },

  async openDispute(orderId: number, reason: string): Promise<Order> {
    return await this.request<Order>(`/orders/${orderId}/dispute`, {
      method: 'POST',
      body: JSON.stringify({ reason })
    });
  },

  // Chat
  async getMessages(orderId: number): Promise<ChatMessage[]> {
    return await this.request<ChatMessage[]>(`/orders/${orderId}/chat`);
  },

  async sendMessage(orderId: number, message: string): Promise<ChatMessage> {
    return await this.request<ChatMessage>(`/orders/${orderId}/chat`, {
      method: 'POST',
      body: JSON.stringify({ message })
    });
  },

  // Wallet
  async getWallet(): Promise<Wallet> {
    return await this.request<Wallet>('/wallet');
  },

  async deposit(amount: number): Promise<Wallet> {
    return await this.request<Wallet>('/wallet/deposit', {
      method: 'POST',
      body: JSON.stringify({ amount })
    });
  },

  async withdraw(amount: number, cardNumber: string, cardHolderName: string): Promise<Wallet> {
    return await this.request<Wallet>('/wallet/withdraw', {
      method: 'POST',
      body: JSON.stringify({ amount, cardNumber, cardHolderName })
    });
  }
};

// Mock Fallback Data (When Backend is offline or for instant preview)
function getMockCategories(): Category[] {
  return [
    {
      id: 1,
      code: 'CURRENCY',
      nameUa: 'Ігрова валюта',
      nameEn: 'Game Currency',
      type: 'GAME_CURRENCY',
      icon: 'Coins',
      games: [
        { id: 1, categoryId: 1, code: 'wow', nameUa: 'World of Warcraft (Gold)', nameEn: 'World of Warcraft (Gold)', imageUrl: 'https://images.unsplash.com/photo-1542751371-adc38448a05e?w=300' },
        { id: 2, categoryId: 1, code: 'roblox', nameUa: 'Roblox (Robux)', nameEn: 'Roblox (Robux)', imageUrl: 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=300' },
        { id: 3, categoryId: 1, code: 'brawl-stars', nameUa: 'Brawl Stars (Гемси)', nameEn: 'Brawl Stars (Gems)', imageUrl: 'https://images.unsplash.com/photo-1511512578047-dfb367046420?w=300' },
      ]
    },
    {
      id: 2,
      code: 'ITEMS',
      nameUa: 'Скіни та предмети',
      nameEn: 'Skins & Items',
      type: 'GAME_ITEMS',
      icon: 'Shield',
      games: [
        { id: 4, categoryId: 2, code: 'cs2', nameUa: 'Counter-Strike 2', nameEn: 'Counter-Strike 2', imageUrl: 'https://images.unsplash.com/photo-1563089145-599997674d42?w=300' },
        { id: 5, categoryId: 2, code: 'dota2', nameUa: 'Dota 2', nameEn: 'Dota 2', imageUrl: 'https://images.unsplash.com/photo-1538481199705-c710c4e965fc?w=300' },
      ]
    },
    {
      id: 3,
      code: 'SOFTWARE',
      nameUa: 'Софт та ключі',
      nameEn: 'Software & Keys',
      type: 'SOFTWARE_KEYS',
      icon: 'Key',
      games: [
        { id: 6, categoryId: 3, code: 'windows-office', nameUa: 'Windows 11 / MS Office', nameEn: 'Windows 11 / MS Office', imageUrl: 'https://images.unsplash.com/photo-1517694712202-14dd9538aa97?w=300' },
        { id: 7, categoryId: 3, code: 'vpn-proxies', nameUa: 'VPN та Проксі сервіси', nameEn: 'VPN & Proxies', imageUrl: 'https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5?w=300' }
      ]
    },
    {
      id: 4,
      code: 'ACCOUNTS',
      nameUa: 'Підписки та сервіси',
      nameEn: 'Subscriptions & Services',
      type: 'ACCOUNTS',
      icon: 'UserCheck',
      games: [
        { id: 8, categoryId: 4, code: 'discord-nitro', nameUa: 'Discord Nitro & TG Stars', nameEn: 'Discord Nitro & TG Stars', imageUrl: 'https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=300' }
      ]
    }
  ];
}

function getMockLots(gameId: number): TradeLot[] {
  return [
    {
      id: 101,
      sellerId: 1,
      sellerUsername: 'Oleg_CyberGold',
      sellerRating: 4.98,
      sellerReviewsCount: 342,
      sellerIsOnline: true,
      gameId: gameId,
      gameNameUa: 'World of Warcraft',
      gameNameEn: 'World of Warcraft',
      titleUa: '100,000 WoW Gold [Сервер Гордунни / Альянс]',
      titleEn: '100,000 WoW Gold [Gordunni / Alliance]',
      descriptionUa: 'Швидка передача через обмін або гільдійський банк за 5-10 хвилин.',
      descriptionEn: 'Instant trade or guild bank within 5-10 mins.',
      tradeType: 'MANUAL_P2P',
      pricePerUnit: 185.00,
      minAmount: 1,
      availableAmount: 15,
      serverName: 'Gordunni',
      sideName: 'Alliance',
      createdAt: new Date().toISOString()
    },
    {
      id: 102,
      sellerId: 2,
      sellerUsername: 'SoftKey_Master',
      sellerRating: 5.0,
      sellerReviewsCount: 1120,
      sellerIsOnline: true,
      gameId: gameId,
      gameNameUa: 'Windows & Office',
      gameNameEn: 'Windows & Office',
      titleUa: 'Windows 11 Pro Ліцензійний Офіційний Ключ',
      titleEn: 'Windows 11 Pro Official Retail License Key',
      descriptionUa: 'Миттєва автовидача в чат та на пошту одразу після оплати.',
      descriptionEn: 'Instant auto-delivery right after payment.',
      tradeType: 'INSTANT_AUTO',
      pricePerUnit: 290.00,
      minAmount: 1,
      availableAmount: 48,
      serverName: 'Global',
      sideName: 'Retail',
      createdAt: new Date().toISOString()
    },
    {
      id: 103,
      sellerId: 3,
      sellerUsername: 'Taras_Trader',
      sellerRating: 4.92,
      sellerReviewsCount: 88,
      sellerIsOnline: false,
      gameId: gameId,
      gameNameUa: 'Roblox',
      gameNameEn: 'Roblox',
      titleUa: '1,000 Robux (Передача через Gamepass)',
      titleEn: '1,000 Robux (Transferred via Gamepass)',
      descriptionUa: 'Комісію Roblox 30% покриваю повністю!',
      descriptionEn: 'Roblox 30% tax covered completely!',
      tradeType: 'MANUAL_P2P',
      pricePerUnit: 340.00,
      minAmount: 1,
      availableAmount: 8,
      serverName: 'Global',
      sideName: 'Gamepass',
      createdAt: new Date().toISOString()
    }
  ];
}
