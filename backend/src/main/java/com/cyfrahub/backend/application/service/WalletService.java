package com.cyfrahub.backend.application.service;

import com.cyfrahub.backend.application.dto.WalletDto;
import com.cyfrahub.backend.domain.model.Wallet;
import com.cyfrahub.backend.domain.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Transactional(readOnly = true)
    public WalletDto.WalletResponse getWalletByUserId(Long userId) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found for user"));

        return WalletDto.WalletResponse.builder()
                .id(wallet.getId())
                .userId(wallet.getUser().getId())
                .balanceAvailable(wallet.getBalanceAvailable())
                .balanceFrozen(wallet.getBalanceFrozen())
                .currency(wallet.getCurrency())
                .build();
    }

    @Transactional
    public WalletDto.WalletResponse deposit(Long userId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found for user"));

        wallet.setBalanceAvailable(wallet.getBalanceAvailable().add(amount));
        wallet.setUpdatedAt(OffsetDateTime.now());
        wallet = walletRepository.save(wallet);

        return getWalletByUserId(userId);
    }

    @Transactional
    public WalletDto.WalletResponse withdraw(Long userId, WalletDto.WithdrawRequest request) {
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }

        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found for user"));

        if (wallet.getBalanceAvailable().compareTo(request.getAmount()) < 0) {
            throw new IllegalStateException("Insufficient funds for withdrawal");
        }

        wallet.setBalanceAvailable(wallet.getBalanceAvailable().subtract(request.getAmount()));
        wallet.setUpdatedAt(OffsetDateTime.now());
        wallet = walletRepository.save(wallet);

        return getWalletByUserId(userId);
    }
}
