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
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    
    public ProductService(@Autowired ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public MessageResponseDTO createProduct(ProductForm productForm) throws ProductAlreadyRegisteredException{

        verifyIfIsAlreadyRegistered(productForm.getBarCode());
        
        var productToSave = new Product(productForm);
        var savedProduct = this.productRepository.save(productToSave);
        return createMessageResponse(savedProduct.getId(), "Created product with ID " );
    }
    
    @Override
    public List<ProductForm> listAll() {
        List<Product> allProduct = productRepository.findAll();
        return allProduct.stream()
                .map(ProductForm::new)
                .collect(Collectors.toList());
    }
    
    @Override
    public ProductForm findByBarCode(String barCode) throws ProductNotFoundException {
        return productRepository.findByBarCode(barCode)
            .map(ProductForm::new)
            .orElseThrow(() -> new ProductNotFoundException(barCode));
    }
    
    @Override
    public MessageResponseDTO delete(Long id) throws ProductNotFoundException {
        this.verifyIfExists(id);
        this.productRepository.deleteById(id);
        return createMessageResponse(id, "Deleted product with ID ");
    }
    
    @Override
    public MessageResponseDTO updateById(Long id, ProductForm productForm) throws ProductNotFoundException {
        var productToUpdate = this.verifyIfExists(id);
        productToUpdate.setName(productForm.getName());
        productToUpdate.setCategory(productForm.getCategory());
        productToUpdate.setUnitPrice(productForm.getUnitPrice());
        productToUpdate.setMeasureUnit(productForm.getMeasureUnit());
        productToUpdate.setStockQuantity(productForm.getStockQuantity());
        productToUpdate.setMinStockLevel(productForm.getMinStockLevel());
        productToUpdate.setMaxStockLevel(productForm.getMaxStockLevel());
        productToUpdate.setDescription(productForm.getDescription());

        var updatedProduct = productRepository.save(productToUpdate);
        return createMessageResponse(updatedProduct.getId(), "Updated product with ID ");
    }
    
    @Override
    public ProductForm increment(Long id, Integer quantityToIncrement) throws ProductNotFoundException, ProductStockExceededException {
        var productToIncrementStock = verifyIfExists(id);
        var updatedQuantity = sumQuantity(productToIncrementStock.getStockQuantity(),
            quantityToIncrement);
        
        if (updatedQuantity > productToIncrementStock.getMaxStockLevel()) {
            throw new ProductStockExceededException(id, quantityToIncrement);
        }
        
        productToIncrementStock.setStockQuantity(updatedQuantity);
        var incrementedProductStock = productRepository.save(productToIncrementStock);
        return new ProductForm(incrementedProductStock);
    }
    
    @Override
    public ProductForm decrement(Long id, Integer quantityToDecrement) throws ProductNotFoundException, ProductStockUnderThanZeroException {
        var productToDecrementStock = verifyIfExists(id);
        var quantityAfterDecrement = Math.max(0,
            subtractQuantity(productToDecrementStock.getStockQuantity(), quantityToDecrement));
        
        if (quantityAfterDecrement == 0) {
            throw new ProductStockUnderThanZeroException(id, quantityToDecrement);
        }
        
        productToDecrementStock.setStockQuantity(quantityAfterDecrement);
        var decrementedProductStock = productRepository.save(productToDecrementStock);
        return new ProductForm(decrementedProductStock);
    }
    
    private Integer subtractQuantity(Integer stockQuantity, Integer quantityToDecrement) {
        return stockQuantity - quantityToDecrement;
    }
    
    private Integer sumQuantity(Integer stockQuantity, Integer quantityToIncrement) {
        return stockQuantity + quantityToIncrement;
    }

    private Product verifyIfExists(Long id) throws ProductNotFoundException {
        return this.productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
    private void verifyIfIsAlreadyRegistered(String barCode) throws ProductAlreadyRegisteredException {
        Optional<Product> optSavedProduct = this.productRepository.findByBarCode(barCode);
        if (optSavedProduct.isPresent()) {
            throw new ProductAlreadyRegisteredException(barCode);
        }
    }

    private MessageResponseDTO createMessageResponse(Long id, String message) {
        return MessageResponseDTO
                .builder()
                .message(message + id)
                .build();
    }

}
