package com.gilberto.logistockapi.services;

import com.gilberto.logistockapi.models.dto.request.ProductForm;
import com.gilberto.logistockapi.models.dto.response.MessageResponseDTO;
import com.gilberto.logistockapi.models.entity.Product;
import com.gilberto.logistockapi.exceptions.ProductAlreadyRegisteredException;
import com.gilberto.logistockapi.exceptions.ProductNotFoundException;
import com.gilberto.logistockapi.exceptions.ProductStockExceededException;
import com.gilberto.logistockapi.exceptions.ProductStockUnderThanZeroException;
import com.gilberto.logistockapi.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    
    public ProductService(@Autowired ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public MessageResponseDTO createProduct(ProductForm productDTO) throws ProductAlreadyRegisteredException{

        verifyIfIsAlreadyRegistered(productDTO.getName());
        Product productToSave = new Product(productDTO);

        Product savedProduct = productRepository.save(productToSave);
        return createMessageResponse(savedProduct.getId(), "Created product with ID " );


    }

    public List<ProductForm> listAll() {
        List<Product> allProduct = productRepository.findAll();
        return allProduct.stream()
                .map(ProductForm::new)
                .collect(Collectors.toList());
    }

    public ProductForm findByName(String name) throws ProductNotFoundException {
        Product product = productRepository.findByName(name)
                .orElseThrow(() -> new ProductNotFoundException(name));

        return new ProductForm(product);
    }

    public MessageResponseDTO delete(Long id) throws ProductNotFoundException {
        verifyIfExists(id);
        productRepository.deleteById(id);
        return createMessageResponse(id, "Deleted product with ID ");
    }

    public MessageResponseDTO updateById(Long id, ProductForm productDTO) throws ProductNotFoundException {
        verifyIfExists(id);

        Product productToUpdate = new Product(productDTO);

        Product updatedProduct = productRepository.save(productToUpdate);
        return createMessageResponse(updatedProduct.getId(), "Updated product with ID ");
    }

    public ProductForm increment(Long id, int quantityToIncrement) throws ProductNotFoundException, ProductStockExceededException {
        Product productToIncrementStock = verifyIfExists(id);
        int quantityAfterIncrement = quantityToIncrement + productToIncrementStock.getStockQuantity();
        if (quantityAfterIncrement <= productToIncrementStock.getMaxStockLevel()) {
            productToIncrementStock.setStockQuantity(quantityAfterIncrement);
            Product incrementedProductStock = productRepository.save(productToIncrementStock);
            return new ProductForm(incrementedProductStock);
        }
        throw new ProductStockExceededException(id, quantityToIncrement);
    }

    public ProductForm decrement(Long id, int quantityToDecrement) throws ProductNotFoundException, ProductStockUnderThanZeroException {
        Product productToDecrementStock = verifyIfExists(id);
        int quantityAfterDecrement = productToDecrementStock.getStockQuantity()-quantityToDecrement;
        if(quantityAfterDecrement>=0){
            productToDecrementStock.setStockQuantity(quantityAfterDecrement);
            Product decrementedProductStock = productRepository.save(productToDecrementStock);
            return new ProductForm(decrementedProductStock);
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
