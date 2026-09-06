package com.cyfrahub.backend.application.service;

import com.cyfrahub.backend.application.dto.LotDto;
import com.cyfrahub.backend.domain.model.Game;
import com.cyfrahub.backend.domain.model.TradeLot;
import com.cyfrahub.backend.domain.model.TradeType;
import com.cyfrahub.backend.domain.model.User;
import com.cyfrahub.backend.domain.repository.GameRepository;
import com.cyfrahub.backend.domain.repository.TradeLotRepository;
import com.cyfrahub.backend.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class TradeLotService {

    private final TradeLotRepository lotRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    public TradeLotService(TradeLotRepository lotRepository, GameRepository gameRepository, UserRepository userRepository) {
        this.lotRepository = lotRepository;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<LotDto.LotResponse> getLotsByGame(Long gameId) {
        return lotRepository.findByGameIdAndIsActiveTrueOrderByPricePerUnitAsc(gameId)
                .stream().map(this::mapToResponse).toList();
    }

    @Transactional(readOnly = true)
    public LotDto.LotResponse getLotById(Long lotId) {
        TradeLot lot = lotRepository.findById(lotId)
                .orElseThrow(() -> new IllegalArgumentException("Lot not found"));
        return mapToResponse(lot);
    }

    @Transactional
    public LotDto.LotResponse createLot(Long sellerId, LotDto.CreateLotRequest request) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new IllegalArgumentException("Seller not found"));
        Game game = gameRepository.findById(request.getGameId())
                .orElseThrow(() -> new IllegalArgumentException("Game not found"));

        TradeType tradeType = "INSTANT_AUTO".equalsIgnoreCase(request.getTradeType())
                ? TradeType.INSTANT_AUTO : TradeType.MANUAL_P2P;

        TradeLot lot = TradeLot.builder()
                .seller(seller)
                .game(game)
                .titleUa(request.getTitleUa())
                .titleEn(request.getTitleEn())
                .descriptionUa(request.getDescriptionUa())
                .descriptionEn(request.getDescriptionEn())
                .tradeType(tradeType)
                .pricePerUnit(request.getPricePerUnit())
                .minAmount(request.getMinAmount() != null ? request.getMinAmount() : 1)
                .availableAmount(request.getAvailableAmount() != null ? request.getAvailableAmount() : 1)
                .serverName(request.getServerName())
                .sideName(request.getSideName())
                .isActive(true)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();

        lot = lotRepository.save(lot);
        return mapToResponse(lot);
    }

    private LotDto.LotResponse mapToResponse(TradeLot lot) {
        return LotDto.LotResponse.builder()
                .id(lot.getId())
                .sellerId(lot.getSeller().getId())
                .sellerUsername(lot.getSeller().getUsername())
                .sellerRating(lot.getSeller().getRating())
                .sellerReviewsCount(lot.getSeller().getReviewsCount())
                .sellerIsOnline(lot.getSeller().getIsOnline())
                .gameId(lot.getGame().getId())
                .gameNameUa(lot.getGame().getNameUa())
                .gameNameEn(lot.getGame().getNameEn())
                .titleUa(lot.getTitleUa())
                .titleEn(lot.getTitleEn())
                .descriptionUa(lot.getDescriptionUa())
                .descriptionEn(lot.getDescriptionEn())
                .tradeType(lot.getTradeType().name())
                .pricePerUnit(lot.getPricePerUnit())
                .minAmount(lot.getMinAmount())
                .availableAmount(lot.getAvailableAmount())
                .serverName(lot.getServerName())
                .sideName(lot.getSideName())
                .createdAt(lot.getCreatedAt())
                .build();
    }
}
