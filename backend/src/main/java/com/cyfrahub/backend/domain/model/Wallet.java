package com.cyfrahub.backend.domain.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "wallets")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @Column(name = "balance_available", nullable = false, precision = 12, scale = 2)
    private BigDecimal balanceAvailable = BigDecimal.ZERO;

    @Column(name = "balance_frozen", nullable = false, precision = 12, scale = 2)
    private BigDecimal balanceFrozen = BigDecimal.ZERO;

    @Column(nullable = false, length = 10)
    private String currency = "UAH";

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    public Wallet() {}

    public Wallet(Long id, User user, BigDecimal balanceAvailable, BigDecimal balanceFrozen, String currency, OffsetDateTime updatedAt) {
        this.id = id;
        this.user = user;
        this.balanceAvailable = balanceAvailable != null ? balanceAvailable : BigDecimal.ZERO;
        this.balanceFrozen = balanceFrozen != null ? balanceFrozen : BigDecimal.ZERO;
        this.currency = currency != null ? currency : "UAH";
        this.updatedAt = updatedAt != null ? updatedAt : OffsetDateTime.now();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private User user;
        private BigDecimal balanceAvailable = BigDecimal.ZERO;
        private BigDecimal balanceFrozen = BigDecimal.ZERO;
        private String currency = "UAH";
        private OffsetDateTime updatedAt = OffsetDateTime.now();

        public Builder id(Long id) { this.id = id; return this; }
        public Builder user(User user) { this.user = user; return this; }
        public Builder balanceAvailable(BigDecimal balanceAvailable) { this.balanceAvailable = balanceAvailable; return this; }
        public Builder balanceFrozen(BigDecimal balanceFrozen) { this.balanceFrozen = balanceFrozen; return this; }
        public Builder currency(String currency) { this.currency = currency; return this; }
        public Builder updatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Wallet build() {
            return new Wallet(id, user, balanceAvailable, balanceFrozen, currency, updatedAt);
        }
    }

    public void holdFunds(BigDecimal amount) {
        if (this.balanceAvailable.compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient funds to hold");
        }
        this.balanceAvailable = this.balanceAvailable.subtract(amount);
        this.balanceFrozen = this.balanceFrozen.add(amount);
        this.updatedAt = OffsetDateTime.now();
    }

    public void releaseFrozenFundsTo(Wallet recipient, BigDecimal totalAmount, BigDecimal fee) {
        if (this.balanceFrozen.compareTo(totalAmount) < 0) {
            throw new IllegalStateException("Insufficient frozen funds");
        }
        this.balanceFrozen = this.balanceFrozen.subtract(totalAmount);
        BigDecimal netEarning = totalAmount.subtract(fee);
        recipient.setBalanceAvailable(recipient.getBalanceAvailable().add(netEarning));
        recipient.setUpdatedAt(OffsetDateTime.now());
        this.updatedAt = OffsetDateTime.now();
    }

    public void refundFrozenFunds(BigDecimal amount) {
        if (this.balanceFrozen.compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient frozen funds for refund");
        }
        this.balanceFrozen = this.balanceFrozen.subtract(amount);
        this.balanceAvailable = this.balanceAvailable.add(amount);
        this.updatedAt = OffsetDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public BigDecimal getBalanceAvailable() { return balanceAvailable; }
    public void setBalanceAvailable(BigDecimal balanceAvailable) { this.balanceAvailable = balanceAvailable; }
    public BigDecimal getBalanceFrozen() { return balanceFrozen; }
    public void setBalanceFrozen(BigDecimal balanceFrozen) { this.balanceFrozen = balanceFrozen; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}
