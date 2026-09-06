package com.cyfrahub.backend.domain.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(unique = true, nullable = false, length = 100)
    private String username;

    @Column(nullable = false, length = 50)
    private String role = "ROLE_USER";

    @Column(name = "telegram_chat_id")
    private Long telegramChatId;

    @Column(name = "telegram_username")
    private String telegramUsername;

    @Column(precision = 3, scale = 2)
    private BigDecimal rating = new BigDecimal("5.00");

    @Column(name = "reviews_count")
    private Integer reviewsCount = 0;

    @Column(name = "is_online")
    private Boolean isOnline = false;

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    public User() {}

    public User(Long id, String email, String passwordHash, String username, String role, Long telegramChatId, String telegramUsername, BigDecimal rating, Integer reviewsCount, Boolean isOnline, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.username = username;
        this.role = role != null ? role : "ROLE_USER";
        this.telegramChatId = telegramChatId;
        this.telegramUsername = telegramUsername;
        this.rating = rating != null ? rating : new BigDecimal("5.00");
        this.reviewsCount = reviewsCount != null ? reviewsCount : 0;
        this.isOnline = isOnline != null ? isOnline : false;
        this.createdAt = createdAt != null ? createdAt : OffsetDateTime.now();
        this.updatedAt = updatedAt != null ? updatedAt : OffsetDateTime.now();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String email;
        private String passwordHash;
        private String username;
        private String role = "ROLE_USER";
        private Long telegramChatId;
        private String telegramUsername;
        private BigDecimal rating = new BigDecimal("5.00");
        private Integer reviewsCount = 0;
        private Boolean isOnline = false;
        private OffsetDateTime createdAt = OffsetDateTime.now();
        private OffsetDateTime updatedAt = OffsetDateTime.now();

        public Builder id(Long id) { this.id = id; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder passwordHash(String passwordHash) { this.passwordHash = passwordHash; return this; }
        public Builder username(String username) { this.username = username; return this; }
        public Builder role(String role) { this.role = role; return this; }
        public Builder telegramChatId(Long telegramChatId) { this.telegramChatId = telegramChatId; return this; }
        public Builder telegramUsername(String telegramUsername) { this.telegramUsername = telegramUsername; return this; }
        public Builder rating(BigDecimal rating) { this.rating = rating; return this; }
        public Builder reviewsCount(Integer reviewsCount) { this.reviewsCount = reviewsCount; return this; }
        public Builder isOnline(Boolean isOnline) { this.isOnline = isOnline; return this; }
        public Builder createdAt(OffsetDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public User build() {
            return new User(id, email, passwordHash, username, role, telegramChatId, telegramUsername, rating, reviewsCount, isOnline, createdAt, updatedAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Long getTelegramChatId() { return telegramChatId; }
    public void setTelegramChatId(Long telegramChatId) { this.telegramChatId = telegramChatId; }
    public String getTelegramUsername() { return telegramUsername; }
    public void setTelegramUsername(String telegramUsername) { this.telegramUsername = telegramUsername; }
    public BigDecimal getRating() { return rating; }
    public void setRating(BigDecimal rating) { this.rating = rating; }
    public Integer getReviewsCount() { return reviewsCount; }
    public void setReviewsCount(Integer reviewsCount) { this.reviewsCount = reviewsCount; }
    public Boolean getIsOnline() { return isOnline; }
    public void setIsOnline(Boolean isOnline) { this.isOnline = isOnline; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}
