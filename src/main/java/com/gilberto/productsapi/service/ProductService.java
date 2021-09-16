package com.gilberto.productsapi.service;

import com.gilberto.productsapi.dto.request.ProductDTO;
import com.gilberto.productsapi.dto.response.MessageResponseDTO;
import com.gilberto.productsapi.entity.Product;
import com.gilberto.productsapi.exception.ProductNotFoundException;
import com.gilberto.productsapi.mapper.ProductMapper;
import com.gilberto.productsapi.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ProductService {

    private ProductRepository productRepository;

    private final ProductMapper productMapper = ProductMapper.INSTANCE;

    public MessageResponseDTO createProduct(ProductDTO productDTO){
        Product productToSave = productMapper.toModel(productDTO);

        Product savedProduct = productRepository.save(productToSave);
        return createMessageResponse(savedProduct.getId(), "Created product with ID " );
    }

    public List<ProductDTO> listAll() {
        List<Product> allProduct = productRepository.findAll();
        return allProduct.stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO findById(Long id) throws ProductNotFoundException {
        Product product = verifyIfExists(id);

        return productMapper.toDTO(product);
    }

    public void delete(Long id) throws ProductNotFoundException {
        verifyIfExists(id);
        productRepository.deleteById(id);
    }

    public MessageResponseDTO updateById(Long id, ProductDTO productDTO) throws ProductNotFoundException {
        verifyIfExists(id);

        Product productToUpdate = productMapper.toModel(productDTO);

        Product updatedProduct = productRepository.save(productToUpdate);
        return createMessageResponse(updatedProduct.getId(), "Updated product with ID ");
    }

    private Product verifyIfExists(Long id) throws ProductNotFoundException {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    private MessageResponseDTO createMessageResponse(Long id, String message) {
        return MessageResponseDTO
                .builder()
                .message(message + id)
                .build();
    }


}
