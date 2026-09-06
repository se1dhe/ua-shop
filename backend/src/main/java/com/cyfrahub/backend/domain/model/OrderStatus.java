package com.cyfrahub.backend.domain.model;

public enum OrderStatus {
    CREATED,                // Заказ создан, ожидание оплаты
    PAID_HELD,              // Средства заморожены (Escrow Hold), продавец передает товар
    TRANSFERRED_BY_SELLER,  // Продавец нажал "Товар передан", покупатель проверяет
    COMPLETED,              // Покупатель подтвердил получение, средства зачислены продавцу
    DISPUTED,               // Открыт спор, арбитраж администратора
    CANCELLED,              // Заказ отменен до оплаты
    REFUNDED                // Средства возвращены покупателю по решению арбитража
}
