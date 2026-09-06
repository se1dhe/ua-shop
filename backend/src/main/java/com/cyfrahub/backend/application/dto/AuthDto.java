package com.cyfrahub.backend.application.dto;

public class AuthDto {

    public static class LoginRequest {
        private String email;
        private String password;

        public LoginRequest() {}
        public LoginRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class RegisterRequest {
        private String email;
        private String password;
        private String username;
        private String telegramUsername;

        public RegisterRequest() {}
        public RegisterRequest(String email, String password, String username, String telegramUsername) {
            this.email = email;
            this.password = password;
            this.username = username;
            this.telegramUsername = telegramUsername;
        }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getTelegramUsername() { return telegramUsername; }
        public void setTelegramUsername(String telegramUsername) { this.telegramUsername = telegramUsername; }
    }

    public static class AuthResponse {
        private String token;
        private Long userId;
        private String username;
        private String email;
        private String role;
        private String telegramUsername;

        public AuthResponse() {}
        public AuthResponse(String token, Long userId, String username, String email, String role, String telegramUsername) {
            this.token = token;
            this.userId = userId;
            this.username = username;
            this.email = email;
            this.role = role;
            this.telegramUsername = telegramUsername;
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private String token;
            private Long userId;
            private String username;
            private String email;
            private String role;
            private String telegramUsername;

            public Builder token(String token) { this.token = token; return this; }
            public Builder userId(Long userId) { this.userId = userId; return this; }
            public Builder username(String username) { this.username = username; return this; }
            public Builder email(String email) { this.email = email; return this; }
            public Builder role(String role) { this.role = role; return this; }
            public Builder telegramUsername(String telegramUsername) { this.telegramUsername = telegramUsername; return this; }

            public AuthResponse build() {
                return new AuthResponse(token, userId, username, email, role, telegramUsername);
            }
        }

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public String getTelegramUsername() { return telegramUsername; }
        public void setTelegramUsername(String telegramUsername) { this.telegramUsername = telegramUsername; }
    }
}
