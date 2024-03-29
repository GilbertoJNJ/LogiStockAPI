package com.gilberto.logistockapi.controllers;

import com.gilberto.logistockapi.exceptions.ProductAlreadyRegisteredException;
import com.gilberto.logistockapi.exceptions.ProductNotFoundException;
import com.gilberto.logistockapi.exceptions.ProductStockExceededException;
import com.gilberto.logistockapi.exceptions.ProductStockUnderThanZeroException;
import com.gilberto.logistockapi.models.dto.request.ProductFilter;
import com.gilberto.logistockapi.models.dto.request.ProductForm;
import com.gilberto.logistockapi.models.dto.request.ProductUpdateForm;
import com.gilberto.logistockapi.models.dto.request.QuantityForm;
import com.gilberto.logistockapi.models.dto.response.ProductDTO;
import com.gilberto.logistockapi.services.IProductService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {
  
  private final IProductService productService;
  
  public ProductController(@Autowired IProductService productService) {
    this.productService = productService;
  }
  
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<ProductDTO> create(@RequestBody @Valid ProductForm productForm)
      throws ProductAlreadyRegisteredException {
    return ResponseEntity.created(URI.create(""))
        .body(this.productService.create(productForm));
  }
  
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<List<ProductDTO>> listAll(@Valid ProductFilter filter) {
    return ResponseEntity.ok(this.productService.listAll(filter));
  }
  
  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<ProductDTO> findById(@PathVariable Long id)
      throws ProductNotFoundException {
    return ResponseEntity.ok(this.productService.findById(id));
  }
  
  @GetMapping("/barcode/{barcode}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<ProductDTO> findByBarCode(@PathVariable String barcode)
      throws ProductNotFoundException {
    return ResponseEntity.ok(this.productService.findByBarCode(barcode));
  }
  
  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<ProductDTO> updateById(@PathVariable Long id,
                                               @RequestBody @Valid ProductUpdateForm updateForm)
      throws ProductNotFoundException {
    return ResponseEntity.ok(this.productService.updateById(id, updateForm));
  }
  
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<Object> deleteById(@PathVariable Long id) throws ProductNotFoundException {
    this.productService.delete(id);
    return ResponseEntity.noContent().build();
  }
  
  @PatchMapping("/{id}/increase")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<ProductDTO> increaseStock(@PathVariable Long id,
                                                  @RequestBody @Valid QuantityForm quantityForm)
      throws ProductNotFoundException, ProductStockExceededException {
    return ResponseEntity.ok(this.productService.increaseStock(id, quantityForm));
  }
  
  @PatchMapping("/{id}/decrease")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<ProductDTO> decreaseStock(@PathVariable Long id,
                                              @RequestBody @Valid QuantityForm quantityForm)
      throws ProductStockUnderThanZeroException, ProductNotFoundException {
    return ResponseEntity.ok(this.productService.decreaseStock(id, quantityForm));
  }
  
}
