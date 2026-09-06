import React from 'react';
import { useTranslation } from 'react-i18next';
import { ShieldCheck, Wallet as WalletIcon, Globe, Send, UserCheck, PlusCircle } from 'lucide-react';
import { Wallet, User } from '../types';

interface NavbarProps {
  wallet: Wallet | null;
  user: User | null;
  onOpenWallet: () => void;
  onOpenAuth: () => void;
  onLogout: () => void;
}

export const Navbar: React.FC<NavbarProps> = ({ wallet, user, onOpenWallet, onOpenAuth, onLogout }) => {
  const { t, i18n } = useTranslation();

  const toggleLanguage = () => {
    const nextLang = i18n.language === 'uk' ? 'en' : 'uk';
    i18n.changeLanguage(nextLang);
    localStorage.setItem('cyfrahub_lang', nextLang);
  };

  return (
    <header className="sticky top-0 z-40 bg-[#0F172A]/90 backdrop-blur-md border-b border-slate-800">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
        
        {/* Brand */}
        <div className="flex items-center space-x-3 cursor-pointer">
          <div className="w-10 h-10 rounded-xl bg-gradient-to-tr from-blue-600 to-emerald-400 flex items-center justify-center shadow-lg shadow-blue-500/20">
            <ShieldCheck className="w-6 h-6 text-white" />
          </div>
          <div>
            <div className="flex items-center space-x-2">
              <span className="text-xl font-extrabold tracking-tight bg-gradient-to-r from-blue-400 via-emerald-400 to-teal-300 bg-clip-text text-transparent">
                CyfraHub
              </span>
              <span className="text-[10px] font-bold uppercase tracking-wider px-1.5 py-0.5 rounded bg-blue-500/20 text-blue-400 border border-blue-500/30">
                P2P ESCROW
              </span>
            </div>
            <p className="text-xs text-slate-400 hidden sm:block">
              {t('brand.tagline')}
            </p>
          </div>
        </div>

        {/* Center Navigation */}
        <nav className="hidden md:flex items-center space-x-6 text-sm font-medium text-slate-300">
          <a href="#catalog" className="hover:text-blue-400 transition-colors">{t('nav.catalog')}</a>
          <a href="#software" className="hover:text-blue-400 transition-colors">{t('nav.software')}</a>
          <a href="#escrow-info" className="flex items-center space-x-1 text-emerald-400 hover:text-emerald-300 transition-colors">
            <ShieldCheck className="w-4 h-4" />
            <span>{t('nav.escrow')}</span>
          </a>
        </nav>

        {/* Actions */}
        <div className="flex items-center space-x-4">
          
          {/* Language Switcher */}
          <button
            onClick={toggleLanguage}
            className="flex items-center space-x-1 px-2.5 py-1.5 rounded-lg bg-slate-800/80 hover:bg-slate-700 text-xs font-semibold text-slate-200 border border-slate-700 transition"
            title="Switch Language"
          >
            <Globe className="w-3.5 h-3.5 text-blue-400" />
            <span>{i18n.language === 'uk' ? 'UA' : 'EN'}</span>
          </button>

          {/* Telegram Bot Link */}
          <a
            href="https://t.me/CyfraHubBot"
            target="_blank"
            rel="noopener noreferrer"
            className="hidden sm:flex items-center space-x-1.5 px-3 py-1.5 rounded-lg bg-sky-500/10 hover:bg-sky-500/20 text-sky-400 border border-sky-500/30 text-xs font-medium transition"
          >
            <Send className="w-3.5 h-3.5" />
            <span>Telegram Bot</span>
          </a>

          {/* User Wallet */}
          <button
            onClick={onOpenWallet}
            className="flex items-center space-x-2 px-3 py-1.5 rounded-lg bg-slate-800 hover:bg-slate-700 border border-slate-700 text-xs transition"
          >
            <WalletIcon className="w-4 h-4 text-emerald-400" />
            <div className="text-left">
              <span className="font-bold text-slate-100">
                {wallet ? Number(wallet.balanceAvailable).toFixed(2) : '1,000.00'} ₴
              </span>
              {wallet && wallet.balanceFrozen > 0 && (
                <span className="text-[10px] text-amber-400 block">
                  (🔒 {Number(wallet.balanceFrozen).toFixed(2)})
                </span>
              )}
            </div>
            <PlusCircle className="w-3.5 h-3.5 text-blue-400 ml-1" />
          </button>

          {/* User Auth */}
          {user ? (
            <div className="flex items-center space-x-2">
              <div className="text-xs text-right hidden sm:block">
                <span className="font-semibold text-slate-200 block">{user.username}</span>
                <span className="text-[10px] text-emerald-400">Онлайн</span>
              </div>
              <button
                onClick={onLogout}
                className="px-2.5 py-1.5 text-xs text-slate-400 hover:text-red-400 transition"
              >
                {t('nav.logout')}
              </button>
            </div>
          ) : (
            <button
              onClick={onOpenAuth}
              className="flex items-center space-x-1.5 px-3 py-1.5 rounded-lg bg-blue-600 hover:bg-blue-500 text-white font-medium text-xs shadow-lg shadow-blue-600/30 transition"
            >
              <UserCheck className="w-3.5 h-3.5" />
              <span>{t('nav.login')}</span>
            </button>
          )}

        </div>

      </div>
    </header>
  );
};
