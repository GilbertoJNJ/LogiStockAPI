package com.gilberto.logistockapi.controllers;

import com.gilberto.logistockapi.builder.ProductDTOBuilder;
import com.gilberto.logistockapi.models.dto.request.ProductForm;
import com.gilberto.logistockapi.models.dto.request.QuantityForm;
import com.gilberto.logistockapi.exceptions.ProductNotFoundException;
import com.gilberto.logistockapi.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Collections;

import static com.gilberto.logistockapi.utils.JsonConvertionUtils.asJsonString;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    private static final String PRODUCT_API_URL_PATH = "/api/v1/products";
    private static final long VALID_PRODUCT_ID = 1L;
    private static final long INVALID_PRODUCT_ID = 2L;
    private static final String PRODUCT_API_SUBPATH_INCREMENT_URL = "/increment";
    private static final String PRODUCT_API_SUBPATH_DECREMENT_URL = "/decrement";

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    //POST
    @Test
    void whenPOSTIsCalledThenAProductIsCreated() throws Exception {
        // given
        ProductForm productDTO = ProductDTOBuilder.builder().build().toProductDTO();

        // when
//        when(productService.createProduct(productDTO)).thenReturn(MessageResponseDTO
//                .builder()
//                .build());

        // then
        mockMvc.perform(post(PRODUCT_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(productDTO)))
                .andExpect(status().isCreated());
                //.andExpect(jsonPath("$.name", is(productDTO.getName())));
                //.andExpect(jsonPath("$.buyPrice", is(productDTO.getBuyPrice())))
                //.andExpect(jsonPath("$.sellPrice", is(productDTO.getSellPrice())));
    }

    @Test
    void whenPOSTIsCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        // given
        ProductForm productDTO = ProductDTOBuilder.builder().build().toProductDTO();
        productDTO.setName(null);

        // then
        mockMvc.perform(post(PRODUCT_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(productDTO)))
                .andExpect(status().isBadRequest());
    }

    //PUT
    @Test
    void whenPUTIsCalledThenAProductIsUpdated() throws Exception {
        // given
        ProductForm productDTO = ProductDTOBuilder.builder().build().toProductDTO();

        // when
//        when(productService.updateById(VALID_PRODUCT_ID,productDTO)).thenReturn(MessageResponseDTO
//                .builder()
//                .build());

        // then
        mockMvc.perform(put(PRODUCT_API_URL_PATH + "/" + VALID_PRODUCT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(productDTO)))
                .andExpect(status().isOk());
        //.andExpect(jsonPath("$.name", is(productDTO.getName())));
        //.andExpect(jsonPath("$.buyPrice", is(productDTO.getBuyPrice())))
        //.andExpect(jsonPath("$.sellPrice", is(productDTO.getSellPrice())));
    }

    @Test
    void whenPUTIsCalledWithInvalidIdThenAnErrorIsReturned() throws Exception {
        // given
        ProductForm productDTO = ProductDTOBuilder.builder().build().toProductDTO();

        //when
        doThrow(ProductNotFoundException.class).when(productService).updateById(INVALID_PRODUCT_ID,productDTO);


        // then
        mockMvc.perform(put(PRODUCT_API_URL_PATH + "/" + INVALID_PRODUCT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(productDTO)))
                .andExpect(status().isNotFound());

    }

    //GET Name
    @Test
    void whenGETIsCalledWithValidNameThenOkStatusIsReturned() throws Exception {
        // given
        ProductForm productDTO = ProductDTOBuilder.builder().build().toProductDTO();

        //when
//        when(productService.findByBarCode(productDTO.getName())).thenReturn(productDTO);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(PRODUCT_API_URL_PATH + "/" + productDTO.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is(productDTO.getName())))
        .andExpect(jsonPath("$.buyPrice", is(productDTO.getUnitPrice())));
//        .andExpect(jsonPath("$.sellPrice", is(productDTO.getSellPrice())));
    }

    @Test
    void whenGETIsCalledWithoutRegisteredNameThenNotFoundStatusIsReturned() throws Exception {
        // given
        ProductForm productDTO = ProductDTOBuilder.builder().build().toProductDTO();

        //when
        when(productService.findByBarCode(productDTO.getName())).thenThrow(ProductNotFoundException.class);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(PRODUCT_API_URL_PATH + "/" + productDTO.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    //GET ALL
    @Test
    void whenGETListWithProductsIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        ProductForm productDTO = ProductDTOBuilder.builder().build().toProductDTO();

        //when
//        when(productService.listAll()).thenReturn(Collections.singletonList(productDTO));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(PRODUCT_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name", is(productDTO.getName())))
        .andExpect(jsonPath("$[0].buyPrice", is(productDTO.getUnitPrice())));
//        .andExpect(jsonPath("$[0].sellPrice", is(productDTO.getSellPrice())));
    }

    @Test
    void whenGETListWithoutProductsIsCalledThenOkStatusIsReturned() throws Exception {

        //when
        when(productService.listAll()).thenReturn(Collections.emptyList());

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(PRODUCT_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    //DELETE
    @Test
    void whenDELETEIsCalledWithValidIdThenNoContentStatusIsReturned() throws Exception {
        // given
        ProductForm productDTO = ProductDTOBuilder.builder().build().toProductDTO();

        //when
//        when(productService.delete(productDTO.getId())).thenReturn(MessageResponseDTO
//                .builder()
//                .build());

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(PRODUCT_API_URL_PATH + "/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenDELETEIsCalledWithInvalidIdThenNotFoundStatusIsReturned() throws Exception {
        //when
        doThrow(ProductNotFoundException.class).when(productService).delete(INVALID_PRODUCT_ID);

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(PRODUCT_API_URL_PATH + "/" + INVALID_PRODUCT_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    //PATCH
    @Test
    void whenPATCHIsCalledToIncrementDiscountThenOStatusIsReturned() throws Exception {
        QuantityForm quantityDTO = QuantityForm.builder()
                .quantity(10)
                .build();

        ProductForm productDTO = ProductDTOBuilder.builder().build().toProductDTO();
//        productDTO.setQuantity(productDTO.getQuantity() + quantityDTO.getQuantity());

//        when(productService.increment(VALID_PRODUCT_ID, quantityDTO.getQuantity())).thenReturn(productDTO);

        mockMvc.perform(MockMvcRequestBuilders.patch(PRODUCT_API_URL_PATH + "/" + VALID_PRODUCT_ID + PRODUCT_API_SUBPATH_INCREMENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(quantityDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(productDTO.getName())));
//                .andExpect(jsonPath("$.quantity", is(productDTO.getQuantity())));
    }

    @Test
    void whenPATCHIsCalledToIncrementGreatherThanMaxThenBadRequestStatusIsReturned() throws Exception {
        QuantityForm quantityDTO = QuantityForm.builder()
                .quantity(91)
                .build();

        ProductForm productDTO = ProductDTOBuilder.builder().build().toProductDTO();
//        productDTO.setQuantity(productDTO.getQuantity() + quantityDTO.getQuantity());

//        when(productService.increment(VALID_PRODUCT_ID, quantityDTO.getQuantity())).thenThrow(ProductStockExceededException.class);

        mockMvc.perform(MockMvcRequestBuilders.patch(PRODUCT_API_URL_PATH + "/" + VALID_PRODUCT_ID + PRODUCT_API_SUBPATH_INCREMENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(quantityDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenPATCHIsCalledWithInvalidProductIdToIncrementThenNotFoundStatusIsReturned() throws Exception {
        QuantityForm quantityDTO = QuantityForm.builder()
                .quantity(30)
                .build();

//        when(productService.increment(INVALID_PRODUCT_ID, quantityDTO.getQuantity())).thenThrow(ProductNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.patch(PRODUCT_API_URL_PATH + "/" + INVALID_PRODUCT_ID + PRODUCT_API_SUBPATH_INCREMENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(quantityDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenPATCHIsCalledToDecrementDiscountThenOKstatusIsReturned() throws Exception {
        QuantityForm quantityDTO = QuantityForm.builder()
                .quantity(9)
                .build();

        ProductForm productDTO = ProductDTOBuilder.builder().build().toProductDTO();
//        productDTO.setQuantity(productDTO.getQuantity() - quantityDTO.getQuantity());

//        when(productService.decrement(VALID_PRODUCT_ID, quantityDTO.getQuantity())).thenReturn(productDTO);

        mockMvc.perform(MockMvcRequestBuilders.patch(PRODUCT_API_URL_PATH + "/" + VALID_PRODUCT_ID + PRODUCT_API_SUBPATH_DECREMENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(quantityDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(productDTO.getName())));
//                .andExpect(jsonPath("$.quantity", is(productDTO.getQuantity())));
    }

    @Test
    void whenPATCHIsCalledToDEcrementLowerThanZeroThenBadRequestStatusIsReturned() throws Exception {
        QuantityForm quantityDTO = QuantityForm.builder()
                .quantity(11)
                .build();

        ProductForm productDTO = ProductDTOBuilder.builder().build().toProductDTO();
//        productDTO.setQuantity(productDTO.getQuantity() - quantityDTO.getQuantity());

//        when(productService.decrement(VALID_PRODUCT_ID, quantityDTO.getQuantity())).thenThrow(ProductStockUnderThanZeroException.class);

        mockMvc.perform(MockMvcRequestBuilders.patch(PRODUCT_API_URL_PATH + "/" + VALID_PRODUCT_ID + PRODUCT_API_SUBPATH_DECREMENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(quantityDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenPATCHIsCalledWithInvalidProductIdToDecrementThenNotFoundStatusIsReturned() throws Exception {
        QuantityForm quantityDTO = QuantityForm.builder()
                .quantity(5)
                .build();

//        when(productService.decrement(INVALID_PRODUCT_ID, quantityDTO.getQuantity())).thenThrow(ProductNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.patch(PRODUCT_API_URL_PATH + "/" + INVALID_PRODUCT_ID + PRODUCT_API_SUBPATH_DECREMENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(quantityDTO)))
                .andExpect(status().isNotFound());
    }
}
