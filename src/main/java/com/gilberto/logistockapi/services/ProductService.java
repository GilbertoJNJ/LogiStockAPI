package com.gilberto.logistockapi.services;

import com.gilberto.logistockapi.mappers.IProductMapper;
import com.gilberto.logistockapi.models.dto.request.ProductBaseForm;
import com.gilberto.logistockapi.models.dto.request.ProductForm;
import com.gilberto.logistockapi.models.dto.request.QuantityForm;
import com.gilberto.logistockapi.models.dto.response.ProductDTO;
import com.gilberto.logistockapi.models.entity.Product;
import com.gilberto.logistockapi.exceptions.ProductAlreadyRegisteredException;
import com.gilberto.logistockapi.exceptions.ProductNotFoundException;
import com.gilberto.logistockapi.exceptions.ProductStockExceededException;
import com.gilberto.logistockapi.exceptions.ProductStockUnderThanZeroException;
import com.gilberto.logistockapi.repositories.ProductRepository;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    
    private final IProductMapper productMapper;
    
    public ProductService(@Autowired ProductRepository productRepository,
                          @Autowired IProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper     = productMapper;
    }

    @Override
    public ProductDTO create(ProductForm productForm) throws ProductAlreadyRegisteredException{

        verifyIfIsAlreadyRegistered(productForm.getBarCode());
        
        var product = this.productMapper.toProduct(productForm);
        var savedProduct = this.productRepository.save(product);
        return this.productMapper.toProductDTO(savedProduct);
    }
    
    @Override
    public List<ProductDTO> listAll() {
        return this.productRepository.findAll().stream()
            .map(this.productMapper::toProductDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public ProductDTO findByBarCode(String barCode) throws ProductNotFoundException {
        return productRepository.findByBarCode(barCode)
            .map(this.productMapper::toProductDTO)
            .orElseThrow(() -> new ProductNotFoundException(barCode));
    }
    
    @Override
    public void delete(Long id) throws ProductNotFoundException {
        this.verifyIfExists(id);
        this.productRepository.deleteById(id);
    }
    
    @Override
    public ProductDTO updateById(Long id, ProductBaseForm productForm) throws ProductNotFoundException {
        var product = this.verifyIfExists(id);
        product.setName(productForm.getName());
        product.setCategory(productForm.getCategory());
        product.setUnitPrice(productForm.getUnitPrice());
        product.setMeasureUnit(productForm.getMeasureUnit());
        product.setDescription(productForm.getDescription());

        var updatedProduct = productRepository.save(product);
        
        return this.productMapper.toProductDTO(updatedProduct);
    }
    
    @Override
    public ProductDTO increment(Long id, @Valid QuantityForm quantity) throws ProductNotFoundException, ProductStockExceededException {
        var product = verifyIfExists(id);
        var updatedQuantity = sumQuantity(product.getStockQuantity(), quantity.getQuantity());
        
        if (updatedQuantity > product.getMaxStockLevel()) {
            throw new ProductStockExceededException(id, quantity.getQuantity());
        }
        
        product.setStockQuantity(updatedQuantity);
        var updatedProduct = productRepository.save(product);
        return this.productMapper.toProductDTO(updatedProduct);
    }
    
    @Override
    public ProductDTO decrement(Long id, @Valid QuantityForm quantity) throws ProductNotFoundException, ProductStockUnderThanZeroException {
        var product = verifyIfExists(id);
        var quantityAfterDecrement = Math.max(0,
            subtractQuantity(product.getStockQuantity(), quantity.getQuantity()));
        
        if (quantityAfterDecrement == 0) {
            throw new ProductStockUnderThanZeroException(id, quantity.getQuantity());
        }
        
        product.setStockQuantity(quantityAfterDecrement);
        var updatedProduct = productRepository.save(product);
        return this.productMapper.toProductDTO(updatedProduct);
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
        var optSavedProduct = this.productRepository.findByBarCode(barCode);
        if (optSavedProduct.isPresent()) {
            throw new ProductAlreadyRegisteredException(barCode);
        }
    }

}
