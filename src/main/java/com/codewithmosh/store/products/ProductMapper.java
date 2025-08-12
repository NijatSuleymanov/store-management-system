package com.codewithmosh.store.products;

import com.codewithmosh.store.dtos.CreateProductRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "categoryId", source = "category.id")
    ProductDto toDto(Product product);
    Product toEntity(CreateProductRequest request);
    @Mapping(target = "id",ignore = true)
    void update(ProductDto productDto, @MappingTarget Product product);
}
