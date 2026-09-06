package com.cyfrahub.backend.application.service;

import com.cyfrahub.backend.application.dto.GameDto;
import com.cyfrahub.backend.domain.model.Category;
import com.cyfrahub.backend.domain.model.Game;
import com.cyfrahub.backend.domain.repository.CategoryRepository;
import com.cyfrahub.backend.domain.repository.GameRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryGameService {

    private final CategoryRepository categoryRepository;
    private final GameRepository gameRepository;

    public CategoryGameService(CategoryRepository categoryRepository, GameRepository gameRepository) {
        this.categoryRepository = categoryRepository;
        this.gameRepository = gameRepository;
    }

    @Transactional(readOnly = true)
    public List<GameDto.CategoryResponse> getAllCategoriesWithGames() {
        List<Category> categories = categoryRepository.findAllByOrderBySortOrderAsc();
        return categories.stream().map(cat -> {
            List<Game> games = gameRepository.findByCategoryIdAndIsActiveTrueOrderBySortOrderAsc(cat.getId());
            List<GameDto.GameResponse> gameDtos = games.stream().map(this::mapToGameResponse).toList();
            return GameDto.CategoryResponse.builder()
                    .id(cat.getId())
                    .code(cat.getCode())
                    .nameUa(cat.getNameUa())
                    .nameEn(cat.getNameEn())
                    .type(cat.getType().name())
                    .icon(cat.getIcon())
                    .games(gameDtos)
                    .build();
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<GameDto.GameResponse> getGamesByCategory(Long categoryId) {
        return gameRepository.findByCategoryIdAndIsActiveTrueOrderBySortOrderAsc(categoryId)
                .stream().map(this::mapToGameResponse).toList();
    }

    private GameDto.GameResponse mapToGameResponse(Game game) {
        return GameDto.GameResponse.builder()
                .id(game.getId())
                .categoryId(game.getCategory().getId())
                .code(game.getCode())
                .nameUa(game.getNameUa())
                .nameEn(game.getNameEn())
                .imageUrl(game.getImageUrl())
                .build();
    }
}
