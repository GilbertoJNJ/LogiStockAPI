package com.gilberto.logistockapi.services;

import com.gilberto.logistockapi.builder.ProductDTOBuilder;
import com.gilberto.logistockapi.models.dto.request.ProductForm;
import com.gilberto.logistockapi.models.dto.response.MessageResponseDTO;
import com.gilberto.logistockapi.models.entity.Product;
import com.gilberto.logistockapi.exceptions.ProductAlreadyRegisteredException;
import com.gilberto.logistockapi.exceptions.ProductNotFoundException;
import com.gilberto.logistockapi.exceptions.ProductStockExceededException;
import com.gilberto.logistockapi.exceptions.ProductStockUnderThanZeroException;
import com.gilberto.logistockapi.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    private static final long INVALID_PRODUCT_ID = 1L;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    //CreateProduct
    @Test
    void whenProductIsInformedThenItShouldBeCreated() throws ProductAlreadyRegisteredException {
        // given
        ProductForm expectedProductDTO   = ProductDTOBuilder.builder().build().toProductDTO();
        Product     expectedSavedProduct = new Product(expectedProductDTO);

        // when
        when(productRepository.findByName(expectedProductDTO.getName())).thenReturn(Optional.empty());
        when(productRepository.save(expectedSavedProduct)).thenReturn(expectedSavedProduct);

        //then
        MessageResponseDTO createdProductDTO = productService.createProduct(expectedProductDTO);

        assertThat(createdProductDTO.getMessage(), is("Created product with ID "));
    }

    @Test
    void whenAlreadyRegisteredProductInformedThenAnExceptionShouldBeThrown() {
        // given
        ProductForm expectedProductDTO = ProductDTOBuilder.builder().build().toProductDTO();
        Product     duplicatedProduct  = new Product(expectedProductDTO);

        // when
        when(productRepository.findByName(expectedProductDTO.getName())).thenReturn(Optional.of(duplicatedProduct));

        // then
        assertThrows(ProductAlreadyRegisteredException.class, () -> productService.createProduct(expectedProductDTO));
    }

    //Find By Name
    @Test
    void whenValidProductNameIsGivenThenReturnAProduct() throws ProductNotFoundException {
        // given
        ProductForm expectedFoundProductDTO = ProductDTOBuilder.builder().build().toProductDTO();
        Product     expectedFoundProduct    = new Product(expectedFoundProductDTO);

        // when
        when(productRepository.findByName(expectedFoundProduct.getName())).thenReturn(Optional.of(expectedFoundProduct));

        // then
        ProductForm foundProductDTO = productService.findByName(expectedFoundProductDTO.getName());

        assertThat(foundProductDTO, is(equalTo(expectedFoundProductDTO)));
    }

    @Test
    void whenNotRegisteredProductNameIsGivenThenThrowAnException() {
        // given
        ProductForm expectedFoundProductDTO = ProductDTOBuilder.builder().build().toProductDTO();

        // when
        when(productRepository.findByName(expectedFoundProductDTO.getName())).thenReturn(Optional.empty());

        // then
        assertThrows(ProductNotFoundException.class, () -> productService.findByName(expectedFoundProductDTO.getName()));
    }

    //Find All
    @Test
    void whenListProductsIsCalledThenReturnAListOfProducts() {
        // given
        ProductForm expectedFoundProductDTO = ProductDTOBuilder.builder().build().toProductDTO();
        Product     expectedFoundProduct    = new Product(expectedFoundProductDTO);

        //when
        when(productRepository.findAll()).thenReturn(Collections.singletonList(expectedFoundProduct));

        //then
        List<ProductForm> foundListProductDTO = productService.listAll();

        assertThat(foundListProductDTO, is(not(empty())));
        assertThat(foundListProductDTO.get(0), is(equalTo(expectedFoundProductDTO)));
    }

    @Test
    void whenAListIsCalledWithoutProductThenReturnAnEmptyList() {
        //when
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        //then
        List<ProductForm> foundListProductsDTO = productService.listAll();

        assertThat(foundListProductsDTO, is(empty()));
    }

    //Delete By Id
    @Test
    void whenExclusionIsCalledWithValidIdThenAProductShouldBeDeleted() throws ProductNotFoundException {
        // given
        ProductForm expectedDeletedProductDTO = ProductDTOBuilder.builder().build().toProductDTO();
        Product     expectedDeletedProduct    = new Product(expectedDeletedProductDTO);

        // when
        when(productRepository.findById(expectedDeletedProductDTO.getId())).thenReturn(Optional.of(expectedDeletedProduct));
        doNothing().when(productRepository).deleteById(expectedDeletedProductDTO.getId());

        // then
        MessageResponseDTO deletedProduct = productService.delete(expectedDeletedProductDTO.getId());

        assertThat(deletedProduct.getMessage(), is("Deleted product with ID " + expectedDeletedProductDTO.getId()));
        verify(productRepository, times(1)).findById(expectedDeletedProductDTO.getId());
        verify(productRepository, times(1)).deleteById(expectedDeletedProductDTO.getId());
    }

    @Test
    void whenExclusionIsCalledWithInvalidIdThenThrowAnException() {

        // when
        when(productRepository.findById(INVALID_PRODUCT_ID)).thenReturn(Optional.empty());

        // then
        assertThrows(ProductNotFoundException.class, () -> productService.delete(INVALID_PRODUCT_ID));

    }

    //Update By id
    @Test
    void whenUpdateIsCalledWithValidIdThenAProductShouldBeUpdated() throws ProductNotFoundException {
        //Given
        ProductForm expectedUpdatedProductDTO = ProductDTOBuilder.builder().build().toProductDTO();
        Product     expectedUpdatedProduct    = new Product(expectedUpdatedProductDTO);

        //when
        when(productRepository.findById(expectedUpdatedProductDTO.getId())).thenReturn(Optional.of(expectedUpdatedProduct));
        when(productRepository.save(expectedUpdatedProduct)).thenReturn(expectedUpdatedProduct);

        //then
        MessageResponseDTO updatedProductDTO = productService.updateById(expectedUpdatedProductDTO.getId(),expectedUpdatedProductDTO);

        assertThat(updatedProductDTO.getMessage(), is("Updated product with ID " + expectedUpdatedProductDTO.getId()));
    }

    @Test
    void whenUpdateIsCalledWithInvalidIdThenThrowAnException() {
        //Given
        ProductForm expectedUpdatedProductDTO = ProductDTOBuilder.builder().build().toProductDTO();

        //when
        when(productRepository.findById(INVALID_PRODUCT_ID)).thenReturn(Optional.empty());

        //then
        assertThrows(ProductNotFoundException.class, () -> productService.updateById(INVALID_PRODUCT_ID,expectedUpdatedProductDTO));
    }

    //Increment
    @Test
    void whenIncrementIsCalledThenIncrementProductStock() throws ProductNotFoundException, ProductStockExceededException {
        //given
        ProductForm expectedProductDTO = ProductDTOBuilder.builder().build().toProductDTO();
        Product     expectedProduct    = new Product(expectedProductDTO);

        //when
        when(productRepository.findById(expectedProductDTO.getId())).thenReturn(Optional.of(expectedProduct));
        when(productRepository.save(expectedProduct)).thenReturn(expectedProduct);

        int quantityToIncrement = 89;
        int expectedQuantityAfterIncrement = expectedProductDTO.getQuantity() + quantityToIncrement;

        // then
        ProductForm incrementedProductDTO = productService.increment(expectedProductDTO.getId(), quantityToIncrement);

        assertThat(expectedQuantityAfterIncrement, equalTo(incrementedProductDTO.getQuantity()));
        assertThat(expectedQuantityAfterIncrement, lessThan(expectedProductDTO.getMaxQuantity()));
    }

    @Test
    void whenIncrementAfterSumIsEqualsTheMaxQuantityThenIncrementProductStock() throws ProductNotFoundException, ProductStockExceededException {
        //given
        ProductForm expectedProductDTO = ProductDTOBuilder.builder().build().toProductDTO();
        Product     expectedProduct    = new Product(expectedProductDTO);

        //when
        when(productRepository.findById(expectedProductDTO.getId())).thenReturn(Optional.of(expectedProduct));
        when(productRepository.save(expectedProduct)).thenReturn(expectedProduct);

        int quantityToIncrement = 90;
        int expectedQuantityAfterIncrement = expectedProductDTO.getQuantity() + quantityToIncrement;

        // then
        ProductForm incrementedProductDTO = productService.increment(expectedProductDTO.getId(), quantityToIncrement);

        assertThat(expectedQuantityAfterIncrement, equalTo(incrementedProductDTO.getQuantity()));
        assertThat(expectedQuantityAfterIncrement, equalTo(expectedProductDTO.getMaxQuantity()));
    }

    @Test
    void whenIncrementAfterSumIsGreatherThanMaxThenThrowException() {
        ProductForm expectedProductDTO = ProductDTOBuilder.builder().build().toProductDTO();
        Product     expectedProduct    = new Product(expectedProductDTO);

        when(productRepository.findById(expectedProductDTO.getId())).thenReturn(Optional.of(expectedProduct));

        int quantityToIncrement = 91;
        assertThrows(ProductStockExceededException.class, () -> productService.increment(expectedProductDTO.getId(), quantityToIncrement));
    }

    @Test
    void whenIncrementIsCalledWithInvalidIdThenThrowException() {
        int quantityToIncrement = 10;

        when(productRepository.findById(INVALID_PRODUCT_ID)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.increment(INVALID_PRODUCT_ID, quantityToIncrement));
    }

    //Decrement
    @Test
    void whenDecrementIsCalledThenDecrementProductStock() throws ProductNotFoundException, ProductStockUnderThanZeroException {
        ProductForm expectedProductDTO = ProductDTOBuilder.builder().build().toProductDTO();
        Product     expectedProduct    = new Product(expectedProductDTO);

        when(productRepository.findById(expectedProductDTO.getId())).thenReturn(Optional.of(expectedProduct));
        when(productRepository.save(expectedProduct)).thenReturn(expectedProduct);

        int quantityToDecrement = 9;
        int         expectedQuantityAfterDecrement = expectedProductDTO.getQuantity() - quantityToDecrement;
        ProductForm decrementedProductDTO          = productService.decrement(expectedProductDTO.getId(), quantityToDecrement);

        assertThat(expectedQuantityAfterDecrement, equalTo(decrementedProductDTO.getQuantity()));
        assertThat(expectedQuantityAfterDecrement, greaterThan(0));
    }

    @Test
    void whenDecrementIsCalledToEmptyStockThenEmptyProductStock() throws ProductStockUnderThanZeroException, ProductNotFoundException {
        ProductForm expectedProductDTO = ProductDTOBuilder.builder().build().toProductDTO();
        Product     expectedProduct    = new Product(expectedProductDTO);

        when(productRepository.findById(expectedProductDTO.getId())).thenReturn(Optional.of(expectedProduct));
        when(productRepository.save(expectedProduct)).thenReturn(expectedProduct);

        int quantityToDecrement = 10;
        int         expectedQuantityAfterDecrement = expectedProductDTO.getQuantity() - quantityToDecrement;
        ProductForm incrementedProductDTO          = productService.decrement(expectedProductDTO.getId(), quantityToDecrement);

        assertThat(expectedQuantityAfterDecrement, equalTo(0));
        assertThat(expectedQuantityAfterDecrement, equalTo(incrementedProductDTO.getQuantity()));
    }

    @Test
    void whenDecrementIsLowerThanZeroThenThrowException() {
        ProductForm expectedProductDTO = ProductDTOBuilder.builder().build().toProductDTO();
        Product     expectedProduct    = new Product(expectedProductDTO);

        when(productRepository.findById(expectedProductDTO.getId())).thenReturn(Optional.of(expectedProduct));

        int quantityToDecrement = 11;
        assertThrows(ProductStockUnderThanZeroException.class, () -> productService.decrement(expectedProductDTO.getId(), quantityToDecrement));
    }

    @Test
    void whenDecrementIsCalledWithInvalidIdThenThrowException() {
        int quantityToDecrement = 10;

        when(productRepository.findById(INVALID_PRODUCT_ID)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.decrement(INVALID_PRODUCT_ID, quantityToDecrement));
    }
}
