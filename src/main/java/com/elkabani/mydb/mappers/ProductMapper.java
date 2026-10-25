package com.elkabani.mydb.mappers;

import com.elkabani.mydb.dtos.ProductDto;
import com.elkabani.mydb.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "category.id", target = "categoryId")
    ProductDto toDto(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    Product toEntity(ProductDto productDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    void update(ProductDto productDto, @MappingTarget Product product);
}