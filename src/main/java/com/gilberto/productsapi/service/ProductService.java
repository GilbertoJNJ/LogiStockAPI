package com.gilberto.productsapi.service;

import com.gilberto.productsapi.dto.request.ProductDTO;
import com.gilberto.productsapi.dto.response.MessageResponseDTO;
import com.gilberto.productsapi.entity.Product;
import com.gilberto.productsapi.exception.ProductAlreadyRegisteredException;
import com.gilberto.productsapi.exception.ProductNotFoundException;
import com.gilberto.productsapi.exception.ProductStockExceededException;
import com.gilberto.productsapi.exception.ProductStockUnderThanZeroException;
import com.gilberto.productsapi.mapper.ProductMapper;
import com.gilberto.productsapi.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ProductService {

    private ProductRepository productRepository;

    private final ProductMapper productMapper = ProductMapper.INSTANCE;

    public MessageResponseDTO createProduct(ProductDTO productDTO) throws ProductAlreadyRegisteredException{

        verifyIfIsAlreadyRegistered(productDTO.getName());
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

    public ProductDTO findByName(String name) throws ProductNotFoundException {
        Product product = productRepository.findByName(name)
                .orElseThrow(() -> new ProductNotFoundException(name));

        return productMapper.toDTO(product);
    }

    public MessageResponseDTO delete(Long id) throws ProductNotFoundException {
        verifyIfExists(id);
        productRepository.deleteById(id);
        return createMessageResponse(id, "Deleted product with ID ");
    }

    public MessageResponseDTO updateById(Long id, ProductDTO productDTO) throws ProductNotFoundException {
        verifyIfExists(id);

        Product productToUpdate = productMapper.toModel(productDTO);

        Product updatedProduct = productRepository.save(productToUpdate);
        return createMessageResponse(updatedProduct.getId(), "Updated product with ID ");
    }

    public ProductDTO increment(Long id, int quantityToIncrement) throws ProductNotFoundException, ProductStockExceededException {
        Product productToIncrementStock = verifyIfExists(id);
        int quantityAfterIncrement = quantityToIncrement + productToIncrementStock.getQuantity();
        if (quantityAfterIncrement <= productToIncrementStock.getMaxQuantity()) {
            productToIncrementStock.setQuantity(quantityAfterIncrement);
            Product incrementedProductStock = productRepository.save(productToIncrementStock);
            return productMapper.toDTO(incrementedProductStock);
        }
        throw new ProductStockExceededException(id, quantityToIncrement);
    }

    public ProductDTO decrement(Long id, int quantityToDecrement) throws ProductNotFoundException, ProductStockUnderThanZeroException {
        Product productToDecrementStock = verifyIfExists(id);
        int quantityAfterDecrement = productToDecrementStock.getQuantity()-quantityToDecrement;
        if(quantityAfterDecrement>=0){
            productToDecrementStock.setQuantity(quantityAfterDecrement);
            Product decrementedProductStock = productRepository.save(productToDecrementStock);
            return productMapper.toDTO(decrementedProductStock);
        }
        throw new ProductStockUnderThanZeroException(id, quantityToDecrement);
    }

    private Product verifyIfExists(Long id) throws ProductNotFoundException {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
    private void verifyIfIsAlreadyRegistered(String name) throws ProductAlreadyRegisteredException {
        Optional<Product> optSavedProduct = productRepository.findByName(name);
        if (optSavedProduct.isPresent()) {
            throw new ProductAlreadyRegisteredException(name);
        }
    }

    private MessageResponseDTO createMessageResponse(Long id, String message) {
        return MessageResponseDTO
                .builder()
                .message(message + id)
                .build();
    }

}
