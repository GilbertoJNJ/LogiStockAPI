package com.gilberto.storeapi.service;

import com.gilberto.storeapi.dto.request.ProductDTO;
import com.gilberto.storeapi.dto.response.MessageResponseDTO;
import com.gilberto.storeapi.entity.Product;
import com.gilberto.storeapi.exception.ProductNotFoundException;
import com.gilberto.storeapi.mapper.ProductMapper;
import com.gilberto.storeapi.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ProductService {

    private ProductRepository productRepository;

    //private final ProductMapper productMapper = ProductMapper.INSTANCE;

    public MessageResponseDTO createProduct(Product product){
        //Product productToSave = productMapper.toModel(productDTO);

        Product savedProduct = productRepository.save(product);
        return createMessageResponse(savedProduct.getId(), "Created product with ID " );
    }

    public List<Product> listAll() {
        List<Product> allProduct = productRepository.findAll();
        /*return allProduct.stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());

         */
        return allProduct;
    }

    public Product findById(Long id) throws ProductNotFoundException {
        Product product = verifyIfExists(id);

        return product;
    }

    public void delete(Long id) throws ProductNotFoundException {
        verifyIfExists(id);
        productRepository.deleteById(id);
    }

    public MessageResponseDTO updateById(Long id, Product product) throws ProductNotFoundException {
        verifyIfExists(id);

        //Product productToUpdate = productMapper.toModel(productDTO);

        Product updatedProduct = productRepository.save(product);
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
