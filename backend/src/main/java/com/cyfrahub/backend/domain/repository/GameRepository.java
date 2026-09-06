package com.cyfrahub.backend.domain.repository;

import com.cyfrahub.backend.domain.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<Game> findByCode(String code);
    List<Game> findByCategoryIdAndIsActiveTrueOrderBySortOrderAsc(Long categoryId);
    List<Game> findByIsActiveTrueOrderBySortOrderAsc();
}
