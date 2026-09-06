package com.cyfrahub.backend.interfaces.web;

import com.cyfrahub.backend.application.dto.LotDto;
import com.cyfrahub.backend.application.service.TradeLotService;
import com.cyfrahub.backend.domain.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lots")
public class LotController {

    private final TradeLotService tradeLotService;

    public LotController(TradeLotService tradeLotService) {
        this.tradeLotService = tradeLotService;
    }

    @GetMapping("/game/{gameId}")
    public ResponseEntity<List<LotDto.LotResponse>> getLotsByGame(@PathVariable Long gameId) {
        return ResponseEntity.ok(tradeLotService.getLotsByGame(gameId));
    }

    @GetMapping("/{lotId}")
    public ResponseEntity<LotDto.LotResponse> getLotById(@PathVariable Long lotId) {
        return ResponseEntity.ok(tradeLotService.getLotById(lotId));
    }

    @PostMapping
    public ResponseEntity<LotDto.LotResponse> createLot(
            @AuthenticationPrincipal User user,
            @RequestBody LotDto.CreateLotRequest request) {
        return ResponseEntity.ok(tradeLotService.createLot(user.getId(), request));
    }
}
