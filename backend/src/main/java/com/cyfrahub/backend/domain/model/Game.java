package com.cyfrahub.backend.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(unique = true, nullable = false, length = 100)
    private String code;

    @Column(name = "name_ua", nullable = false, length = 150)
    private String nameUa;

    @Column(name = "name_en", nullable = false, length = 150)
    private String nameEn;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    public Game() {}

    public Game(Long id, Category category, String code, String nameUa, String nameEn, String imageUrl, Boolean isActive, Integer sortOrder) {
        this.id = id;
        this.category = category;
        this.code = code;
        this.nameUa = nameUa;
        this.nameEn = nameEn;
        this.imageUrl = imageUrl;
        this.isActive = isActive != null ? isActive : true;
        this.sortOrder = sortOrder != null ? sortOrder : 0;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Category category;
        private String code;
        private String nameUa;
        private String nameEn;
        private String imageUrl;
        private Boolean isActive = true;
        private Integer sortOrder = 0;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder category(Category category) { this.category = category; return this; }
        public Builder code(String code) { this.code = code; return this; }
        public Builder nameUa(String nameUa) { this.nameUa = nameUa; return this; }
        public Builder nameEn(String nameEn) { this.nameEn = nameEn; return this; }
        public Builder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }
        public Builder isActive(Boolean isActive) { this.isActive = isActive; return this; }
        public Builder sortOrder(Integer sortOrder) { this.sortOrder = sortOrder; return this; }

        public Game build() {
            return new Game(id, category, code, nameUa, nameEn, imageUrl, isActive, sortOrder);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getNameUa() { return nameUa; }
    public void setNameUa(String nameUa) { this.nameUa = nameUa; }
    public String getNameEn() { return nameEn; }
    public void setNameEn(String nameEn) { this.nameEn = nameEn; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
}
