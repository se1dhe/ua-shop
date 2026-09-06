import React, { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { 
  Search, ShieldCheck, Zap, Lock, Coins, Sparkles, Send, 
  CheckCircle, ArrowUpRight, Filter, ChevronRight
} from 'lucide-react';
import { Category, Game, TradeLot, Order, Wallet, User } from './types';
import { apiClient } from './api/client';
import { Navbar } from './components/Navbar';
import { LotTable } from './components/LotTable';
import { EscrowModal } from './components/EscrowModal';
import { WalletModal } from './components/WalletModal';
import { AuthModal } from './components/AuthModal';

export const App: React.FC = () => {
  const { t, i18n } = useTranslation();
  const isUa = i18n.language === 'uk';

  const [categories, setCategories] = useState<Category[]>([]);
  const [selectedCategory, setSelectedCategory] = useState<Category | null>(null);
  const [selectedGame, setSelectedGame] = useState<Game | null>(null);
  const [lots, setLots] = useState<TradeLot[]>([]);
  const [searchQuery, setSearchQuery] = useState('');
  
  // User & Modals
  const [user, setUser] = useState<User | null>(apiClient.getUser());
  const [wallet, setWallet] = useState<Wallet | null>(null);
  const [selectedLot, setSelectedLot] = useState<TradeLot | null>(null);
  const [activeOrder, setActiveOrder] = useState<Order | null>(null);
  const [isWalletOpen, setIsWalletOpen] = useState(false);
  const [isAuthOpen, setIsAuthOpen] = useState(false);

  // Initial load
  useEffect(() => {
    const init = async () => {
      const cats = await apiClient.getCategories();
      setCategories(cats);
      if (cats.length > 0) {
        setSelectedCategory(cats[0]);
        if (cats[0].games.length > 0) {
          setSelectedGame(cats[0].games[0]);
          const initialLots = await apiClient.getLotsByGame(cats[0].games[0].id);
          setLots(initialLots);
        }
      }

      // Wallet
      try {
        const w = await apiClient.getWallet();
        setWallet(w);
      } catch {
        setWallet({
          id: 1,
          userId: user?.userId || 1,
          balanceAvailable: 1000.00,
          balanceFrozen: 0.00,
          currency: 'UAH'
        });
      }
    };
    init();
  }, []);

  // When selected game changes
  const handleSelectGame = async (game: Game) => {
    setSelectedGame(game);
    const gameLots = await apiClient.getLotsByGame(game.id);
    setLots(gameLots);
  };

  const handleLogout = () => {
    apiClient.clearToken();
    setUser(null);
  };

  const filteredLots = lots.filter(lot => {
    if (!searchQuery) return true;
    const q = searchQuery.toLowerCase();
    return lot.titleUa.toLowerCase().includes(q) ||
           lot.titleEn.toLowerCase().includes(q) ||
           lot.sellerUsername.toLowerCase().includes(q) ||
           (lot.serverName && lot.serverName.toLowerCase().includes(q));
  });

  return (
    <div className="min-h-screen bg-[#0B0F19] text-slate-100 flex flex-col">
      
      {/* Top Navbar */}
      <Navbar
        wallet={wallet}
        user={user}
        onOpenWallet={() => setIsWalletOpen(true)}
        onOpenAuth={() => setIsAuthOpen(true)}
        onLogout={handleLogout}
      />

      {/* Hero Section */}
      <section className="relative overflow-hidden pt-12 pb-16 px-4 sm:px-6 lg:px-8 border-b border-slate-800/80 bg-gradient-to-b from-blue-950/20 via-transparent to-transparent">
        <div className="max-w-5xl mx-auto text-center space-y-6">
          
          <div className="inline-flex items-center space-x-2 px-3.5 py-1.5 rounded-full bg-emerald-500/10 border border-emerald-500/30 text-emerald-400 text-xs font-bold uppercase tracking-wider animate-pulse">
            <ShieldCheck className="w-4 h-4" />
            <span>100% Захист кожної P2P Escrow угоди</span>
          </div>

          <h1 className="text-4xl sm:text-6xl font-black tracking-tight leading-tight">
            {isUa ? (
              <>
                Український маркетплейс <br />
                <span className="bg-gradient-to-r from-blue-400 via-emerald-400 to-teal-300 bg-clip-text text-transparent">
                  ігрових цінностей та софту
                </span>
              </>
            ) : (
              <>
                Ukrainian Marketplace for <br />
                <span className="bg-gradient-to-r from-blue-400 via-emerald-400 to-teal-300 bg-clip-text text-transparent">
                  gaming assets & digital goods
                </span>
              </>
            )}
          </h1>

          <p className="text-base sm:text-lg text-slate-400 max-w-2xl mx-auto">
            {isUa
              ? 'Купуйте золото WoW, робукси, скіни CS2, ліцензійні ключі Windows та підписки Discord у перевірених продавців з миттєвою автовидачею або безпечним Escrow.'
              : 'Buy WoW Gold, Robux, CS2 skins, Windows retail keys, and Discord Nitro safely with Escrow protection and instant delivery.'}
          </p>

          {/* Search Bar */}
          <div className="max-w-2xl mx-auto relative pt-4">
            <div className="relative flex items-center">
              <Search className="w-5 h-5 text-slate-400 absolute left-4 pointer-events-none" />
              <input
                type="text"
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                placeholder={t('search.placeholder')}
                className="w-full pl-12 pr-28 py-4 rounded-2xl bg-slate-900/90 border border-slate-700 text-slate-100 placeholder-slate-500 shadow-2xl focus:outline-none focus:border-blue-500 transition text-sm"
              />
              <button className="absolute right-2 px-4 py-2.5 rounded-xl bg-blue-600 hover:bg-blue-500 text-white font-semibold text-xs transition shadow-lg shadow-blue-600/30">
                Знайти
              </button>
            </div>
          </div>

          {/* Key Advantages Grid */}
          <div className="grid grid-cols-2 sm:grid-cols-4 gap-4 max-w-4xl mx-auto pt-6 text-left">
            <div className="p-3.5 rounded-2xl bg-slate-900/60 border border-slate-800 flex items-center space-x-3">
              <div className="p-2 rounded-xl bg-blue-500/10 text-blue-400">
                <Lock className="w-4 h-4" />
              </div>
              <div>
                <span className="text-xs font-bold block text-slate-200">Escrow Холд</span>
                <span className="text-[11px] text-slate-400 block">Гроші у безпеці</span>
              </div>
            </div>

            <div className="p-3.5 rounded-2xl bg-slate-900/60 border border-slate-800 flex items-center space-x-3">
              <div className="p-2 rounded-xl bg-emerald-500/10 text-emerald-400">
                <Zap className="w-4 h-4" />
              </div>
              <div>
                <span className="text-xs font-bold block text-slate-200">Автовидача</span>
                <span className="text-[11px] text-slate-400 block">Ключі за 1 секунду</span>
              </div>
            </div>

            <div className="p-3.5 rounded-2xl bg-slate-900/60 border border-slate-800 flex items-center space-x-3">
              <div className="p-2 rounded-xl bg-sky-500/10 text-sky-400">
                <Send className="w-4 h-4" />
              </div>
              <div>
                <span className="text-xs font-bold block text-slate-200">Telegram Бот</span>
                <span className="text-[11px] text-slate-400 block">Угоди прямо в чаті</span>
              </div>
            </div>

            <div className="p-3.5 rounded-2xl bg-slate-900/60 border border-slate-800 flex items-center space-x-3">
              <div className="p-2 rounded-xl bg-amber-500/10 text-amber-400">
                <Coins className="w-4 h-4" />
              </div>
              <div>
                <span className="text-xs font-bold block text-slate-200">Monobank / IBAN</span>
                <span className="text-[11px] text-slate-400 block">Швидкий вивід грн</span>
              </div>
            </div>
          </div>

        </div>
      </section>

      {/* Main Catalog & Trading Hub */}
      <main id="catalog" className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10 flex-1 w-full space-y-8">
        
        {/* Categories Tab Bar */}
        <div className="flex items-center space-x-2 overflow-x-auto pb-2 scrollbar-none">
          {categories.map((cat) => {
            const isSelected = selectedCategory?.id === cat.id;
            return (
              <button
                key={cat.id}
                onClick={() => {
                  setSelectedCategory(cat);
                  if (cat.games.length > 0) {
                    handleSelectGame(cat.games[0]);
                  }
                }}
                className={`px-4 py-2.5 rounded-xl font-bold text-xs uppercase tracking-wider whitespace-nowrap transition flex items-center space-x-2 ${
                  isSelected
                    ? 'bg-blue-600 text-white shadow-lg shadow-blue-600/30'
                    : 'bg-slate-900 text-slate-400 hover:bg-slate-800 hover:text-slate-200 border border-slate-800'
                }`}
              >
                <span>{isUa ? cat.nameUa : cat.nameEn}</span>
              </button>
            );
          })}
        </div>

        {/* Games Pill Selector */}
        {selectedCategory && (
          <div className="flex items-center space-x-3 overflow-x-auto pb-2 scrollbar-none">
            {selectedCategory.games.map((game) => {
              const isSelected = selectedGame?.id === game.id;
              return (
                <button
                  key={game.id}
                  onClick={() => handleSelectGame(game)}
                  className={`flex items-center space-x-3 p-2 pr-4 rounded-2xl border transition group whitespace-nowrap ${
                    isSelected
                      ? 'bg-gradient-to-r from-blue-900/60 to-slate-900 border-blue-500/50 shadow-lg shadow-blue-500/10'
                      : 'bg-slate-900/50 border-slate-800 hover:border-slate-700'
                  }`}
                >
                  <img
                    src={game.imageUrl || 'https://images.unsplash.com/photo-1542751371-adc38448a05e?w=100'}
                    alt={game.nameUa}
                    className="w-10 h-10 rounded-xl object-cover"
                  />
                  <div className="text-left">
                    <span className="font-bold text-xs text-slate-200 block group-hover:text-blue-400 transition">
                      {isUa ? game.nameUa : game.nameEn}
                    </span>
                    <span className="text-[10px] text-slate-400">
                      {isUa ? 'Переглянути лоти' : 'View lots'}
                    </span>
                  </div>
                </button>
              );
            })}
          </div>
        )}

        {/* Selected Game Header & Lots Section */}
        <div className="space-y-4">
          <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
            <div>
              <div className="flex items-center space-x-2">
                <h2 className="text-xl font-black text-slate-100">
                  {selectedGame ? (isUa ? selectedGame.nameUa : selectedGame.nameEn) : 'Всі пропозиції'}
                </h2>
                <span className="px-2 py-0.5 rounded-full text-xs font-semibold bg-slate-800 text-slate-300 border border-slate-700">
                  {filteredLots.length} лотів
                </span>
              </div>
              <p className="text-xs text-slate-400">
                {isUa
                  ? 'Оберіть найкращу ціну серед перевірених українських продавців'
                  : 'Choose the best offer from verified Ukrainian traders'}
              </p>
            </div>

            {/* Filter buttons */}
            <div className="flex items-center space-x-2 text-xs">
              <button className="px-3 py-1.5 rounded-lg bg-slate-900 border border-slate-800 hover:border-slate-700 text-slate-300 transition">
                Дешевші спочатку
              </button>
              <button className="px-3 py-1.5 rounded-lg bg-slate-900 border border-slate-800 hover:border-slate-700 text-slate-300 transition">
                Лише онлайн
              </button>
            </div>
          </div>

          {/* Lots Table */}
          <LotTable
            lots={filteredLots}
            onSelectLot={(lot) => {
              setSelectedLot(lot);
              setActiveOrder(null);
            }}
          />
        </div>

      </main>

      {/* Escrow protection info banner */}
      <section id="escrow-info" className="border-t border-slate-800 bg-gradient-to-r from-blue-950/40 to-slate-950 py-12 px-4">
        <div className="max-w-5xl mx-auto grid grid-cols-1 md:grid-cols-3 gap-8 text-left">
          <div className="space-y-2">
            <div className="w-10 h-10 rounded-xl bg-blue-600/20 border border-blue-500/30 flex items-center justify-center text-blue-400">
              <ShieldCheck className="w-5 h-5" />
            </div>
            <h4 className="font-bold text-sm text-slate-100">Безпечний холдинг коштів</h4>
            <p className="text-xs text-slate-400 leading-relaxed">
              Покупець оплачує замовлення, але продавець отримує виплату лише після того, як покупець підтвердить отримання товару.
            </p>
          </div>

          <div className="space-y-2">
            <div className="w-10 h-10 rounded-xl bg-emerald-600/20 border border-emerald-500/30 flex items-center justify-center text-emerald-400">
              <Send className="w-5 h-5" />
            </div>
            <h4 className="font-bold text-sm text-slate-100">Telegram бот та чат угоди</h4>
            <p className="text-xs text-slate-400 leading-relaxed">
              Всі сповіщення про замовлення, повідомлення покупців та кнопки підтвердження дублюються в Telegram у реальному часі.
            </p>
          </div>

          <div className="space-y-2">
            <div className="w-10 h-10 rounded-xl bg-amber-600/20 border border-amber-500/30 flex items-center justify-center text-amber-400">
              <Coins className="w-5 h-5" />
            </div>
            <h4 className="font-bold text-sm text-slate-100">Миттєвий вивід у гривні</h4>
            <p className="text-xs text-slate-400 leading-relaxed">
              Виплати на картки українських банків (Monobank, ПриватБанк) через безпечний банківський шлюз.
            </p>
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer className="border-t border-slate-800 bg-[#0B0F19] py-8 text-center text-xs text-slate-500">
        <div className="max-w-7xl mx-auto px-4 flex flex-col sm:flex-row justify-between items-center gap-4">
          <div className="flex items-center space-x-2">
            <span className="font-extrabold text-slate-300">CyfraHub</span>
            <span>© 2026. Всі права захищено.</span>
          </div>
          <div className="flex items-center space-x-6 text-slate-400">
            <a href="https://t.me/CyfraHubBot" target="_blank" rel="noopener noreferrer" className="hover:text-sky-400 transition">
              Telegram Бот
            </a>
            <a href="#escrow-info" className="hover:text-emerald-400 transition">
              Правила безпечної угоди
            </a>
            <span>Україна 🇺🇦</span>
          </div>
        </div>
      </footer>

      {/* Escrow Modal */}
      {(selectedLot || activeOrder) && (
        <EscrowModal
          lot={selectedLot}
          activeOrder={activeOrder}
          currentUser={user}
          onClose={() => {
            setSelectedLot(null);
            setActiveOrder(null);
          }}
          onOrderCompleted={(completedOrder) => {
            setActiveOrder(completedOrder);
            // Refresh wallet
            if (wallet) {
              setWallet({
                ...wallet,
                balanceAvailable: Number(wallet.balanceAvailable) - completedOrder.totalPrice
              });
            }
          }}
        />
      )}

      {/* Wallet Modal */}
      {isWalletOpen && (
        <WalletModal
          wallet={wallet}
          onClose={() => setIsWalletOpen(false)}
          onWalletUpdated={(updated) => setWallet(updated)}
        />
      )}

      {/* Auth Modal */}
      {isAuthOpen && (
        <AuthModal
          onClose={() => setIsAuthOpen(false)}
          onLoginSuccess={(loggedInUser) => {
            setUser(loggedInUser);
            setIsAuthOpen(false);
          }}
        />
      )}

    </div>
  );
};
