package com.cyfrahub.backend.application.dto;

import java.util.List;

public class GameDto {

    public static class CategoryResponse {
        private Long id;
        private String code;
        private String nameUa;
        private String nameEn;
        private String type;
        private String icon;
        private List<GameResponse> games;

        public CategoryResponse() {}
        public CategoryResponse(Long id, String code, String nameUa, String nameEn, String type, String icon, List<GameResponse> games) {
            this.id = id;
            this.code = code;
            this.nameUa = nameUa;
            this.nameEn = nameEn;
            this.type = type;
            this.icon = icon;
            this.games = games;
        }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private Long id;
            private String code;
            private String nameUa;
            private String nameEn;
            private String type;
            private String icon;
            private List<GameResponse> games;

            public Builder id(Long id) { this.id = id; return this; }
            public Builder code(String code) { this.code = code; return this; }
            public Builder nameUa(String nameUa) { this.nameUa = nameUa; return this; }
            public Builder nameEn(String nameEn) { this.nameEn = nameEn; return this; }
            public Builder type(String type) { this.type = type; return this; }
            public Builder icon(String icon) { this.icon = icon; return this; }
            public Builder games(List<GameResponse> games) { this.games = games; return this; }

            public CategoryResponse build() {
                return new CategoryResponse(id, code, nameUa, nameEn, type, icon, games);
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
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getIcon() { return icon; }
        public void setIcon(String icon) { this.icon = icon; }
        public List<GameResponse> getGames() { return games; }
        public void setGames(List<GameResponse> games) { this.games = games; }
    }

    public static class GameResponse {
        private Long id;
        private Long categoryId;
        private String code;
        private String nameUa;
        private String nameEn;
        private String imageUrl;

        public GameResponse() {}
        public GameResponse(Long id, Long categoryId, String code, String nameUa, String nameEn, String imageUrl) {
            this.id = id;
            this.categoryId = categoryId;
            this.code = code;
            this.nameUa = nameUa;
            this.nameEn = nameEn;
            this.imageUrl = imageUrl;
        }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private Long id;
            private Long categoryId;
            private String code;
            private String nameUa;
            private String nameEn;
            private String imageUrl;

            public Builder id(Long id) { this.id = id; return this; }
            public Builder categoryId(Long categoryId) { this.categoryId = categoryId; return this; }
            public Builder code(String code) { this.code = code; return this; }
            public Builder nameUa(String nameUa) { this.nameUa = nameUa; return this; }
            public Builder nameEn(String nameEn) { this.nameEn = nameEn; return this; }
            public Builder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }

            public GameResponse build() {
                return new GameResponse(id, categoryId, code, nameUa, nameEn, imageUrl);
            }
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getCategoryId() { return categoryId; }
        public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public String getNameUa() { return nameUa; }
        public void setNameUa(String nameUa) { this.nameUa = nameUa; }
        public String getNameEn() { return nameEn; }
        public void setNameEn(String nameEn) { this.nameEn = nameEn; }
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    }
}
