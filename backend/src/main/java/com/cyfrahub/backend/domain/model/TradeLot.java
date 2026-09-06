package com.cyfrahub.backend.domain.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "trade_lots")
public class TradeLot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Column(name = "title_ua", nullable = false)
    private String titleUa;

    @Column(name = "title_en", nullable = false)
    private String titleEn;

    @Column(name = "description_ua", columnDefinition = "TEXT")
    private String descriptionUa;

    @Column(name = "description_en", columnDefinition = "TEXT")
    private String descriptionEn;

    @Enumerated(EnumType.STRING)
    @Column(name = "trade_type", nullable = false, length = 50)
    private TradeType tradeType = TradeType.MANUAL_P2P;

    @Column(name = "price_per_unit", nullable = false, precision = 12, scale = 2)
    private BigDecimal pricePerUnit;

    @Column(name = "min_amount", nullable = false)
    private Integer minAmount = 1;

    @Column(name = "available_amount", nullable = false)
    private Integer availableAmount = 1;

    @Column(name = "server_name", length = 100)
    private String serverName;

    @Column(name = "side_name", length = 100)
    private String sideName;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    public TradeLot() {}

    public TradeLot(Long id, User seller, Game game, String titleUa, String titleEn, String descriptionUa, String descriptionEn, TradeType tradeType, BigDecimal pricePerUnit, Integer minAmount, Integer availableAmount, String serverName, String sideName, Boolean isActive, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.id = id;
        this.seller = seller;
        this.game = game;
        this.titleUa = titleUa;
        this.titleEn = titleEn;
        this.descriptionUa = descriptionUa;
        this.descriptionEn = descriptionEn;
        this.tradeType = tradeType != null ? tradeType : TradeType.MANUAL_P2P;
        this.pricePerUnit = pricePerUnit;
        this.minAmount = minAmount != null ? minAmount : 1;
        this.availableAmount = availableAmount != null ? availableAmount : 1;
        this.serverName = serverName;
        this.sideName = sideName;
        this.isActive = isActive != null ? isActive : true;
        this.createdAt = createdAt != null ? createdAt : OffsetDateTime.now();
        this.updatedAt = updatedAt != null ? updatedAt : OffsetDateTime.now();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private User seller;
        private Game game;
        private String titleUa;
        private String titleEn;
        private String descriptionUa;
        private String descriptionEn;
        private TradeType tradeType = TradeType.MANUAL_P2P;
        private BigDecimal pricePerUnit;
        private Integer minAmount = 1;
        private Integer availableAmount = 1;
        private String serverName;
        private String sideName;
        private Boolean isActive = true;
        private OffsetDateTime createdAt = OffsetDateTime.now();
        private OffsetDateTime updatedAt = OffsetDateTime.now();

        public Builder id(Long id) { this.id = id; return this; }
        public Builder seller(User seller) { this.seller = seller; return this; }
        public Builder game(Game game) { this.game = game; return this; }
        public Builder titleUa(String titleUa) { this.titleUa = titleUa; return this; }
        public Builder titleEn(String titleEn) { this.titleEn = titleEn; return this; }
        public Builder descriptionUa(String descriptionUa) { this.descriptionUa = descriptionUa; return this; }
        public Builder descriptionEn(String descriptionEn) { this.descriptionEn = descriptionEn; return this; }
        public Builder tradeType(TradeType tradeType) { this.tradeType = tradeType; return this; }
        public Builder pricePerUnit(BigDecimal pricePerUnit) { this.pricePerUnit = pricePerUnit; return this; }
        public Builder minAmount(Integer minAmount) { this.minAmount = minAmount; return this; }
        public Builder availableAmount(Integer availableAmount) { this.availableAmount = availableAmount; return this; }
        public Builder serverName(String serverName) { this.serverName = serverName; return this; }
        public Builder sideName(String sideName) { this.sideName = sideName; return this; }
        public Builder isActive(Boolean isActive) { this.isActive = isActive; return this; }
        public Builder createdAt(OffsetDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public TradeLot build() {
            return new TradeLot(id, seller, game, titleUa, titleEn, descriptionUa, descriptionEn, tradeType, pricePerUnit, minAmount, availableAmount, serverName, sideName, isActive, createdAt, updatedAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getSeller() { return seller; }
    public void setSeller(User seller) { this.seller = seller; }
    public Game getGame() { return game; }
    public void setGame(Game game) { this.game = game; }
    public String getTitleUa() { return titleUa; }
    public void setTitleUa(String titleUa) { this.titleUa = titleUa; }
    public String getTitleEn() { return titleEn; }
    public void setTitleEn(String titleEn) { this.titleEn = titleEn; }
    public String getDescriptionUa() { return descriptionUa; }
    public void setDescriptionUa(String descriptionUa) { this.descriptionUa = descriptionUa; }
    public String getDescriptionEn() { return descriptionEn; }
    public void setDescriptionEn(String descriptionEn) { this.descriptionEn = descriptionEn; }
    public TradeType getTradeType() { return tradeType; }
    public void setTradeType(TradeType tradeType) { this.tradeType = tradeType; }
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
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}
