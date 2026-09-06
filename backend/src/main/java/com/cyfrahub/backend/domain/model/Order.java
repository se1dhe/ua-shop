package com.cyfrahub.backend.domain.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number", unique = true, nullable = false, length = 64)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    private User buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lot_id", nullable = false)
    private TradeLot lot;

    @Column(nullable = false)
    private Integer amount = 1;

    @Column(name = "total_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "platform_fee", nullable = false, precision = 12, scale = 2)
    private BigDecimal platformFee;

    @Column(name = "seller_net_earnings", nullable = false, precision = 12, scale = 2)
    private BigDecimal sellerNetEarnings;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private OrderStatus status = OrderStatus.CREATED;

    @Column(name = "secret_payload", columnDefinition = "TEXT")
    private String secretPayload;

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    public Order() {}

    public Order(Long id, String orderNumber, User buyer, User seller, TradeLot lot, Integer amount, BigDecimal totalPrice, BigDecimal platformFee, BigDecimal sellerNetEarnings, OrderStatus status, String secretPayload, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.buyer = buyer;
        this.seller = seller;
        this.lot = lot;
        this.amount = amount != null ? amount : 1;
        this.totalPrice = totalPrice;
        this.platformFee = platformFee;
        this.sellerNetEarnings = sellerNetEarnings;
        this.status = status != null ? status : OrderStatus.CREATED;
        this.secretPayload = secretPayload;
        this.createdAt = createdAt != null ? createdAt : OffsetDateTime.now();
        this.updatedAt = updatedAt != null ? updatedAt : OffsetDateTime.now();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String orderNumber;
        private User buyer;
        private User seller;
        private TradeLot lot;
        private Integer amount = 1;
        private BigDecimal totalPrice;
        private BigDecimal platformFee;
        private BigDecimal sellerNetEarnings;
        private OrderStatus status = OrderStatus.CREATED;
        private String secretPayload;
        private OffsetDateTime createdAt = OffsetDateTime.now();
        private OffsetDateTime updatedAt = OffsetDateTime.now();

        public Builder id(Long id) { this.id = id; return this; }
        public Builder orderNumber(String orderNumber) { this.orderNumber = orderNumber; return this; }
        public Builder buyer(User buyer) { this.buyer = buyer; return this; }
        public Builder seller(User seller) { this.seller = seller; return this; }
        public Builder lot(TradeLot lot) { this.lot = lot; return this; }
        public Builder amount(Integer amount) { this.amount = amount; return this; }
        public Builder totalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; return this; }
        public Builder platformFee(BigDecimal platformFee) { this.platformFee = platformFee; return this; }
        public Builder sellerNetEarnings(BigDecimal sellerNetEarnings) { this.sellerNetEarnings = sellerNetEarnings; return this; }
        public Builder status(OrderStatus status) { this.status = status; return this; }
        public Builder secretPayload(String secretPayload) { this.secretPayload = secretPayload; return this; }
        public Builder createdAt(OffsetDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Order build() {
            return new Order(id, orderNumber, buyer, seller, lot, amount, totalPrice, platformFee, sellerNetEarnings, status, secretPayload, createdAt, updatedAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public User getBuyer() { return buyer; }
    public void setBuyer(User buyer) { this.buyer = buyer; }
    public User getSeller() { return seller; }
    public void setSeller(User seller) { this.seller = seller; }
    public TradeLot getLot() { return lot; }
    public void setLot(TradeLot lot) { this.lot = lot; }
    public Integer getAmount() { return amount; }
    public void setAmount(Integer amount) { this.amount = amount; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
    public BigDecimal getPlatformFee() { return platformFee; }
    public void setPlatformFee(BigDecimal platformFee) { this.platformFee = platformFee; }
    public BigDecimal getSellerNetEarnings() { return sellerNetEarnings; }
    public void setSellerNetEarnings(BigDecimal sellerNetEarnings) { this.sellerNetEarnings = sellerNetEarnings; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public String getSecretPayload() { return secretPayload; }
    public void setSecretPayload(String secretPayload) { this.secretPayload = secretPayload; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}
