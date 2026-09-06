package com.cyfrahub.backend.domain.model;

public enum TradeType {
    MANUAL_P2P,  // Escrow с подтверждением вручную (валюта, скины, бустинг)
    INSTANT_AUTO // Автоматическая мгновенная выдача ключа/файла из secret_payload
}
