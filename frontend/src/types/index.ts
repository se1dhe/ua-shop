export interface Category {
  id: number;
  code: string;
  nameUa: string;
  nameEn: string;
  type: string;
  icon?: string;
  games: Game[];
}

export interface Game {
  id: number;
  categoryId: number;
  code: string;
  nameUa: string;
  nameEn: string;
  imageUrl?: string;
}

export interface TradeLot {
  id: number;
  sellerId: number;
  sellerUsername: string;
  sellerRating: number;
  sellerReviewsCount: number;
  sellerIsOnline: boolean;
  gameId: number;
  gameNameUa: string;
  gameNameEn: string;
  titleUa: string;
  titleEn: string;
  descriptionUa?: string;
  descriptionEn?: string;
  tradeType: 'MANUAL_P2P' | 'INSTANT_AUTO';
  pricePerUnit: number;
  minAmount: number;
  availableAmount: number;
  serverName?: string;
  sideName?: string;
  createdAt: string;
}

export interface Order {
  id: number;
  orderNumber: string;
  buyerId: number;
  buyerUsername: string;
  sellerId: number;
  sellerUsername: string;
  lotId: number;
  lotTitleUa: string;
  lotTitleEn: string;
  gameNameUa: string;
  gameNameEn: string;
  tradeType: 'MANUAL_P2P' | 'INSTANT_AUTO';
  amount: number;
  totalPrice: number;
  platformFee: number;
  sellerNetEarnings: number;
  status: 'CREATED' | 'PAID_HELD' | 'TRANSFERRED_BY_SELLER' | 'COMPLETED' | 'DISPUTED' | 'REFUNDED';
  secretPayload?: string;
  createdAt: string;
  updatedAt: string;
}

export interface ChatMessage {
  id: number;
  orderId: number;
  senderId: number;
  senderUsername: string;
  message: string;
  isSystem: boolean;
  createdAt: string;
}

export interface Wallet {
  id: number;
  userId: number;
  balanceAvailable: number;
  balanceFrozen: number;
  currency: string;
}

export interface User {
  userId: number;
  username: string;
  email: string;
  role: string;
  telegramUsername?: string;
  token?: string;
}
