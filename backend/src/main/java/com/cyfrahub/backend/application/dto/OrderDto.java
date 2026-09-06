package com.cyfrahub.backend.application.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class OrderDto {

    public static class CreateOrderRequest {
        private Long lotId;
        private Integer amount;

        public CreateOrderRequest() {}
        public CreateOrderRequest(Long lotId, Integer amount) {
            this.lotId = lotId;
            this.amount = amount;
        }

        public Long getLotId() { return lotId; }
        public void setLotId(Long lotId) { this.lotId = lotId; }
        public Integer getAmount() { return amount; }
        public void setAmount(Integer amount) { this.amount = amount; }
    }

    public static class OrderResponse {
        private Long id;
        private String orderNumber;
        private Long buyerId;
        private String buyerUsername;
        private Long sellerId;
        private String sellerUsername;
        private Long lotId;
        private String lotTitleUa;
        private String lotTitleEn;
        private String gameNameUa;
        private String gameNameEn;
        private String tradeType;
        private Integer amount;
        private BigDecimal totalPrice;
        private BigDecimal platformFee;
        private BigDecimal sellerNetEarnings;
        private String status;
        private String secretPayload;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;

        public OrderResponse() {}
        public OrderResponse(Long id, String orderNumber, Long buyerId, String buyerUsername, Long sellerId, String sellerUsername, Long lotId, String lotTitleUa, String lotTitleEn, String gameNameUa, String gameNameEn, String tradeType, Integer amount, BigDecimal totalPrice, BigDecimal platformFee, BigDecimal sellerNetEarnings, String status, String secretPayload, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
            this.id = id;
            this.orderNumber = orderNumber;
            this.buyerId = buyerId;
            this.buyerUsername = buyerUsername;
            this.sellerId = sellerId;
            this.sellerUsername = sellerUsername;
            this.lotId = lotId;
            this.lotTitleUa = lotTitleUa;
            this.lotTitleEn = lotTitleEn;
            this.gameNameUa = gameNameUa;
            this.gameNameEn = gameNameEn;
            this.tradeType = tradeType;
            this.amount = amount;
            this.totalPrice = totalPrice;
            this.platformFee = platformFee;
            this.sellerNetEarnings = sellerNetEarnings;
            this.status = status;
            this.secretPayload = secretPayload;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private Long id;
            private String orderNumber;
            private Long buyerId;
            private String buyerUsername;
            private Long sellerId;
            private String sellerUsername;
            private Long lotId;
            private String lotTitleUa;
            private String lotTitleEn;
            private String gameNameUa;
            private String gameNameEn;
            private String tradeType;
            private Integer amount;
            private BigDecimal totalPrice;
            private BigDecimal platformFee;
            private BigDecimal sellerNetEarnings;
            private String status;
            private String secretPayload;
            private OffsetDateTime createdAt;
            private OffsetDateTime updatedAt;

            public Builder id(Long id) { this.id = id; return this; }
            public Builder orderNumber(String orderNumber) { this.orderNumber = orderNumber; return this; }
            public Builder buyerId(Long buyerId) { this.buyerId = buyerId; return this; }
            public Builder buyerUsername(String buyerUsername) { this.buyerUsername = buyerUsername; return this; }
            public Builder sellerId(Long sellerId) { this.sellerId = sellerId; return this; }
            public Builder sellerUsername(String sellerUsername) { this.sellerUsername = sellerUsername; return this; }
            public Builder lotId(Long lotId) { this.lotId = lotId; return this; }
            public Builder lotTitleUa(String lotTitleUa) { this.lotTitleUa = lotTitleUa; return this; }
            public Builder lotTitleEn(String lotTitleEn) { this.lotTitleEn = lotTitleEn; return this; }
            public Builder gameNameUa(String gameNameUa) { this.gameNameUa = gameNameUa; return this; }
            public Builder gameNameEn(String gameNameEn) { this.gameNameEn = gameNameEn; return this; }
            public Builder tradeType(String tradeType) { this.tradeType = tradeType; return this; }
            public Builder amount(Integer amount) { this.amount = amount; return this; }
            public Builder totalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; return this; }
            public Builder platformFee(BigDecimal platformFee) { this.platformFee = platformFee; return this; }
            public Builder sellerNetEarnings(BigDecimal sellerNetEarnings) { this.sellerNetEarnings = sellerNetEarnings; return this; }
            public Builder status(String status) { this.status = status; return this; }
            public Builder secretPayload(String secretPayload) { this.secretPayload = secretPayload; return this; }
            public Builder createdAt(OffsetDateTime createdAt) { this.createdAt = createdAt; return this; }
            public Builder updatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

            public OrderResponse build() {
                return new OrderResponse(id, orderNumber, buyerId, buyerUsername, sellerId, sellerUsername, lotId, lotTitleUa, lotTitleEn, gameNameUa, gameNameEn, tradeType, amount, totalPrice, platformFee, sellerNetEarnings, status, secretPayload, createdAt, updatedAt);
            }
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getOrderNumber() { return orderNumber; }
        public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
        public Long getBuyerId() { return buyerId; }
        public void setBuyerId(Long buyerId) { this.buyerId = buyerId; }
        public String getBuyerUsername() { return buyerUsername; }
        public void setBuyerUsername(String buyerUsername) { this.buyerUsername = buyerUsername; }
        public Long getSellerId() { return sellerId; }
        public void setSellerId(Long sellerId) { this.sellerId = sellerId; }
        public String getSellerUsername() { return sellerUsername; }
        public void setSellerUsername(String sellerUsername) { this.sellerUsername = sellerUsername; }
        public Long getLotId() { return lotId; }
        public void setLotId(Long lotId) { this.lotId = lotId; }
        public String getLotTitleUa() { return lotTitleUa; }
        public void setLotTitleUa(String lotTitleUa) { this.lotTitleUa = lotTitleUa; }
        public String getLotTitleEn() { return lotTitleEn; }
        public void setLotTitleEn(String lotTitleEn) { this.lotTitleEn = lotTitleEn; }
        public String getGameNameUa() { return gameNameUa; }
        public void setGameNameUa(String gameNameUa) { this.gameNameUa = gameNameUa; }
        public String getGameNameEn() { return gameNameEn; }
        public void setGameNameEn(String gameNameEn) { this.gameNameEn = gameNameEn; }
        public String getTradeType() { return tradeType; }
        public void setTradeType(String tradeType) { this.tradeType = tradeType; }
        public Integer getAmount() { return amount; }
        public void setAmount(Integer amount) { this.amount = amount; }
        public BigDecimal getTotalPrice() { return totalPrice; }
        public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
        public BigDecimal getPlatformFee() { return platformFee; }
        public void setPlatformFee(BigDecimal platformFee) { this.platformFee = platformFee; }
        public BigDecimal getSellerNetEarnings() { return sellerNetEarnings; }
        public void setSellerNetEarnings(BigDecimal sellerNetEarnings) { this.sellerNetEarnings = sellerNetEarnings; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getSecretPayload() { return secretPayload; }
        public void setSecretPayload(String secretPayload) { this.secretPayload = secretPayload; }
        public OffsetDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
        public OffsetDateTime getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
    }
}
