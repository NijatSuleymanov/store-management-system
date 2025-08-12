package com.codewithmosh.store.orders;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDto {
    public Long id;
    public String name;
    public BigDecimal price;
}
