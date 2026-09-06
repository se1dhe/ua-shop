import React from 'react';
import { useTranslation } from 'react-i18next';
import { Zap, ShieldCheck, Star, ShoppingCart } from 'lucide-react';
import { TradeLot } from '../types';

interface LotTableProps {
  lots: TradeLot[];
  onSelectLot: (lot: TradeLot) => void;
}

export const LotTable: React.FC<LotTableProps> = ({ lots, onSelectLot }) => {
  const { t, i18n } = useTranslation();
  const isUa = i18n.language === 'uk';

  if (lots.length === 0) {
    return (
      <div className="text-center py-12 bg-slate-900/50 rounded-2xl border border-slate-800">
        <p className="text-slate-400 text-sm">
          {isUa ? 'У цій категорії поки немає активних лотів.' : 'No active trade lots in this category yet.'}
        </p>
      </div>
    );
  }

  return (
    <div className="overflow-x-auto rounded-2xl border border-slate-800 bg-[#0F172A]/80 backdrop-blur shadow-xl">
      <table className="w-full text-left text-sm text-slate-300">
        <thead className="bg-slate-950/60 text-xs uppercase font-semibold text-slate-400 border-b border-slate-800">
          <tr>
            <th className="px-5 py-4">{isUa ? 'Опис пропозиції' : 'Offer Description'}</th>
            <th className="px-4 py-4">{t('trade.server')}</th>
            <th className="px-4 py-4">{t('trade.available')}</th>
            <th className="px-4 py-4">{t('trade.seller')}</th>
            <th className="px-5 py-4 text-right">{t('trade.price_per_unit')}</th>
            <th className="px-4 py-4 text-center">{isUa ? 'Дія' : 'Action'}</th>
          </tr>
        </thead>
        <tbody className="divide-y divide-slate-800/60">
          {lots.map((lot) => {
            const isAuto = lot.tradeType === 'INSTANT_AUTO';
            return (
              <tr 
                key={lot.id} 
                className="hover:bg-slate-800/40 transition-colors group cursor-pointer"
                onClick={() => onSelectLot(lot)}
              >
                {/* Description & Badge */}
                <td className="px-5 py-4">
                  <div className="flex items-center space-x-2">
                    <span className="font-medium text-slate-100 group-hover:text-blue-400 transition-colors">
                      {isUa ? lot.titleUa : lot.titleEn}
                    </span>
                    {isAuto ? (
                      <span className="inline-flex items-center space-x-1 px-2 py-0.5 rounded-full text-[11px] font-semibold bg-emerald-500/10 text-emerald-400 border border-emerald-500/20">
                        <Zap className="w-3 h-3 text-emerald-400" />
                        <span>{t('trade.instant_auto')}</span>
                      </span>
                    ) : (
                      <span className="inline-flex items-center space-x-1 px-2 py-0.5 rounded-full text-[11px] font-semibold bg-blue-500/10 text-blue-400 border border-blue-500/20">
                        <ShieldCheck className="w-3 h-3 text-blue-400" />
                        <span>{t('trade.manual_p2p')}</span>
                      </span>
                    )}
                  </div>
                  <p className="text-xs text-slate-400 mt-0.5 line-clamp-1">
                    {isUa ? lot.descriptionUa : lot.descriptionEn}
                  </p>
                </td>

                {/* Server / Side */}
                <td className="px-4 py-4 whitespace-nowrap text-xs text-slate-300">
                  {lot.serverName ? (
                    <div>
                      <span className="font-semibold text-slate-200">{lot.serverName}</span>
                      {lot.sideName && (
                        <span className="text-slate-400 block text-[11px]">{lot.sideName}</span>
                      )}
                    </div>
                  ) : (
                    <span className="text-slate-500">—</span>
                  )}
                </td>

                {/* Available */}
                <td className="px-4 py-4 whitespace-nowrap">
                  <span className="font-bold text-slate-200">{lot.availableAmount}</span>
                  <span className="text-slate-400 text-xs block">
                    {t('trade.min_order')}: {lot.minAmount}
                  </span>
                </td>

                {/* Seller */}
                <td className="px-4 py-4 whitespace-nowrap">
                  <div className="flex items-center space-x-2">
                    <div className="relative">
                      <div className="w-7 h-7 rounded-full bg-slate-700 flex items-center justify-center font-bold text-xs text-slate-200">
                        {lot.sellerUsername.substring(0, 2).toUpperCase()}
                      </div>
                      {lot.sellerIsOnline && (
                        <span className="absolute bottom-0 right-0 w-2 h-2 rounded-full bg-emerald-500 ring-2 ring-slate-900" title={t('trade.online')} />
                      )}
                    </div>
                    <div>
                      <span className="font-medium text-slate-200 text-xs block">{lot.sellerUsername}</span>
                      <div className="flex items-center space-x-1 text-[11px] text-amber-400">
                        <Star className="w-3 h-3 fill-amber-400 text-amber-400" />
                        <span className="font-semibold">{Number(lot.sellerRating).toFixed(1)}</span>
                        <span className="text-slate-500">({lot.sellerReviewsCount})</span>
                      </div>
                    </div>
                  </div>
                </td>

                {/* Price */}
                <td className="px-5 py-4 whitespace-nowrap text-right">
                  <span className="text-base font-extrabold text-emerald-400">
                    {Number(lot.pricePerUnit).toFixed(2)} ₴
                  </span>
                </td>

                {/* Buy Button */}
                <td className="px-4 py-4 whitespace-nowrap text-center">
                  <button
                    onClick={(e) => {
                      e.stopPropagation();
                      onSelectLot(lot);
                    }}
                    className="inline-flex items-center space-x-1.5 px-3 py-1.5 rounded-xl bg-blue-600 hover:bg-blue-500 text-white font-semibold text-xs shadow-lg shadow-blue-600/20 transition group-hover:scale-105"
                  >
                    <ShoppingCart className="w-3.5 h-3.5" />
                    <span>{t('trade.buy')}</span>
                  </button>
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
};
