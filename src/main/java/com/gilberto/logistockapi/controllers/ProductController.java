package com.gilberto.logistockapi.controllers;

import com.gilberto.logistockapi.models.dto.request.ProductForm;
import com.gilberto.logistockapi.models.dto.request.QuantityForm;
import com.gilberto.logistockapi.exceptions.ProductAlreadyRegisteredException;
import com.gilberto.logistockapi.exceptions.ProductNotFoundException;
import com.gilberto.logistockapi.exceptions.ProductStockExceededException;
import com.gilberto.logistockapi.exceptions.ProductStockUnderThanZeroException;
import com.gilberto.logistockapi.models.dto.response.ProductDTO;
import com.gilberto.logistockapi.services.IProductService;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final IProductService productService;
    
    public ProductController (@Autowired IProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProductDTO> create(@RequestBody @Valid ProductForm productForm) throws ProductAlreadyRegisteredException {
        return ResponseEntity.created(URI.create(""))
            .body(this.productService.create(productForm));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ProductDTO>> listAll() {
        return ResponseEntity.ok(this.productService.listAll());
    }

    @GetMapping("/{barcode}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ProductDTO> findByBarCode(@PathVariable String barcode) throws ProductNotFoundException {
        return ResponseEntity.ok(this.productService.findByBarCode(barcode));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ProductDTO> updateById(@PathVariable Long id,
                                                 @RequestBody @Valid ProductForm productDTO) throws ProductNotFoundException {
        return ResponseEntity.ok(this.productService.updateById(id, productDTO));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Object> deleteById(@PathVariable Long id) throws ProductNotFoundException {
        this.productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/increment")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ProductDTO> increment(@PathVariable Long id,
                                                @RequestBody @Valid QuantityForm quantityForm) throws ProductNotFoundException, ProductStockExceededException {
        return ResponseEntity.ok(this.productService.increment(id, quantityForm));
    }

    @PatchMapping("/{id}/decrement")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ProductDTO> decrement(@PathVariable Long id,
                                 @RequestBody @Valid QuantityForm quantityForm) throws ProductStockUnderThanZeroException, ProductNotFoundException {
        return ResponseEntity.ok(this.productService.decrement(id, quantityForm));
    }

}
