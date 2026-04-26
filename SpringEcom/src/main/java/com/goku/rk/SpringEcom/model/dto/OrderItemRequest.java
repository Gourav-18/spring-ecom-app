package com.goku.rk.SpringEcom.model.dto;

public record OrderItemRequest(
        int productId,
        int quantity
) {}
