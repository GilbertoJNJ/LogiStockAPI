package com.gilberto.logistockapi.services;

import com.gilberto.logistockapi.exceptions.ProductAlreadyRegisteredException;
import com.gilberto.logistockapi.exceptions.ProductNotFoundException;
import com.gilberto.logistockapi.exceptions.ProductStockExceededException;
import com.gilberto.logistockapi.exceptions.ProductStockUnderThanZeroException;
import com.gilberto.logistockapi.models.dto.request.QuantityForm;
import com.gilberto.logistockapi.repositories.IProductRepository;
import com.gilberto.logistockapi.services.implementations.ProductService;
import com.gilberto.logistockapi.utils.ModelUtils;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.springframework.context.annotation.Profile;

@Profile("test")
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
  
  private static final long PRODUCT_ID         = 1L;
  private static final long INVALID_PRODUCT_ID = 2L;
  
  @Mock
  private IProductRepository productRepository;
  
  @Mock
  private ISupplierService supplierService;
  
  @InjectMocks
  private ProductService productService;
  
  // Create Product
  @Test
  void shouldCreateProduct() throws ProductAlreadyRegisteredException {
    // given
    var productForm = ModelUtils.getProductForm();
    var product     = ModelUtils.getProduct();
    
    // when
    when(this.productRepository.findByBarCode(productForm.barCode()))
        .thenReturn(Optional.empty());
    when(this.supplierService.save(productForm.supplier()))
        .thenReturn(null);
    when(this.productRepository.save(any()))
        .thenReturn(product);
    
    // then
    var productDTO = this.productService.create(productForm);
    
    assertThat(productDTO.id(), is(product.getId()));
    assertThat(productDTO.barCode(), is(product.getBarCode()));
    assertThat(productDTO.name(), is(product.getName()));
  }
  
  @Test
  void whenAProductHasAlreadyBeenRegisteredThenAnExceptionMustBeThrown() {
    // given
    var productForm = ModelUtils.getProductForm();
    var product     = ModelUtils.getProduct();
    
    // when
    when(this.productRepository.findByBarCode(productForm.barCode()))
        .thenReturn(Optional.of(product));
    
    // then
    assertThrows(ProductAlreadyRegisteredException.class,
        () -> this.productService.create(productForm));
  }
  
  // Find All
  @Test
  void shouldReturnAProductList() {
    // given
    var productFilter = ModelUtils.getProductFilter();
    var product       = ModelUtils.getProduct();
    var pageNumber    = productFilter.pageNumber() * productFilter.pageSize();
    
    // when
    when(this.productRepository.findAllByFilters(productFilter.categories(),
        productFilter.search(), productFilter.pageSize(), pageNumber))
        .thenReturn(Collections.singletonList(product));
    
    // then
    var productDTOList = this.productService.listAll(productFilter);
    
    assertThat(productDTOList, is(not(empty())));
    assertThat(productDTOList.get(0).name(), is(product.getName()));
    assertThat(productDTOList.get(0).barCode(), is(product.getBarCode()));
  }
  
  @Test
  void shouldReturnAnEmptyProductList() {
    // given
    var productFilter = ModelUtils.getProductFilter();
    var pageNumber    = productFilter.pageNumber() * productFilter.pageSize();
    
    // when
    when(this.productRepository.findAllByFilters(productFilter.categories(),
        productFilter.search(), productFilter.pageSize(), pageNumber))
        .thenReturn(Collections.emptyList());
    
    // then
    var productDTOList = this.productService.listAll(productFilter);
    
    assertThat(productDTOList, is(empty()));
  }
  
  // Find By Barcode
  @Test
  void whenAProductBarcodeIsValidThenReturnAProduct() throws ProductNotFoundException {
    // given
    var product     = ModelUtils.getProduct();
    
    // when
    when(this.productRepository.findByBarCode(product.getBarCode()))
        .thenReturn(Optional.of(product));
    
    // then
    var productDTO = this.productService.findByBarCode(product.getBarCode());
    
    assertThat(productDTO.id(), is(product.getId()));
    assertThat(productDTO.barCode(), is(product.getBarCode()));
    assertThat(productDTO.name(), is(product.getName()));
  }
  
  @Test
  void whenAProductBarcodeHasNotBeenRegisteredThenAnExceptionMustBeThrown() {
    // given
    var product = ModelUtils.getProduct();
    
    // when
    when(this.productRepository.findByBarCode(product.getBarCode()))
        .thenReturn(Optional.empty());
    
    // then
    assertThrows(ProductNotFoundException.class,
        () -> this.productService.findByBarCode(product.getBarCode()));
  }
  
  // Find By Id
  @Test
  public void whenAProductIdIsValidThenReturnAProduct() throws ProductNotFoundException {
    // given
    var product = ModelUtils.getProduct();
    
    // when
    when(this.productRepository.findById(PRODUCT_ID))
        .thenReturn(Optional.of(product));
    
    // then
    var productDTO = this.productService.findById(PRODUCT_ID);
    
    assertThat(productDTO.id(), is(product.getId()));
    assertThat(productDTO.barCode(), is(product.getBarCode()));
    assertThat(productDTO.name(), is(product.getName()));
    
  }
  
  @Test
  public void whenAProductIdIsInvalidThenAnExceptionMustBeThrown() {
    // when
    when(this.productRepository.findById(INVALID_PRODUCT_ID))
        .thenReturn(Optional.empty());
    
    // then
    assertThrows(ProductNotFoundException.class,
        () -> this.productService.findById(INVALID_PRODUCT_ID));
    
  }
  
  // Delete By Id
  @Test
  void shouldDeleteAProduct() throws ProductNotFoundException {
    // given
    var product = ModelUtils.getProduct();
    
    // when
    when(this.productRepository.findById(product.getId()))
        .thenReturn(Optional.of(product));
    doNothing().when(this.productRepository).deleteById(product.getId());
    
    // then
    this.productService.delete(product.getId());
    
    verify(this.productRepository, times(1)).findById(product.getId());
    verify(this.productRepository, times(1)).deleteById(product.getId());
  }
  
  @Test
  void whenAProductWithAnInvalidIdIsDeletedThenAnExceptionMustBeThrown() {
    // when
    when(this.productRepository.findById(INVALID_PRODUCT_ID))
        .thenReturn(Optional.empty());
    
    // then
    assertThrows(ProductNotFoundException.class,
        () -> this.productService.delete(INVALID_PRODUCT_ID));
    
  }
  
  // Update By id
  @Test
  void shouldUpdateAProduct() throws ProductNotFoundException {
    // given
    var productUpdateForm = ModelUtils.getProductUpdateForm();
    var product           = ModelUtils.getProduct();
    
    // when
    when(this.productRepository.findById(PRODUCT_ID))
        .thenReturn(Optional.of(product));
    when(this.productRepository.save(any()))
        .thenReturn(product);
    
    // then
    var productDTO = this.productService.updateById(PRODUCT_ID, productUpdateForm);
    
    assertThat(productDTO.name(), is(product.getName()));
    assertThat(productDTO.barCode(), is(product.getBarCode()));
  }
  
  @Test
  void whenAProductWithInvalidIdIsUpdatedThenAnExceptionMustBeThrown() {
    // given
    var productUpdateForm = ModelUtils.getProductUpdateForm();
    
    // when
    when(this.productRepository.findById(INVALID_PRODUCT_ID))
        .thenReturn(Optional.empty());
    
    // then
    assertThrows(ProductNotFoundException.class,
        () -> this.productService.updateById(INVALID_PRODUCT_ID, productUpdateForm));
  }
  
  // Increase Stock
  @Test
  void shouldIncreaseProductStock()
      throws ProductNotFoundException, ProductStockExceededException {
    // given
    var quantityForm = new QuantityForm(10);
    var product      = ModelUtils.getProduct();
    
    var savedProduct = ModelUtils.getProduct();
    savedProduct.setStockQuantity(product.getStockQuantity() + quantityForm.quantity());
    
    // when
    when(this.productRepository.findById(PRODUCT_ID))
        .thenReturn(Optional.of(product));
    when(this.productRepository.save(any()))
        .thenReturn(savedProduct);
    
    // then
    var productDTO = this.productService.increaseStock(PRODUCT_ID, quantityForm);
    
    assertThat(productDTO.stockQuantity(), lessThan(savedProduct.getMaxStockLevel()));
  }
  
  @Test
  void WhenTheQuantityAfterTheSumIsEqualToTheMaximumStockThenIncreaseTheStock()
      throws ProductNotFoundException, ProductStockExceededException {
    // given
    var quantityForm = new QuantityForm(90);
    var product      = ModelUtils.getProduct();
    
    var savedProduct = ModelUtils.getProduct();
    savedProduct.setStockQuantity(product.getStockQuantity() + quantityForm.quantity());
    
    // when
    when(this.productRepository.findById(PRODUCT_ID))
        .thenReturn(Optional.of(product));
    when(this.productRepository.save(any()))
        .thenReturn(savedProduct);
    
    // then
    var productDTO = this.productService.increaseStock(PRODUCT_ID, quantityForm);
    
    assertThat(productDTO.stockQuantity(), equalTo(savedProduct.getMaxStockLevel()));
  }
  
  @Test
  void whenTheQuantityAfterTheSumIsGreaterThanTheMaximumStockThenAnExceptionMustBeThrown() {
    // given
    var quantityForm = new QuantityForm(91);
    var product      = ModelUtils.getProduct();
    
    // when
    when(this.productRepository.findById(PRODUCT_ID))
        .thenReturn(Optional.of(product));
    
    // then
    assertThrows(ProductStockExceededException.class,
        () -> this.productService.increaseStock(PRODUCT_ID, quantityForm));
  }
  
  @Test
  void whenIncreaseTheStockOfAProductWithAnInvalidIdThenAnExceptionMustBeThrown() {
    // given
    var quantityForm = new QuantityForm(10);
    
    // when
    when(this.productRepository.findById(INVALID_PRODUCT_ID))
        .thenReturn(Optional.empty());
    
    // then
    assertThrows(ProductNotFoundException.class,
        () -> this.productService.increaseStock(INVALID_PRODUCT_ID, quantityForm));
  }
  
  // Decreases
  @Test
  void shouldDecreasesProductStock()
      throws ProductNotFoundException, ProductStockUnderThanZeroException {
    // given
    var quantityForm = new QuantityForm(2);
    var product      = ModelUtils.getProduct();
    
    var quantity     = product.getStockQuantity() - quantityForm.quantity();
    var savedProduct = ModelUtils.getProduct();
    savedProduct.setStockQuantity(quantity);
    
    // when
    when(this.productRepository.findById(PRODUCT_ID))
        .thenReturn(Optional.of(product));
    when(this.productRepository.save(any()))
        .thenReturn(savedProduct);
    
    // then
    var productDTO = this.productService.decreaseStock(PRODUCT_ID, quantityForm);
    
    assertThat(productDTO.stockQuantity(), equalTo(quantity));
    assertThat(productDTO.stockQuantity(), greaterThan(0));
  }
  
  @Test
  void whenTheQuantityAfterSubtractionIsEqualToZeroThenDecreasesStock()
      throws ProductStockUnderThanZeroException, ProductNotFoundException {
    // given
    var quantityForm = new QuantityForm(10);
    var product      = ModelUtils.getProduct();
    
    var quantity     = product.getStockQuantity() - quantityForm.quantity();
    var savedProduct = ModelUtils.getProduct();
    savedProduct.setStockQuantity(quantity);
    
    // when
    when(this.productRepository.findById(PRODUCT_ID))
        .thenReturn(Optional.of(product));
    when(this.productRepository.save(any()))
        .thenReturn(savedProduct);
    
    // then
    var productDTO = this.productService.decreaseStock(PRODUCT_ID, quantityForm);
    
    assertThat(productDTO.stockQuantity(), equalTo(0));
  }
  
  @Test
  void whenTheQuantityAfterSubtractionIsLessThanZeroThenAnExceptionShouldBeThrown() {
    // given
    var quantityForm = new QuantityForm(11);
    var product      = ModelUtils.getProduct();
    
    // when
    when(this.productRepository.findById(PRODUCT_ID))
        .thenReturn(Optional.of(product));
    
    // then
    assertThrows(ProductStockUnderThanZeroException.class,
        () -> this.productService.decreaseStock(PRODUCT_ID, quantityForm));
  }
  
  @Test
  void whenDecreasesTheStockOfAProductWithAnInvalidIdThenAnExceptionMustBeThrown() {
    // given
    var quantityForm = new QuantityForm(10);
    
    // when
    when(this.productRepository.findById(INVALID_PRODUCT_ID))
        .thenReturn(Optional.empty());
    
    // then
    assertThrows(ProductNotFoundException.class,
        () -> this.productService.decreaseStock(INVALID_PRODUCT_ID, quantityForm));
  }
  
}
