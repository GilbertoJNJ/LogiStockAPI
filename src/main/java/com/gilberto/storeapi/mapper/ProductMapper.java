package com.gilberto.storeapi.mapper;

import com.gilberto.storeapi.dto.request.ProductDTO;
import com.gilberto.storeapi.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    Product toModel(ProductDTO productDTO);

    ProductDTO toDTO(Product product);

}
