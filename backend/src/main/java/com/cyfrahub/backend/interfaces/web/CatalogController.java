package com.cyfrahub.backend.interfaces.web;

import com.cyfrahub.backend.application.dto.GameDto;
import com.cyfrahub.backend.application.service.CategoryGameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog")
public class CatalogController {

    private final CategoryGameService categoryGameService;

    public CatalogController(CategoryGameService categoryGameService) {
        this.categoryGameService = categoryGameService;
    }

    @GetMapping("/categories")
    public ResponseEntity<List<GameDto.CategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(categoryGameService.getAllCategoriesWithGames());
    }

    @GetMapping("/categories/{categoryId}/games")
    public ResponseEntity<List<GameDto.GameResponse>> getGamesByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(categoryGameService.getGamesByCategory(categoryId));
    }
}
