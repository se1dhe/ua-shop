package com.cyfrahub.backend.domain.repository;

import com.cyfrahub.backend.domain.model.TradeLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeLotRepository extends JpaRepository<TradeLot, Long> {
    List<TradeLot> findByGameIdAndIsActiveTrueOrderByPricePerUnitAsc(Long gameId);
    List<TradeLot> findBySellerIdOrderByCreatedAtDesc(Long sellerId);
}
