package com.cyfrahub.backend.interfaces.web;

import com.cyfrahub.backend.application.dto.WalletDto;
import com.cyfrahub.backend.application.service.WalletService;
import com.cyfrahub.backend.domain.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping
    public ResponseEntity<WalletDto.WalletResponse> getWallet(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(walletService.getWalletByUserId(user.getId()));
    }

    @PostMapping("/deposit")
    public ResponseEntity<WalletDto.WalletResponse> deposit(
            @AuthenticationPrincipal User user,
            @RequestBody WalletDto.DepositRequest request) {
        return ResponseEntity.ok(walletService.deposit(user.getId(), request.getAmount()));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<WalletDto.WalletResponse> withdraw(
            @AuthenticationPrincipal User user,
            @RequestBody WalletDto.WithdrawRequest request) {
        return ResponseEntity.ok(walletService.withdraw(user.getId(), request));
    }
}
