package com.gilberto.logistockapi.services.implementations;

import com.gilberto.logistockapi.mappers.IProductMapper;
import com.gilberto.logistockapi.models.dto.request.ProductFilter;
import com.gilberto.logistockapi.models.dto.request.ProductUpdateForm;
import com.gilberto.logistockapi.models.dto.request.ProductForm;
import com.gilberto.logistockapi.models.dto.request.QuantityForm;
import com.gilberto.logistockapi.models.dto.response.ProductDTO;
import com.gilberto.logistockapi.models.entity.Product;
import com.gilberto.logistockapi.exceptions.ProductAlreadyRegisteredException;
import com.gilberto.logistockapi.exceptions.ProductNotFoundException;
import com.gilberto.logistockapi.exceptions.ProductStockExceededException;
import com.gilberto.logistockapi.exceptions.ProductStockUnderThanZeroException;
import com.gilberto.logistockapi.models.enums.Category;
import com.gilberto.logistockapi.repositories.IProductRepository;
import com.gilberto.logistockapi.services.IProductService;
import com.gilberto.logistockapi.services.ISupplierService;
import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService implements IProductService {

    private final IProductRepository productRepository;
    
    private final IProductMapper productMapper;
    
    private final ISupplierService supplierService;
    
    public ProductService(@Autowired IProductRepository productRepository,
                          @Autowired ISupplierService supplierService,
                          @Autowired IProductMapper productMapper) {
        this.productRepository  = productRepository;
        this.supplierService    = supplierService;
        this.productMapper      = productMapper;
    }

    @Override
    public ProductDTO create(ProductForm productForm) throws ProductAlreadyRegisteredException{

        verifyIfIsAlreadyRegistered(productForm.barCode());
        
        var product = this.productMapper.toProduct(productForm);
        product.setSupplier(this.supplierService.save(productForm.supplier()));
        var savedProduct = this.productRepository.save(product);
        return this.productMapper.toProductDTO(savedProduct);
    }
    
    @Override
    public List<ProductDTO> listAll(ProductFilter filter) {
        var search = StringUtils.defaultIfBlank(filter.search(), "");
        
        var categories = filter.categories() == null ?
                         Arrays.asList(Category.values()) :
                         filter.categories();
        
        var pageNumber = filter.pageNumber() * filter.pageSize();
        
        return this.productRepository.findAllByFilters(categories, search, filter.pageSize(),
                pageNumber).stream()
            .map(this.productMapper::toProductDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public ProductDTO findById(Long id) throws ProductNotFoundException {
        return this.productRepository.findById(id)
            .map(this.productMapper::toProductDTO)
            .orElseThrow(ProductNotFoundException::new);
    }
    
    @Override
    public ProductDTO findByBarCode(String barCode) throws ProductNotFoundException {
        return this.productRepository.findByBarCode(barCode)
            .map(this.productMapper::toProductDTO)
            .orElseThrow(ProductNotFoundException::new);
    }
    
    @Override
    public void delete(Long id) throws ProductNotFoundException {
        this.verifyIfExists(id);
        this.productRepository.deleteById(id);
    }
    
    @Override
    public ProductDTO updateById(Long id, ProductUpdateForm updateForm)
        throws ProductNotFoundException {
        var product = this.verifyIfExists(id);
        product.setName(updateForm.name());
        product.setCategory(updateForm.category());
        product.setUnitPrice(updateForm.unitPrice());
        product.setMeasureUnit(updateForm.measureUnit());
        product.setDescription(updateForm.description());
        product.setMaxStockLevel(updateForm.maxStockLevel());
        product.setSupplier(this.supplierService.save(updateForm.supplier()));

        var updatedProduct = this.productRepository.save(product);
        
        return this.productMapper.toProductDTO(updatedProduct);
    }
    
    @Override
    public ProductDTO increment(Long id, QuantityForm quantityForm)
        throws ProductNotFoundException, ProductStockExceededException {
        var product = verifyIfExists(id);
        var updatedQuantity = sumQuantity(product.getStockQuantity(), quantityForm.quantity());
        
        if (updatedQuantity > product.getMaxStockLevel()) {
            throw new ProductStockExceededException();
        }
        
        product.setStockQuantity(updatedQuantity);
        var updatedProduct = this.productRepository.save(product);
        return this.productMapper.toProductDTO(updatedProduct);
    }
    
    @Override
    public ProductDTO decrement(Long id, QuantityForm quantityForm)
        throws ProductNotFoundException, ProductStockUnderThanZeroException {
        var product = verifyIfExists(id);
        var updatedQuantity = subtractQuantity(product.getStockQuantity(), quantityForm.quantity());
        
        if (updatedQuantity == 0) {
            throw new ProductStockUnderThanZeroException();
        }
        
        product.setStockQuantity(updatedQuantity);
        var updatedProduct = this.productRepository.save(product);
        return this.productMapper.toProductDTO(updatedProduct);
    }
    
    private Integer sumQuantity(Integer stockQuantity, Integer quantityToIncrement) {
        return stockQuantity + quantityToIncrement;
    }
    
    private Integer subtractQuantity(Integer stockQuantity, Integer quantityToDecrement) {
        return Math.max(0, stockQuantity - quantityToDecrement);
    }

    private Product verifyIfExists(Long id) throws ProductNotFoundException {
        return this.productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);
    }
    
    private void verifyIfIsAlreadyRegistered(String barCode)
        throws ProductAlreadyRegisteredException {
        var optSavedProduct = this.productRepository.findByBarCode(barCode);
        if (optSavedProduct.isPresent()) {
            throw new ProductAlreadyRegisteredException();
        }
    }

}
