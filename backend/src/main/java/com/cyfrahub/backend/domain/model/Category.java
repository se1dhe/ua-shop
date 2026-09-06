package com.cyfrahub.backend.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String code;

    @Column(name = "name_ua", nullable = false, length = 100)
    private String nameUa;

    @Column(name = "name_en", nullable = false, length = 100)
    private String nameEn;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private CategoryType type;

    @Column(length = 100)
    private String icon;

    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    public Category() {}

    public Category(Long id, String code, String nameUa, String nameEn, CategoryType type, String icon, Integer sortOrder) {
        this.id = id;
        this.code = code;
        this.nameUa = nameUa;
        this.nameEn = nameEn;
        this.type = type;
        this.icon = icon;
        this.sortOrder = sortOrder != null ? sortOrder : 0;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String code;
        private String nameUa;
        private String nameEn;
        private CategoryType type;
        private String icon;
        private Integer sortOrder = 0;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder code(String code) { this.code = code; return this; }
        public Builder nameUa(String nameUa) { this.nameUa = nameUa; return this; }
        public Builder nameEn(String nameEn) { this.nameEn = nameEn; return this; }
        public Builder type(CategoryType type) { this.type = type; return this; }
        public Builder icon(String icon) { this.icon = icon; return this; }
        public Builder sortOrder(Integer sortOrder) { this.sortOrder = sortOrder; return this; }

        public Category build() {
            return new Category(id, code, nameUa, nameEn, type, icon, sortOrder);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getNameUa() { return nameUa; }
    public void setNameUa(String nameUa) { this.nameUa = nameUa; }
    public String getNameEn() { return nameEn; }
    public void setNameEn(String nameEn) { this.nameEn = nameEn; }
    public CategoryType getType() { return type; }
    public void setType(CategoryType type) { this.type = type; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
}
