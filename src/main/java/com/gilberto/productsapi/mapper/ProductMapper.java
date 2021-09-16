package com.gilberto.productsapi.mapper;

import com.gilberto.productsapi.dto.request.ProductDTO;
import com.gilberto.productsapi.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    Product toModel(ProductDTO productDTO);

    ProductDTO toDTO(Product product);

}
