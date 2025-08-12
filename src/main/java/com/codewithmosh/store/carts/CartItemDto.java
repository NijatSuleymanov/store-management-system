package com.codewithmosh.store.carts;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemDto {

    public ProductDto product;
    public int quantity;
    public BigDecimal totalPrice;

}
