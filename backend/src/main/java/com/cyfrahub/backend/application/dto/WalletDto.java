package com.cyfrahub.backend.application.dto;

import java.math.BigDecimal;

public class WalletDto {

    public static class WalletResponse {
        private Long id;
        private Long userId;
        private BigDecimal balanceAvailable;
        private BigDecimal balanceFrozen;
        private String currency;

        public WalletResponse() {}
        public WalletResponse(Long id, Long userId, BigDecimal balanceAvailable, BigDecimal balanceFrozen, String currency) {
            this.id = id;
            this.userId = userId;
            this.balanceAvailable = balanceAvailable;
            this.balanceFrozen = balanceFrozen;
            this.currency = currency;
        }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private Long id;
            private Long userId;
            private BigDecimal balanceAvailable;
            private BigDecimal balanceFrozen;
            private String currency;

            public Builder id(Long id) { this.id = id; return this; }
            public Builder userId(Long userId) { this.userId = userId; return this; }
            public Builder balanceAvailable(BigDecimal balanceAvailable) { this.balanceAvailable = balanceAvailable; return this; }
            public Builder balanceFrozen(BigDecimal balanceFrozen) { this.balanceFrozen = balanceFrozen; return this; }
            public Builder currency(String currency) { this.currency = currency; return this; }

            public WalletResponse build() {
                return new WalletResponse(id, userId, balanceAvailable, balanceFrozen, currency);
            }
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public BigDecimal getBalanceAvailable() { return balanceAvailable; }
        public void setBalanceAvailable(BigDecimal balanceAvailable) { this.balanceAvailable = balanceAvailable; }
        public BigDecimal getBalanceFrozen() { return balanceFrozen; }
        public void setBalanceFrozen(BigDecimal balanceFrozen) { this.balanceFrozen = balanceFrozen; }
        public String getCurrency() { return currency; }
        public void setCurrency(String currency) { this.currency = currency; }
    }

    public static class DepositRequest {
        private BigDecimal amount;

        public DepositRequest() {}
        public DepositRequest(BigDecimal amount) { this.amount = amount; }

        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
    }

    public static class WithdrawRequest {
        private BigDecimal amount;
        private String cardNumber;
        private String cardHolderName;

        public WithdrawRequest() {}
        public WithdrawRequest(BigDecimal amount, String cardNumber, String cardHolderName) {
            this.amount = amount;
            this.cardNumber = cardNumber;
            this.cardHolderName = cardHolderName;
        }

        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public String getCardNumber() { return cardNumber; }
        public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
        public String getCardHolderName() { return cardHolderName; }
        public void setCardHolderName(String cardHolderName) { this.cardHolderName = cardHolderName; }
    }
}
