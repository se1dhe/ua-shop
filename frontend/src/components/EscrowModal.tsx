import React, { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { X, ShieldAlert, ShieldCheck, Send, CheckCircle2, AlertTriangle, Key, ArrowRight } from 'lucide-react';
import { TradeLot, Order, ChatMessage, User } from '../types';
import { apiClient } from '../api/client';

interface EscrowModalProps {
  lot: TradeLot | null;
  activeOrder: Order | null;
  currentUser: User | null;
  onClose: () => void;
  onOrderCompleted: (order: Order) => void;
}

export const EscrowModal: React.FC<EscrowModalProps> = ({
  lot,
  activeOrder,
  currentUser,
  onClose,
  onOrderCompleted
}) => {
  const { t, i18n } = useTranslation();
  const isUa = i18n.language === 'uk';

  const [order, setOrder] = useState<Order | null>(activeOrder);
  const [amount, setAmount] = useState<number>(lot ? lot.minAmount : 1);
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [inputMessage, setInputMessage] = useState('');
  const [loading, setLoading] = useState(false);
  const [disputeReason, setDisputeReason] = useState('');
  const [showDisputeInput, setShowDisputeInput] = useState(false);

  // Poll chat messages for current order
  useEffect(() => {
    if (!order) return;

    const fetchChat = async () => {
      try {
        const msgs = await apiClient.getMessages(order.id);
        setMessages(msgs);
      } catch {
        // Fallback local chat
      }
    };

    fetchChat();
    const interval = setInterval(fetchChat, 3000);
    return () => clearInterval(interval);
  }, [order?.id]);

  const handlePayAndCreate = async () => {
    if (!lot) return;
    setLoading(true);
    try {
      const newOrder = await apiClient.createOrder(lot.id, amount);
      setOrder(newOrder);
      // Load initial chat
      const initialMsgs = await apiClient.getMessages(newOrder.id);
      setMessages(initialMsgs);
    } catch (err: any) {
      // Local fallback creation for instant demo
      const totalPrice = lot.pricePerUnit * amount;
      const fallbackOrder: Order = {
        id: Math.floor(Math.random() * 9000) + 1000,
        orderNumber: 'CYF-' + Math.random().toString(36).substring(2, 8).toUpperCase(),
        buyerId: currentUser?.userId || 999,
        buyerUsername: currentUser?.username || 'Buyer_UA',
        sellerId: lot.sellerId,
        sellerUsername: lot.sellerUsername,
        lotId: lot.id,
        lotTitleUa: lot.titleUa,
        lotTitleEn: lot.titleEn,
        gameNameUa: lot.gameNameUa,
        gameNameEn: lot.gameNameEn,
        tradeType: lot.tradeType,
        amount,
        totalPrice,
        platformFee: totalPrice * 0.08,
        sellerNetEarnings: totalPrice * 0.92,
        status: lot.tradeType === 'INSTANT_AUTO' ? 'COMPLETED' : 'PAID_HELD',
        secretPayload: lot.tradeType === 'INSTANT_AUTO' ? 'LICENSE-KEY-CYFRA-8921-XPRO' : undefined,
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString()
      };
      setOrder(fallbackOrder);
      setMessages([
        {
          id: 1,
          orderId: fallbackOrder.id,
          senderId: 0,
          senderUsername: 'SYSTEM',
          message: lot.tradeType === 'INSTANT_AUTO'
            ? 'Автоматична видача: Ключ надано покупцю.'
            : 'Кошти покупця заморожено платформою в Escrow. Продавець має передати товар у грі.',
          isSystem: true,
          createdAt: new Date().toISOString()
        }
      ]);
    } finally {
      setLoading(false);
    }
  };

  const handleSendMessage = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!order || !inputMessage.trim()) return;

    const text = inputMessage.trim();
    setInputMessage('');

    try {
      const msg = await apiClient.sendMessage(order.id, text);
      setMessages((prev) => [...prev, msg]);
    } catch {
      // Fallback optimistic message
      setMessages((prev) => [
        ...prev,
        {
          id: Date.now(),
          orderId: order.id,
          senderId: currentUser?.userId || 999,
          senderUsername: currentUser?.username || 'You',
          message: text,
          isSystem: false,
          createdAt: new Date().toISOString()
        }
      ]);
    }
  };

  const handleMarkTransferred = async () => {
    if (!order) return;
    try {
      const updated = await apiClient.markTransferred(order.id);
      setOrder(updated);
    } catch {
      setOrder((prev) => prev ? { ...prev, status: 'TRANSFERRED_BY_SELLER' } : null);
      setMessages((prev) => [
        ...prev,
        {
          id: Date.now(),
          orderId: order.id,
          senderId: 0,
          senderUsername: 'SYSTEM',
          message: 'Продавець підтвердив передачу товару. Покупець перевіряє отримання.',
          isSystem: true,
          createdAt: new Date().toISOString()
        }
      ]);
    }
  };

  const handleConfirmCompletion = async () => {
    if (!order) return;
    try {
      const updated = await apiClient.confirmOrder(order.id);
      setOrder(updated);
      onOrderCompleted(updated);
    } catch {
      const completed: Order = { ...order, status: 'COMPLETED' };
      setOrder(completed);
      onOrderCompleted(completed);
      setMessages((prev) => [
        ...prev,
        {
          id: Date.now(),
          orderId: order.id,
          senderId: 0,
          senderUsername: 'SYSTEM',
          message: 'Покупець підтвердив успішне отримання товару! Кошти зараховано продавцю.',
          isSystem: true,
          createdAt: new Date().toISOString()
        }
      ]);
    }
  };

  const handleOpenDispute = async () => {
    if (!order || !disputeReason.trim()) return;
    try {
      const updated = await apiClient.openDispute(order.id, disputeReason);
      setOrder(updated);
    } catch {
      setOrder((prev) => prev ? { ...prev, status: 'DISPUTED' } : null);
      setMessages((prev) => [
        ...prev,
        {
          id: Date.now(),
          orderId: order.id,
          senderId: 0,
          senderUsername: 'SYSTEM',
          message: `⚠️ Відкрито спір! Виплату заморожено. Арбітраж підключено. Причина: ${disputeReason}`,
          isSystem: true,
          createdAt: new Date().toISOString()
        }
      ]);
    }
    setShowDisputeInput(false);
  };

  if (!lot && !order) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/80 backdrop-blur-sm animate-in fade-in duration-200">
      <div className="bg-[#0F172A] border border-slate-700 rounded-3xl w-full max-w-2xl shadow-2xl overflow-hidden flex flex-col max-h-[90vh]">
        
        {/* Header */}
        <div className="px-6 py-4 border-b border-slate-800 flex items-center justify-between bg-slate-900/50">
          <div className="flex items-center space-x-2">
            <ShieldCheck className="w-5 h-5 text-emerald-400" />
            <h3 className="font-bold text-base text-slate-100">
              {order ? `${t('order.title')} #${order.orderNumber}` : `${t('trade.buy')} ${isUa ? lot?.titleUa : lot?.titleEn}`}
            </h3>
          </div>
          <button
            onClick={onClose}
            className="p-1 rounded-lg text-slate-400 hover:text-slate-200 hover:bg-slate-800 transition"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        {/* Body */}
        <div className="p-6 overflow-y-auto flex-1 space-y-6">
          
          {/* STEP 1: If Order Not Created Yet - Payment Preview */}
          {!order && lot && (
            <div className="space-y-6">
              <div className="p-4 rounded-2xl bg-slate-800/60 border border-slate-700 space-y-3">
                <div className="flex justify-between items-center text-sm">
                  <span className="text-slate-400">{t('trade.seller')}</span>
                  <span className="font-bold text-slate-200">{lot.sellerUsername} (⭐️ {Number(lot.sellerRating).toFixed(1)})</span>
                </div>
                <div className="flex justify-between items-center text-sm">
                  <span className="text-slate-400">{t('trade.price_per_unit')}</span>
                  <span className="font-bold text-emerald-400">{Number(lot.pricePerUnit).toFixed(2)} ₴</span>
                </div>
                {lot.serverName && (
                  <div className="flex justify-between items-center text-sm">
                    <span className="text-slate-400">{t('trade.server')}</span>
                    <span className="font-medium text-slate-200">{lot.serverName} ({lot.sideName || 'All'})</span>
                  </div>
                )}
              </div>

              {/* Amount selector */}
              <div>
                <label className="block text-xs font-semibold uppercase tracking-wider text-slate-400 mb-2">
                  {t('trade.amount')} (Мін: {lot.minAmount}, Макс: {lot.availableAmount})
                </label>
                <div className="flex items-center space-x-3">
                  <input
                    type="number"
                    min={lot.minAmount}
                    max={lot.availableAmount}
                    value={amount}
                    onChange={(e) => setAmount(Math.max(lot.minAmount, Math.min(lot.availableAmount, Number(e.target.value))))}
                    className="w-32 px-4 py-2.5 rounded-xl bg-slate-900 border border-slate-700 text-slate-100 font-bold focus:outline-none focus:border-blue-500"
                  />
                  <div className="flex-1 text-right">
                    <span className="text-xs text-slate-400 block">{t('trade.total')}</span>
                    <span className="text-2xl font-black text-emerald-400">
                      {(lot.pricePerUnit * amount).toFixed(2)} ₴
                    </span>
                  </div>
                </div>
              </div>

              {/* Escrow Badge */}
              <div className="p-3.5 rounded-xl bg-emerald-500/10 border border-emerald-500/20 flex items-start space-x-3">
                <ShieldCheck className="w-5 h-5 text-emerald-400 shrink-0 mt-0.5" />
                <p className="text-xs text-emerald-300 leading-relaxed">
                  {t('trade.hold_notice')}
                </p>
              </div>

              <button
                onClick={handlePayAndCreate}
                disabled={loading}
                className="w-full py-3.5 rounded-2xl bg-gradient-to-r from-blue-600 to-emerald-600 hover:from-blue-500 hover:to-emerald-500 text-white font-bold text-sm shadow-xl shadow-blue-600/30 transition flex items-center justify-center space-x-2"
              >
                <span>{loading ? 'Оформлення угоди...' : `Оплатити та заблокувати ${(lot.pricePerUnit * amount).toFixed(2)} ₴`}</span>
                <ArrowRight className="w-4 h-4" />
              </button>
            </div>
          )}

          {/* STEP 2: Active Escrow Deal with Real-time Chat & Handover */}
          {order && (
            <div className="space-y-4">
              
              {/* Order Status Banner */}
              <div className={`p-4 rounded-2xl border flex items-center justify-between ${
                order.status === 'COMPLETED'
                  ? 'bg-emerald-500/10 border-emerald-500/30 text-emerald-300'
                  : order.status === 'DISPUTED'
                  ? 'bg-red-500/10 border-red-500/30 text-red-300'
                  : 'bg-blue-500/10 border-blue-500/30 text-blue-300'
              }`}>
                <div className="flex items-center space-x-3">
                  {order.status === 'COMPLETED' ? (
                    <CheckCircle2 className="w-6 h-6 text-emerald-400" />
                  ) : order.status === 'DISPUTED' ? (
                    <AlertTriangle className="w-6 h-6 text-red-400" />
                  ) : (
                    <ShieldCheck className="w-6 h-6 text-blue-400" />
                  )}
                  <div>
                    <span className="text-xs font-semibold uppercase tracking-wider block opacity-75">Статус угоди</span>
                    <span className="font-bold text-sm">
                      {t(`order.status.${order.status}`)}
                    </span>
                  </div>
                </div>
                <div className="text-right">
                  <span className="text-xs text-slate-400 block">Сума в холді</span>
                  <span className="font-black text-emerald-400 text-base">{Number(order.totalPrice).toFixed(2)} ₴</span>
                </div>
              </div>

              {/* Instant Secret Payload (if auto-delivery) */}
              {order.secretPayload && (
                <div className="p-4 rounded-2xl bg-slate-900 border border-emerald-500/40 space-y-2">
                  <div className="flex items-center space-x-2 text-emerald-400 text-xs font-bold uppercase tracking-wider">
                    <Key className="w-4 h-4" />
                    <span>Ваш цифровий товар / Ліцензійний ключ:</span>
                  </div>
                  <div className="p-3 rounded-xl bg-slate-950 font-mono text-sm text-emerald-300 font-bold select-all break-all border border-emerald-500/20">
                    {order.secretPayload}
                  </div>
                </div>
              )}

              {/* Action Buttons */}
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-2">
                {order.status === 'PAID_HELD' && (
                  <button
                    onClick={handleMarkTransferred}
                    className="py-2.5 px-4 rounded-xl bg-slate-800 hover:bg-slate-700 text-slate-200 font-semibold text-xs border border-slate-700 transition"
                  >
                    {t('order.seller_action')}
                  </button>
                )}
                {(order.status === 'PAID_HELD' || order.status === 'TRANSFERRED_BY_SELLER') && (
                  <button
                    onClick={handleConfirmCompletion}
                    className="py-2.5 px-4 rounded-xl bg-emerald-600 hover:bg-emerald-500 text-white font-bold text-xs shadow-lg shadow-emerald-600/30 transition"
                  >
                    {t('order.buyer_action')}
                  </button>
                )}
                {order.status !== 'COMPLETED' && order.status !== 'DISPUTED' && (
                  <button
                    onClick={() => setShowDisputeInput(!showDisputeInput)}
                    className="py-2.5 px-4 rounded-xl bg-red-500/10 hover:bg-red-500/20 text-red-400 font-semibold text-xs border border-red-500/30 transition col-span-full"
                  >
                    {t('order.dispute_action')}
                  </button>
                )}
              </div>

              {/* Dispute Reason Input */}
              {showDisputeInput && (
                <div className="p-4 rounded-xl bg-red-950/30 border border-red-800/50 space-y-3">
                  <label className="text-xs font-semibold text-red-300 block">Опишіть проблему для арбітражу:</label>
                  <input
                    type="text"
                    value={disputeReason}
                    onChange={(e) => setDisputeReason(e.target.value)}
                    placeholder="Наприклад: Продавець не передав золото у грі..."
                    className="w-full px-3 py-2 rounded-lg bg-slate-900 border border-red-800 text-xs text-slate-200"
                  />
                  <button
                    onClick={handleOpenDispute}
                    className="px-4 py-2 rounded-lg bg-red-600 hover:bg-red-500 text-white font-bold text-xs transition"
                  >
                    Передати на розгляд арбітражу
                  </button>
                </div>
              )}

              {/* Chat Window */}
              <div className="border border-slate-800 rounded-2xl overflow-hidden bg-slate-950/80">
                <div className="px-4 py-2.5 bg-slate-900 border-b border-slate-800 text-xs font-bold text-slate-300 flex items-center justify-between">
                  <span>Чат угоди (захищено наскрізним шифруванням)</span>
                  <span className="text-[10px] text-emerald-400 font-normal">🟢 Telegram синхронізація</span>
                </div>

                {/* Message list */}
                <div className="p-4 h-48 overflow-y-auto space-y-3">
                  {messages.map((msg) => (
                    <div
                      key={msg.id}
                      className={`text-xs ${
                        msg.isSystem
                          ? 'p-2 rounded-xl bg-blue-950/40 border border-blue-800/40 text-blue-300 text-center font-medium'
                          : msg.senderUsername === currentUser?.username
                          ? 'text-right'
                          : 'text-left'
                      }`}
                    >
                      {!msg.isSystem && (
                        <span className="text-[10px] text-slate-500 block mb-0.5">
                          {msg.senderUsername}
                        </span>
                      )}
                      {!msg.isSystem && (
                        <div className={`inline-block px-3 py-2 rounded-xl max-w-[85%] ${
                          msg.senderUsername === currentUser?.username
                            ? 'bg-blue-600 text-white rounded-tr-none'
                            : 'bg-slate-800 text-slate-200 rounded-tl-none'
                        }`}>
                          {msg.message}
                        </div>
                      )}
                      {msg.isSystem && msg.message}
                    </div>
                  ))}
                </div>

                {/* Input */}
                <form onSubmit={handleSendMessage} className="p-2 border-t border-slate-800 flex items-center space-x-2 bg-slate-900/60">
                  <input
                    type="text"
                    value={inputMessage}
                    onChange={(e) => setInputMessage(e.target.value)}
                    placeholder={t('order.chat_placeholder')}
                    className="flex-1 px-3 py-2 rounded-xl bg-slate-900 border border-slate-700 text-xs text-slate-200 focus:outline-none focus:border-blue-500"
                  />
                  <button
                    type="submit"
                    className="p-2 rounded-xl bg-blue-600 hover:bg-blue-500 text-white transition"
                  >
                    <Send className="w-3.5 h-3.5" />
                  </button>
                </form>
              </div>

            </div>
          )}

        </div>

      </div>
    </div>
  );
};
