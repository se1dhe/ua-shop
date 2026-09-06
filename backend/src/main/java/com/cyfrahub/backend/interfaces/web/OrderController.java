package com.cyfrahub.backend.interfaces.web;

import com.cyfrahub.backend.application.dto.OrderDto;
import com.cyfrahub.backend.application.service.EscrowOrderService;
import com.cyfrahub.backend.domain.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final EscrowOrderService orderService;

    public OrderController(EscrowOrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderDto.OrderResponse> createOrder(
            @AuthenticationPrincipal User user,
            @RequestBody OrderDto.CreateOrderRequest request) {
        return ResponseEntity.ok(orderService.createAndPayOrder(user.getId(), request));
    }

    @GetMapping("/buyer")
    public ResponseEntity<List<OrderDto.OrderResponse>> getBuyerOrders(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(orderService.getBuyerOrders(user.getId()));
    }

    @GetMapping("/seller")
    public ResponseEntity<List<OrderDto.OrderResponse>> getSellerOrders(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(orderService.getSellerOrders(user.getId()));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto.OrderResponse> getOrderById(
            @AuthenticationPrincipal User user,
            @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(user.getId(), orderId));
    }

    @PostMapping("/{orderId}/transfer")
    public ResponseEntity<OrderDto.OrderResponse> markTransferred(
            @AuthenticationPrincipal User user,
            @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.markTransferred(user.getId(), orderId));
    }

    @PostMapping("/{orderId}/confirm")
    public ResponseEntity<OrderDto.OrderResponse> confirmCompletion(
            @AuthenticationPrincipal User user,
            @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.confirmCompletion(user.getId(), orderId));
    }

    @PostMapping("/{orderId}/dispute")
    public ResponseEntity<OrderDto.OrderResponse> openDispute(
            @AuthenticationPrincipal User user,
            @PathVariable Long orderId,
            @RequestBody Map<String, String> payload) {
        String reason = payload.getOrDefault("reason", "Не відповідність товару або проблеми з передачею");
        return ResponseEntity.ok(orderService.openDispute(user.getId(), orderId, reason));
    }
}
