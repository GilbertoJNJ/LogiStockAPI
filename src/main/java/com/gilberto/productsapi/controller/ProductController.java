package com.gilberto.productsapi.controller;

import com.gilberto.productsapi.dto.request.ProductDTO;
import com.gilberto.productsapi.dto.request.QuantityDTO;
import com.gilberto.productsapi.dto.response.MessageResponseDTO;
import com.gilberto.productsapi.exception.ProductAlreadyRegisteredException;
import com.gilberto.productsapi.exception.ProductNotFoundException;
import com.gilberto.productsapi.exception.ProductStockExceededException;
import com.gilberto.productsapi.exception.ProductStockUnderThanZeroException;
import com.gilberto.productsapi.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ProductController {

    private ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponseDTO createProduct(@RequestBody @Valid ProductDTO productDTO) throws ProductAlreadyRegisteredException {
        return productService.createProduct(productDTO);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDTO> listAll() {
        return productService.listAll();
    }

    @GetMapping("/{name}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO findByName(@PathVariable String name) throws ProductNotFoundException {
        return productService.findByName(name);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MessageResponseDTO updateById(@PathVariable Long id, @RequestBody @Valid ProductDTO productDTO) throws ProductNotFoundException {
        return productService.updateById(id, productDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public MessageResponseDTO deleteById(@PathVariable Long id) throws ProductNotFoundException {
        return productService.delete(id);
    }

    @PatchMapping("/{id}/increment")
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO increment(@PathVariable Long id, @RequestBody @Valid QuantityDTO quantityDTO) throws ProductNotFoundException, ProductStockExceededException {
        return productService.increment(id, quantityDTO.getQuantity());
    }

    @PatchMapping("/{id}/decrement")
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO decrement(@PathVariable Long id, @RequestBody @Valid QuantityDTO quantityDTO) throws ProductStockUnderThanZeroException, ProductNotFoundException {
        return productService.decrement(id, quantityDTO.getQuantity());
    }

}
