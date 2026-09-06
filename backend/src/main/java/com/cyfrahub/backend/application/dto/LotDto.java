package com.cyfrahub.backend.application.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class LotDto {

    public static class LotResponse {
        private Long id;
        private Long sellerId;
        private String sellerUsername;
        private BigDecimal sellerRating;
        private Integer sellerReviewsCount;
        private Boolean sellerIsOnline;
        private Long gameId;
        private String gameNameUa;
        private String gameNameEn;
        private String titleUa;
        private String titleEn;
        private String descriptionUa;
        private String descriptionEn;
        private String tradeType;
        private BigDecimal pricePerUnit;
        private Integer minAmount;
        private Integer availableAmount;
        private String serverName;
        private String sideName;
        private OffsetDateTime createdAt;

        public LotResponse() {}
        public LotResponse(Long id, Long sellerId, String sellerUsername, BigDecimal sellerRating, Integer sellerReviewsCount, Boolean sellerIsOnline, Long gameId, String gameNameUa, String gameNameEn, String titleUa, String titleEn, String descriptionUa, String descriptionEn, String tradeType, BigDecimal pricePerUnit, Integer minAmount, Integer availableAmount, String serverName, String sideName, OffsetDateTime createdAt) {
            this.id = id;
            this.sellerId = sellerId;
            this.sellerUsername = sellerUsername;
            this.sellerRating = sellerRating;
            this.sellerReviewsCount = sellerReviewsCount;
            this.sellerIsOnline = sellerIsOnline;
            this.gameId = gameId;
            this.gameNameUa = gameNameUa;
            this.gameNameEn = gameNameEn;
            this.titleUa = titleUa;
            this.titleEn = titleEn;
            this.descriptionUa = descriptionUa;
            this.descriptionEn = descriptionEn;
            this.tradeType = tradeType;
            this.pricePerUnit = pricePerUnit;
            this.minAmount = minAmount;
            this.availableAmount = availableAmount;
            this.serverName = serverName;
            this.sideName = sideName;
            this.createdAt = createdAt;
        }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private Long id;
            private Long sellerId;
            private String sellerUsername;
            private BigDecimal sellerRating;
            private Integer sellerReviewsCount;
            private Boolean sellerIsOnline;
            private Long gameId;
            private String gameNameUa;
            private String gameNameEn;
            private String titleUa;
            private String titleEn;
            private String descriptionUa;
            private String descriptionEn;
            private String tradeType;
            private BigDecimal pricePerUnit;
            private Integer minAmount;
            private Integer availableAmount;
            private String serverName;
            private String sideName;
            private OffsetDateTime createdAt;

            public Builder id(Long id) { this.id = id; return this; }
            public Builder sellerId(Long sellerId) { this.sellerId = sellerId; return this; }
            public Builder sellerUsername(String sellerUsername) { this.sellerUsername = sellerUsername; return this; }
            public Builder sellerRating(BigDecimal sellerRating) { this.sellerRating = sellerRating; return this; }
            public Builder sellerReviewsCount(Integer sellerReviewsCount) { this.sellerReviewsCount = sellerReviewsCount; return this; }
            public Builder sellerIsOnline(Boolean sellerIsOnline) { this.sellerIsOnline = sellerIsOnline; return this; }
            public Builder gameId(Long gameId) { this.gameId = gameId; return this; }
            public Builder gameNameUa(String gameNameUa) { this.gameNameUa = gameNameUa; return this; }
            public Builder gameNameEn(String gameNameEn) { this.gameNameEn = gameNameEn; return this; }
            public Builder titleUa(String titleUa) { this.titleUa = titleUa; return this; }
            public Builder titleEn(String titleEn) { this.titleEn = titleEn; return this; }
            public Builder descriptionUa(String descriptionUa) { this.descriptionUa = descriptionUa; return this; }
            public Builder descriptionEn(String descriptionEn) { this.descriptionEn = descriptionEn; return this; }
            public Builder tradeType(String tradeType) { this.tradeType = tradeType; return this; }
            public Builder pricePerUnit(BigDecimal pricePerUnit) { this.pricePerUnit = pricePerUnit; return this; }
            public Builder minAmount(Integer minAmount) { this.minAmount = minAmount; return this; }
            public Builder availableAmount(Integer availableAmount) { this.availableAmount = availableAmount; return this; }
            public Builder serverName(String serverName) { this.serverName = serverName; return this; }
            public Builder sideName(String sideName) { this.sideName = sideName; return this; }
            public Builder createdAt(OffsetDateTime createdAt) { this.createdAt = createdAt; return this; }

            public LotResponse build() {
                return new LotResponse(id, sellerId, sellerUsername, sellerRating, sellerReviewsCount, sellerIsOnline, gameId, gameNameUa, gameNameEn, titleUa, titleEn, descriptionUa, descriptionEn, tradeType, pricePerUnit, minAmount, availableAmount, serverName, sideName, createdAt);
            }
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getSellerId() { return sellerId; }
        public void setSellerId(Long sellerId) { this.sellerId = sellerId; }
        public String getSellerUsername() { return sellerUsername; }
        public void setSellerUsername(String sellerUsername) { this.sellerUsername = sellerUsername; }
        public BigDecimal getSellerRating() { return sellerRating; }
        public void setSellerRating(BigDecimal sellerRating) { this.sellerRating = sellerRating; }
        public Integer getSellerReviewsCount() { return sellerReviewsCount; }
        public void setSellerReviewsCount(Integer sellerReviewsCount) { this.sellerReviewsCount = sellerReviewsCount; }
        public Boolean getSellerIsOnline() { return sellerIsOnline; }
        public void setSellerIsOnline(Boolean sellerIsOnline) { this.sellerIsOnline = sellerIsOnline; }
        public Long getGameId() { return gameId; }
        public void setGameId(Long gameId) { this.gameId = gameId; }
        public String getGameNameUa() { return gameNameUa; }
        public void setGameNameUa(String gameNameUa) { this.gameNameUa = gameNameUa; }
        public String getGameNameEn() { return gameNameEn; }
        public void setGameNameEn(String gameNameEn) { this.gameNameEn = gameNameEn; }
        public String getTitleUa() { return titleUa; }
        public void setTitleUa(String titleUa) { this.titleUa = titleUa; }
        public String getTitleEn() { return titleEn; }
        public void setTitleEn(String titleEn) { this.titleEn = titleEn; }
        public String getDescriptionUa() { return descriptionUa; }
        public void setDescriptionUa(String descriptionUa) { this.descriptionUa = descriptionUa; }
        public String getDescriptionEn() { return descriptionEn; }
        public void setDescriptionEn(String descriptionEn) { this.descriptionEn = descriptionEn; }
        public String getTradeType() { return tradeType; }
        public void setTradeType(String tradeType) { this.tradeType = tradeType; }
        public BigDecimal getPricePerUnit() { return pricePerUnit; }
        public void setPricePerUnit(BigDecimal pricePerUnit) { this.pricePerUnit = pricePerUnit; }
        public Integer getMinAmount() { return minAmount; }
        public void setMinAmount(Integer minAmount) { this.minAmount = minAmount; }
        public Integer getAvailableAmount() { return availableAmount; }
        public void setAvailableAmount(Integer availableAmount) { this.availableAmount = availableAmount; }
        public String getServerName() { return serverName; }
        public void setServerName(String serverName) { this.serverName = serverName; }
        public String getSideName() { return sideName; }
        public void setSideName(String sideName) { this.sideName = sideName; }
        public OffsetDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    }

    public static class CreateLotRequest {
        private Long gameId;
        private String titleUa;
        private String titleEn;
        private String descriptionUa;
        private String descriptionEn;
        private String tradeType;
        private BigDecimal pricePerUnit;
        private Integer minAmount;
        private Integer availableAmount;
        private String serverName;
        private String sideName;
        private String secretPayload;

        public CreateLotRequest() {}

        public Long getGameId() { return gameId; }
        public void setGameId(Long gameId) { this.gameId = gameId; }
        public String getTitleUa() { return titleUa; }
        public void setTitleUa(String titleUa) { this.titleUa = titleUa; }
        public String getTitleEn() { return titleEn; }
        public void setTitleEn(String titleEn) { this.titleEn = titleEn; }
        public String getDescriptionUa() { return descriptionUa; }
        public void setDescriptionUa(String descriptionUa) { this.descriptionUa = descriptionUa; }
        public String getDescriptionEn() { return descriptionEn; }
        public void setDescriptionEn(String descriptionEn) { this.descriptionEn = descriptionEn; }
        public String getTradeType() { return tradeType; }
        public void setTradeType(String tradeType) { this.tradeType = tradeType; }
        public BigDecimal getPricePerUnit() { return pricePerUnit; }
        public void setPricePerUnit(BigDecimal pricePerUnit) { this.pricePerUnit = pricePerUnit; }
        public Integer getMinAmount() { return minAmount; }
        public void setMinAmount(Integer minAmount) { this.minAmount = minAmount; }
        public Integer getAvailableAmount() { return availableAmount; }
        public void setAvailableAmount(Integer availableAmount) { this.availableAmount = availableAmount; }
        public String getServerName() { return serverName; }
        public void setServerName(String serverName) { this.serverName = serverName; }
        public String getSideName() { return sideName; }
        public void setSideName(String sideName) { this.sideName = sideName; }
        public String getSecretPayload() { return secretPayload; }
        public void setSecretPayload(String secretPayload) { this.secretPayload = secretPayload; }
    }
}
