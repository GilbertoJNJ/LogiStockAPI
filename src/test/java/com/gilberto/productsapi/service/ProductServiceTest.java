package com.gilberto.productsapi.service;

import com.gilberto.productsapi.builder.ProductDTOBuilder;
import com.gilberto.productsapi.dto.request.ProductDTO;
import com.gilberto.productsapi.dto.response.MessageResponseDTO;
import com.gilberto.productsapi.entity.Product;
import com.gilberto.productsapi.exception.ProductAlreadyRegisteredException;
import com.gilberto.productsapi.exception.ProductNotFoundException;
import com.gilberto.productsapi.exception.ProductStockExceededException;
import com.gilberto.productsapi.exception.ProductStockUnderThanZeroException;
import com.gilberto.productsapi.mapper.ProductMapper;
import com.gilberto.productsapi.repository.ProductRepository;
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

    private ProductMapper productMapper = ProductMapper.INSTANCE;

    @InjectMocks
    private ProductService productService;

    //CreateProduct
    @Test
    void whenProductIsInformedThenItShouldBeCreated() throws ProductAlreadyRegisteredException {
        // given
        ProductDTO expectedProductDTO = ProductDTOBuilder.builder().build().toProductDTO();
        Product expectedSavedProduct = productMapper.toModel(expectedProductDTO);

        // when
        when(productRepository.findByName(expectedProductDTO.getName())).thenReturn(Optional.empty());
        when(productRepository.save(expectedSavedProduct)).thenReturn(expectedSavedProduct);

        //then
        MessageResponseDTO createdProductDTO = productService.createProduct(expectedProductDTO);

        assertThat(createdProductDTO.getMessage(), is("Created product with ID " + expectedProductDTO.getId()));
    }

    @Test
    void whenAlreadyRegisteredProductInformedThenAnExceptionShouldBeThrown() {
        // given
        ProductDTO expectedProductDTO = ProductDTOBuilder.builder().build().toProductDTO();
        Product duplicatedProduct = productMapper.toModel(expectedProductDTO);

        // when
        when(productRepository.findByName(expectedProductDTO.getName())).thenReturn(Optional.of(duplicatedProduct));

        // then
        assertThrows(ProductAlreadyRegisteredException.class, () -> productService.createProduct(expectedProductDTO));
    }

    //Find By Name
    @Test
    void whenValidProductNameIsGivenThenReturnAProduct() throws ProductNotFoundException {
        // given
        ProductDTO expectedFoundProductDTO = ProductDTOBuilder.builder().build().toProductDTO();
        Product expectedFoundProduct = productMapper.toModel(expectedFoundProductDTO);

        // when
        when(productRepository.findByName(expectedFoundProduct.getName())).thenReturn(Optional.of(expectedFoundProduct));

        // then
        ProductDTO foundProductDTO = productService.findByName(expectedFoundProductDTO.getName());

        assertThat(foundProductDTO, is(equalTo(expectedFoundProductDTO)));
    }

    @Test
    void whenNotRegisteredProductNameIsGivenThenThrowAnException() {
        // given
        ProductDTO expectedFoundProductDTO = ProductDTOBuilder.builder().build().toProductDTO();

        // when
        when(productRepository.findByName(expectedFoundProductDTO.getName())).thenReturn(Optional.empty());

        // then
        assertThrows(ProductNotFoundException.class, () -> productService.findByName(expectedFoundProductDTO.getName()));
    }

    //Find All
    @Test
    void whenListProductsIsCalledThenReturnAListOfProducts() {
        // given
        ProductDTO expectedFoundProductDTO = ProductDTOBuilder.builder().build().toProductDTO();
        Product expectedFoundProduct = productMapper.toModel(expectedFoundProductDTO);

        //when
        when(productRepository.findAll()).thenReturn(Collections.singletonList(expectedFoundProduct));

        //then
        List<ProductDTO> foundListProductDTO = productService.listAll();

        assertThat(foundListProductDTO, is(not(empty())));
        assertThat(foundListProductDTO.get(0), is(equalTo(expectedFoundProductDTO)));
    }

    @Test
    void whenAListIsCalledWithoutProductThenReturnAnEmptyList() {
        //when
        when(productRepository.findAll()).thenReturn(Collections.EMPTY_LIST);

        //then
        List<ProductDTO> foundListProductsDTO = productService.listAll();

        assertThat(foundListProductsDTO, is(empty()));
    }

    //Delete By Id
    @Test
    void whenExclusionIsCalledWithValidIdThenAProductShouldBeDeleted() throws ProductNotFoundException {
        // given
        ProductDTO expectedDeletedProductDTO = ProductDTOBuilder.builder().build().toProductDTO();
        Product expectedDeletedProduct = productMapper.toModel(expectedDeletedProductDTO);

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
        ProductDTO expectedUpdatedProductDTO = ProductDTOBuilder.builder().build().toProductDTO();
        Product expectedUpdatedProduct = productMapper.toModel(expectedUpdatedProductDTO);

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
        ProductDTO expectedUpdatedProductDTO = ProductDTOBuilder.builder().build().toProductDTO();

        //when
        when(productRepository.findById(INVALID_PRODUCT_ID)).thenReturn(Optional.empty());

        //then
        assertThrows(ProductNotFoundException.class, () -> productService.updateById(INVALID_PRODUCT_ID,expectedUpdatedProductDTO));
    }

    //Increment
    @Test
    void whenIncrementIsCalledThenIncrementProductStock() throws ProductNotFoundException, ProductStockExceededException {
        //given
        ProductDTO expectedProductDTO = ProductDTOBuilder.builder().build().toProductDTO();
        Product expectedProduct = productMapper.toModel(expectedProductDTO);

        //when
        when(productRepository.findById(expectedProductDTO.getId())).thenReturn(Optional.of(expectedProduct));
        when(productRepository.save(expectedProduct)).thenReturn(expectedProduct);

        int quantityToIncrement = 89;
        int expectedQuantityAfterIncrement = expectedProductDTO.getQuantity() + quantityToIncrement;

        // then
        ProductDTO incrementedProductDTO = productService.increment(expectedProductDTO.getId(), quantityToIncrement);

        assertThat(expectedQuantityAfterIncrement, equalTo(incrementedProductDTO.getQuantity()));
        assertThat(expectedQuantityAfterIncrement, lessThan(expectedProductDTO.getMaxQuantity()));
    }

    @Test
    void whenIncrementAfterSumIsEqualsTheMaxQuantityThenIncrementProductStock() throws ProductNotFoundException, ProductStockExceededException {
        //given
        ProductDTO expectedProductDTO = ProductDTOBuilder.builder().build().toProductDTO();
        Product expectedProduct = productMapper.toModel(expectedProductDTO);

        //when
        when(productRepository.findById(expectedProductDTO.getId())).thenReturn(Optional.of(expectedProduct));
        when(productRepository.save(expectedProduct)).thenReturn(expectedProduct);

        int quantityToIncrement = 90;
        int expectedQuantityAfterIncrement = expectedProductDTO.getQuantity() + quantityToIncrement;

        // then
        ProductDTO incrementedProductDTO = productService.increment(expectedProductDTO.getId(), quantityToIncrement);

        assertThat(expectedQuantityAfterIncrement, equalTo(incrementedProductDTO.getQuantity()));
        assertThat(expectedQuantityAfterIncrement, equalTo(expectedProductDTO.getMaxQuantity()));
    }

    @Test
    void whenIncrementAfterSumIsGreatherThanMaxThenThrowException() {
        ProductDTO expectedProductDTO = ProductDTOBuilder.builder().build().toProductDTO();
        Product expectedProduct = productMapper.toModel(expectedProductDTO);

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
        ProductDTO expectedProductDTO = ProductDTOBuilder.builder().build().toProductDTO();
        Product expectedProduct = productMapper.toModel(expectedProductDTO);

        when(productRepository.findById(expectedProductDTO.getId())).thenReturn(Optional.of(expectedProduct));
        when(productRepository.save(expectedProduct)).thenReturn(expectedProduct);

        int quantityToDecrement = 9;
        int expectedQuantityAfterDecrement = expectedProductDTO.getQuantity() - quantityToDecrement;
        ProductDTO decrementedProductDTO = productService.decrement(expectedProductDTO.getId(), quantityToDecrement);

        assertThat(expectedQuantityAfterDecrement, equalTo(decrementedProductDTO.getQuantity()));
        assertThat(expectedQuantityAfterDecrement, greaterThan(0));
    }

    @Test
    void whenDecrementIsCalledToEmptyStockThenEmptyProductStock() throws ProductStockUnderThanZeroException, ProductNotFoundException {
        ProductDTO expectedProductDTO = ProductDTOBuilder.builder().build().toProductDTO();
        Product expectedProduct = productMapper.toModel(expectedProductDTO);

        when(productRepository.findById(expectedProductDTO.getId())).thenReturn(Optional.of(expectedProduct));
        when(productRepository.save(expectedProduct)).thenReturn(expectedProduct);

        int quantityToDecrement = 10;
        int expectedQuantityAfterDecrement = expectedProductDTO.getQuantity() - quantityToDecrement;
        ProductDTO incrementedProductDTO = productService.decrement(expectedProductDTO.getId(), quantityToDecrement);

        assertThat(expectedQuantityAfterDecrement, equalTo(0));
        assertThat(expectedQuantityAfterDecrement, equalTo(incrementedProductDTO.getQuantity()));
    }

    @Test
    void whenDecrementIsLowerThanZeroThenThrowException() {
        ProductDTO expectedProductDTO = ProductDTOBuilder.builder().build().toProductDTO();
        Product expectedProduct = productMapper.toModel(expectedProductDTO);

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
