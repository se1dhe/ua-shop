package com.cyfrahub.backend.application.service;

import com.cyfrahub.backend.application.dto.AuthDto;
import com.cyfrahub.backend.domain.model.User;
import com.cyfrahub.backend.domain.model.Wallet;
import com.cyfrahub.backend.domain.repository.UserRepository;
import com.cyfrahub.backend.domain.repository.WalletRepository;
import com.cyfrahub.backend.infrastructure.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository, WalletRepository walletRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public AuthDto.AuthResponse register(AuthDto.RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("User with this email already exists");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }

        User user = User.builder()
                .email(request.getEmail().trim().toLowerCase())
                .username(request.getUsername().trim())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .telegramUsername(request.getTelegramUsername())
                .role("ROLE_USER")
                .isOnline(true)
                .build();

        user = userRepository.save(user);

        // Auto-create initial Wallet for new user
        Wallet wallet = Wallet.builder()
                .user(user)
                .balanceAvailable(new BigDecimal("1000.00")) // Welcome 1000 UAH balance for demo/testing
                .balanceFrozen(BigDecimal.ZERO)
                .currency("UAH")
                .build();
        walletRepository.save(wallet);

        String token = jwtTokenProvider.generateToken(user.getId(), user.getUsername(), user.getRole());

        return AuthDto.AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .telegramUsername(user.getTelegramUsername())
                .build();
    }

    public AuthDto.AuthResponse login(AuthDto.LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        user.setIsOnline(true);
        userRepository.save(user);

        String token = jwtTokenProvider.generateToken(user.getId(), user.getUsername(), user.getRole());

        return AuthDto.AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .telegramUsername(user.getTelegramUsername())
                .build();
    }
}
