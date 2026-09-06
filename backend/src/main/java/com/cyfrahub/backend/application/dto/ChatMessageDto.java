package com.cyfrahub.backend.application.dto;

import java.time.OffsetDateTime;

public class ChatMessageDto {

    public static class SendMessageRequest {
        private String message;

        public SendMessageRequest() {}
        public SendMessageRequest(String message) { this.message = message; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    public static class ChatMessageResponse {
        private Long id;
        private Long orderId;
        private Long senderId;
        private String senderUsername;
        private String message;
        private Boolean isSystem;
        private OffsetDateTime createdAt;

        public ChatMessageResponse() {}
        public ChatMessageResponse(Long id, Long orderId, Long senderId, String senderUsername, String message, Boolean isSystem, OffsetDateTime createdAt) {
            this.id = id;
            this.orderId = orderId;
            this.senderId = senderId;
            this.senderUsername = senderUsername;
            this.message = message;
            this.isSystem = isSystem != null ? isSystem : false;
            this.createdAt = createdAt;
        }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private Long id;
            private Long orderId;
            private Long senderId;
            private String senderUsername;
            private String message;
            private Boolean isSystem = false;
            private OffsetDateTime createdAt;

            public Builder id(Long id) { this.id = id; return this; }
            public Builder orderId(Long orderId) { this.orderId = orderId; return this; }
            public Builder senderId(Long senderId) { this.senderId = senderId; return this; }
            public Builder senderUsername(String senderUsername) { this.senderUsername = senderUsername; return this; }
            public Builder message(String message) { this.message = message; return this; }
            public Builder isSystem(Boolean isSystem) { this.isSystem = isSystem; return this; }
            public Builder createdAt(OffsetDateTime createdAt) { this.createdAt = createdAt; return this; }

            public ChatMessageResponse build() {
                return new ChatMessageResponse(id, orderId, senderId, senderUsername, message, isSystem, createdAt);
            }
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getOrderId() { return orderId; }
        public void setOrderId(Long orderId) { this.orderId = orderId; }
        public Long getSenderId() { return senderId; }
        public void setSenderId(Long senderId) { this.senderId = senderId; }
        public String getSenderUsername() { return senderUsername; }
        public void setSenderUsername(String senderUsername) { this.senderUsername = senderUsername; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public Boolean getIsSystem() { return isSystem; }
        public void setIsSystem(Boolean isSystem) { this.isSystem = isSystem; }
        public OffsetDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    }
}
