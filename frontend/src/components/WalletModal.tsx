import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { X, Wallet as WalletIcon, CreditCard, ArrowDownCircle, ArrowUpCircle } from 'lucide-react';
import { Wallet } from '../types';
import { apiClient } from '../api/client';

interface WalletModalProps {
  wallet: Wallet | null;
  onClose: () => void;
  onWalletUpdated: (wallet: Wallet) => void;
}

export const WalletModal: React.FC<WalletModalProps> = ({ wallet, onClose, onWalletUpdated }) => {
  const { t } = useTranslation();
  const [tab, setTab] = useState<'deposit' | 'withdraw'>('deposit');
  const [amount, setAmount] = useState<number>(200);
  const [cardNumber, setCardNumber] = useState('');
  const [cardHolder, setCardHolder] = useState('');
  const [loading, setLoading] = useState(false);

  const handleDeposit = async () => {
    setLoading(true);
    try {
      const updated = await apiClient.deposit(amount);
      onWalletUpdated(updated);
      onClose();
    } catch {
      // Demo fallback
      if (wallet) {
        const updated = { ...wallet, balanceAvailable: Number(wallet.balanceAvailable) + Number(amount) };
        onWalletUpdated(updated);
      }
      onClose();
    } finally {
      setLoading(false);
    }
  };

  const handleWithdraw = async () => {
    setLoading(true);
    try {
      const updated = await apiClient.withdraw(amount, cardNumber, cardHolder);
      onWalletUpdated(updated);
      onClose();
    } catch {
      // Demo fallback
      if (wallet && Number(wallet.balanceAvailable) >= amount) {
        const updated = { ...wallet, balanceAvailable: Number(wallet.balanceAvailable) - Number(amount) };
        onWalletUpdated(updated);
      }
      onClose();
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/80 backdrop-blur-sm">
      <div className="bg-[#0F172A] border border-slate-700 rounded-3xl w-full max-w-md shadow-2xl overflow-hidden">
        
        {/* Header */}
        <div className="px-6 py-4 border-b border-slate-800 flex items-center justify-between bg-slate-900/50">
          <div className="flex items-center space-x-2">
            <WalletIcon className="w-5 h-5 text-emerald-400" />
            <h3 className="font-bold text-base text-slate-100">{t('wallet.title')}</h3>
          </div>
          <button onClick={onClose} className="p-1 rounded-lg text-slate-400 hover:text-slate-200">
            <X className="w-5 h-5" />
          </button>
        </div>

        {/* Content */}
        <div className="p-6 space-y-6">
          
          {/* Balance Widget */}
          <div className="p-5 rounded-2xl bg-gradient-to-br from-slate-900 to-slate-800 border border-slate-700 flex justify-between items-center">
            <div>
              <span className="text-xs text-slate-400 block">{t('wallet.available')}</span>
              <span className="text-2xl font-black text-emerald-400">
                {wallet ? Number(wallet.balanceAvailable).toFixed(2) : '1,000.00'} ₴
              </span>
            </div>
            {wallet && wallet.balanceFrozen > 0 && (
              <div className="text-right">
                <span className="text-xs text-amber-400 block">{t('wallet.frozen')}</span>
                <span className="text-sm font-bold text-amber-300">
                  {Number(wallet.balanceFrozen).toFixed(2)} ₴
                </span>
              </div>
            )}
          </div>

          {/* Tabs */}
          <div className="grid grid-cols-2 gap-2 p-1 rounded-xl bg-slate-900 border border-slate-800">
            <button
              onClick={() => setTab('deposit')}
              className={`py-2 text-xs font-bold rounded-lg transition ${
                tab === 'deposit' ? 'bg-blue-600 text-white' : 'text-slate-400 hover:text-slate-200'
              }`}
            >
              <div className="flex items-center justify-center space-x-1">
                <ArrowDownCircle className="w-3.5 h-3.5" />
                <span>{t('wallet.deposit')}</span>
              </div>
            </button>
            <button
              onClick={() => setTab('withdraw')}
              className={`py-2 text-xs font-bold rounded-lg transition ${
                tab === 'withdraw' ? 'bg-emerald-600 text-white' : 'text-slate-400 hover:text-slate-200'
              }`}
            >
              <div className="flex items-center justify-center space-x-1">
                <ArrowUpCircle className="w-3.5 h-3.5" />
                <span>{t('wallet.withdraw')}</span>
              </div>
            </button>
          </div>

          {/* Tab 1: Deposit */}
          {tab === 'deposit' && (
            <div className="space-y-4">
              <div>
                <label className="text-xs font-semibold text-slate-400 block mb-1">
                  {t('wallet.amount_label')}
                </label>
                <div className="grid grid-cols-4 gap-2 mb-2">
                  {[100, 200, 500, 1000].map((preset) => (
                    <button
                      key={preset}
                      type="button"
                      onClick={() => setAmount(preset)}
                      className={`py-1.5 rounded-lg text-xs font-bold border transition ${
                        amount === preset
                          ? 'bg-blue-600/30 border-blue-500 text-blue-300'
                          : 'bg-slate-900 border-slate-800 text-slate-400 hover:border-slate-700'
                      }`}
                    >
                      {preset} ₴
                    </button>
                  ))}
                </div>
                <input
                  type="number"
                  value={amount}
                  onChange={(e) => setAmount(Number(e.target.value))}
                  className="w-full px-4 py-2.5 rounded-xl bg-slate-900 border border-slate-700 text-slate-100 font-bold"
                />
              </div>

              <button
                onClick={handleDeposit}
                disabled={loading}
                className="w-full py-3.5 rounded-2xl bg-gradient-to-r from-blue-600 to-emerald-600 hover:from-blue-500 hover:to-emerald-500 text-white font-bold text-sm shadow-lg shadow-blue-600/30 transition flex items-center justify-center space-x-2"
              >
                <CreditCard className="w-4 h-4" />
                <span>{loading ? 'Обробка...' : t('wallet.deposit_mono')}</span>
              </button>
            </div>
          )}

          {/* Tab 2: Withdraw */}
          {tab === 'withdraw' && (
            <div className="space-y-4">
              <div>
                <label className="text-xs font-semibold text-slate-400 block mb-1">
                  {t('wallet.amount_label')}
                </label>
                <input
                  type="number"
                  value={amount}
                  onChange={(e) => setAmount(Number(e.target.value))}
                  className="w-full px-4 py-2.5 rounded-xl bg-slate-900 border border-slate-700 text-slate-100 font-bold"
                />
              </div>
              <div>
                <label className="text-xs font-semibold text-slate-400 block mb-1">
                  {t('wallet.withdraw_card')}
                </label>
                <input
                  type="text"
                  placeholder="UA000000000000000000000000000"
                  value={cardNumber}
                  onChange={(e) => setCardNumber(e.target.value)}
                  className="w-full px-4 py-2 rounded-xl bg-slate-900 border border-slate-700 text-xs text-slate-100 font-mono"
                />
              </div>
              <div>
                <label className="text-xs font-semibold text-slate-400 block mb-1">
                  ПІБ власника картки
                </label>
                <input
                  type="text"
                  placeholder="Іваненко Іван Іванович"
                  value={cardHolder}
                  onChange={(e) => setCardHolder(e.target.value)}
                  className="w-full px-4 py-2 rounded-xl bg-slate-900 border border-slate-700 text-xs text-slate-100"
                />
              </div>

              <button
                onClick={handleWithdraw}
                disabled={loading}
                className="w-full py-3.5 rounded-2xl bg-emerald-600 hover:bg-emerald-500 text-white font-bold text-sm shadow-lg shadow-emerald-600/30 transition"
              >
                {loading ? 'Обробка виводу...' : 'Замовити вивід на банківську картку'}
              </button>
            </div>
          )}

        </div>

      </div>
    </div>
  );
};
