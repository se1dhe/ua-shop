package com.cyfrahub.backend.interfaces.web;

import com.cyfrahub.backend.application.dto.ChatMessageDto;
import com.cyfrahub.backend.application.service.ChatService;
import com.cyfrahub.backend.domain.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders/{orderId}/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping
    public ResponseEntity<List<ChatMessageDto.ChatMessageResponse>> getMessages(
            @AuthenticationPrincipal User user,
            @PathVariable Long orderId) {
        return ResponseEntity.ok(chatService.getOrderMessages(user.getId(), orderId));
    }

    @PostMapping
    public ResponseEntity<ChatMessageDto.ChatMessageResponse> sendMessage(
            @AuthenticationPrincipal User user,
            @PathVariable Long orderId,
            @RequestBody ChatMessageDto.SendMessageRequest request) {
        return ResponseEntity.ok(chatService.sendMessage(user.getId(), orderId, request.getMessage()));
    }
}
