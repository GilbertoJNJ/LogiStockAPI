package com.gilberto.logistockapi.controllers;

import com.gilberto.logistockapi.models.dto.request.ProductForm;
import com.gilberto.logistockapi.models.dto.request.QuantityForm;
import com.gilberto.logistockapi.models.dto.response.MessageResponseDTO;
import com.gilberto.logistockapi.exceptions.ProductAlreadyRegisteredException;
import com.gilberto.logistockapi.exceptions.ProductNotFoundException;
import com.gilberto.logistockapi.exceptions.ProductStockExceededException;
import com.gilberto.logistockapi.exceptions.ProductStockUnderThanZeroException;
import com.gilberto.logistockapi.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;
    
    public ProductController (@Autowired ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponseDTO createProduct(@RequestBody @Valid ProductForm productDTO) throws ProductAlreadyRegisteredException {
        return productService.createProduct(productDTO);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductForm> listAll() {
        return productService.listAll();
    }

    @GetMapping("/{name}")
    @ResponseStatus(HttpStatus.OK)
    public ProductForm findByName(@PathVariable String name) throws ProductNotFoundException {
        return productService.findByBarCode(name);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MessageResponseDTO updateById(@PathVariable Long id, @RequestBody @Valid ProductForm productDTO) throws ProductNotFoundException {
        return productService.updateById(id, productDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public MessageResponseDTO deleteById(@PathVariable Long id) throws ProductNotFoundException {
        return productService.delete(id);
    }

    @PatchMapping("/{id}/increment")
    @ResponseStatus(HttpStatus.OK)
    public ProductForm increment(@PathVariable Long id, @RequestBody @Valid QuantityForm quantityDTO) throws ProductNotFoundException, ProductStockExceededException {
        return productService.increment(id, quantityDTO.getQuantity());
    }

    @PatchMapping("/{id}/decrement")
    @ResponseStatus(HttpStatus.OK)
    public ProductForm decrement(@PathVariable Long id, @RequestBody @Valid QuantityForm quantityDTO) throws ProductStockUnderThanZeroException, ProductNotFoundException {
        return productService.decrement(id, quantityDTO.getQuantity());
    }

}
