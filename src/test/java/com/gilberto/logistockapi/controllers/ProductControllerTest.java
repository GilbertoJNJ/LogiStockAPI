package com.gilberto.logistockapi.controllers;

import com.gilberto.logistockapi.exceptions.ProductAlreadyRegisteredException;
import com.gilberto.logistockapi.exceptions.ProductStockExceededException;
import com.gilberto.logistockapi.exceptions.ProductStockUnderThanZeroException;
import com.gilberto.logistockapi.models.dto.request.ProductForm;
import com.gilberto.logistockapi.models.dto.request.ProductUpdateForm;
import com.gilberto.logistockapi.models.dto.request.QuantityForm;
import com.gilberto.logistockapi.exceptions.ProductNotFoundException;
import com.gilberto.logistockapi.services.implementations.ProductService;
import com.gilberto.logistockapi.utils.ModelUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import java.util.Collections;
import static com.gilberto.logistockapi.utils.JsonConvertionUtils.asJsonString;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {
  
  private static final long   PRODUCT_ID                       = 1L;
  private static final long   INVALID_PRODUCT_ID               = 2L;
  private static final String PRODUCT_API_URL_PATH             = "/api/v1/product";
  private static final String PRODUCT_API_SUBPATH_INCREASE_URL = "/increase";
  private static final String PRODUCT_API_SUBPATH_DECREASE_URL = "/decrease";
  private static final String PRODUCT_API_SUBPATH_BAR_CODE     = "/barcode";
  public static final  String QUERY_PARAMS                     = "?pageNumber=1&pageSize=10&" +
      "search=search&categories=ELECTRONIC, CLOTHING, FOOD, OTHER";
  
  private MockMvc mockMvc;
  
  @Mock
  private ProductService productService;
  
  @InjectMocks
  private ProductController productController;
  
  @BeforeEach
  void setUp() {
    this.mockMvc = MockMvcBuilders.standaloneSetup(productController)
        .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
        .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
        .build();
  }
  
  // POST
  @Test
  void whenPOSTIsCalledThenAProductMustBeCreated() throws Exception {
    // given
    var productForm = ModelUtils.getProductForm();
    var productDTO  = ModelUtils.getProductDTO();
    
    // when
    when(this.productService.create(productForm))
        .thenReturn(productDTO);
    
    // then
    this.mockMvc.perform(post(PRODUCT_API_URL_PATH)
            .contentType(APPLICATION_JSON)
            .content(asJsonString(productForm)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(productDTO.id().intValue())))
        .andExpect(jsonPath("$.name", is(productDTO.name())))
        .andExpect(jsonPath("$.barCode", is(productDTO.barCode())));
  }
  
  @Test
  void whenPOSTIsCalledWithoutRequiredFieldThenBadRequestStatusMustBeReturned() throws Exception {
    // given
    var productForm = new ProductForm(null, null, null, null,
        null, null, null, null, null);
    
    // then
    this.mockMvc.perform(post(PRODUCT_API_URL_PATH)
            .contentType(APPLICATION_JSON)
            .content(asJsonString(productForm)))
        .andExpect(status().isBadRequest());
  }
  
  @Test
  void whenPOSTIsCalledToCreateAnAlreadyRegisteredProductThenBadRequestStatusMustBeReturned() throws Exception {
    // given
    var productForm = ModelUtils.getProductForm();
    
    // when
    when(this.productService.create(productForm))
        .thenThrow(ProductAlreadyRegisteredException.class);
    
    // then
    this.mockMvc.perform(post(PRODUCT_API_URL_PATH)
            .contentType(APPLICATION_JSON)
            .content(asJsonString(productForm)))
        .andExpect(status().isBadRequest());
  }
  
  // PUT
  @Test
  void whenPUTIsCalledThenAProductMustBeUpdated() throws Exception {
    // given
    var productUpdateForm = ModelUtils.getProductUpdateForm();
    var productDTO        = ModelUtils.getProductDTO();
    
    // when
    when(this.productService.updateById(PRODUCT_ID, productUpdateForm))
        .thenReturn(productDTO);
    
    // then
    this.mockMvc.perform(put(PRODUCT_API_URL_PATH + "/" + PRODUCT_ID)
            .contentType(APPLICATION_JSON)
            .content(asJsonString(productUpdateForm)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(productDTO.id().intValue())))
        .andExpect(jsonPath("$.name", is(productDTO.name())))
        .andExpect(jsonPath("$.barCode", is(productDTO.barCode())));
  }
  
  @Test
  void whenPUTIsCalledWithoutRequiredFieldThenBadRequestStatusMustBeReturned() throws Exception {
    // given
    var productUpdateForm = new ProductUpdateForm(null, null, null,
        null, null, null, null);
    
    
    // then
    this.mockMvc.perform(put(PRODUCT_API_URL_PATH + "/" + PRODUCT_ID)
            .contentType(APPLICATION_JSON)
            .content(asJsonString(productUpdateForm)))
        .andExpect(status().isBadRequest());
    
  }
  
  @Test
  void whenPUTIsCalledWithInvalidIdThenNotFoundStatusMustBeReturned() throws Exception {
    // given
    var productUpdateForm = ModelUtils.getProductUpdateForm();
    
    // when
    doThrow(ProductNotFoundException.class)
        .when(this.productService).updateById(INVALID_PRODUCT_ID, productUpdateForm);
    
    
    // then
    this.mockMvc.perform(put(PRODUCT_API_URL_PATH + "/" + INVALID_PRODUCT_ID)
            .contentType(APPLICATION_JSON)
            .content(asJsonString(productUpdateForm)))
        .andExpect(status().isNotFound());
    
  }
  
  // GET BarCode
  @Test
  void whenGETIsCalledWithValidBarCodeThenAProductMustBeReturned() throws Exception {
    // given
    var productDTO = ModelUtils.getProductDTO();
    
    // when
    when(this.productService.findByBarCode(productDTO.barCode()))
        .thenReturn(productDTO);
    
    // then
    this.mockMvc.perform(get(PRODUCT_API_URL_PATH + PRODUCT_API_SUBPATH_BAR_CODE + "/" + productDTO.barCode())
            .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(productDTO.id().intValue())))
        .andExpect(jsonPath("$.barCode", is(productDTO.barCode())))
        .andExpect(jsonPath("$.name", is(productDTO.name())));
  }
  
  @Test
  void whenGETIsCalledWithoutRegisteredBarCodeThenNotFoundStatusMustBeReturned() throws Exception {
    // given
    var productDTO = ModelUtils.getProductDTO();
    
    // when
    when(this.productService.findByBarCode(productDTO.barCode()))
        .thenThrow(ProductNotFoundException.class);
    
    // then
    this.mockMvc.perform(get(PRODUCT_API_URL_PATH + PRODUCT_API_SUBPATH_BAR_CODE + "/" + productDTO.barCode())
            .contentType(APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }
  
  // GET Id
  @Test
  void whenGETIsCalledWithValidIdThenAProductMustBeReturned() throws Exception {
    // given
    var productDTO = ModelUtils.getProductDTO();
    
    // when
    when(this.productService.findById(PRODUCT_ID))
        .thenReturn(productDTO);
    
    // then
    this.mockMvc.perform(get(PRODUCT_API_URL_PATH + "/" + PRODUCT_ID)
            .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(productDTO.id().intValue())))
        .andExpect(jsonPath("$.barCode", is(productDTO.barCode())))
        .andExpect(jsonPath("$.name", is(productDTO.name())));
  }
  
  @Test
  void whenGETIsCalledWithInvalidIdThenNotFoundStatusMustBeReturned() throws Exception {
    // when
    when(this.productService.findById(INVALID_PRODUCT_ID))
        .thenThrow(ProductNotFoundException.class);
    
    // then
    this.mockMvc.perform(get(PRODUCT_API_URL_PATH + "/" + INVALID_PRODUCT_ID)
            .contentType(APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }
  
  // GET ALL
  @Test
  void whenGETIsCalledToListProductsThenAProductListMustBeReturned() throws Exception {
    // given
    var productFilter = ModelUtils.getProductFilter();
    var productDTO    = ModelUtils.getProductDTO();
    
    // when
    when(this.productService.listAll(productFilter))
        .thenReturn(Collections.singletonList(productDTO));
    
    // then
    this.mockMvc.perform(get(PRODUCT_API_URL_PATH + QUERY_PARAMS)
            .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id", is(productDTO.id().intValue())))
        .andExpect(jsonPath("$[0].barCode", is(productDTO.barCode())))
        .andExpect(jsonPath("$[0].name", is(productDTO.name())));
  }
  
  @Test
  void whenGETIsCalledToListProductsThenAProductEmptyListMustBeReturned() throws Exception {
    // given
    var productFilter = ModelUtils.getProductFilter();
    
    // when
    when(productService.listAll(productFilter))
        .thenReturn(Collections.emptyList());
    
    // then
    this.mockMvc.perform(get(PRODUCT_API_URL_PATH + QUERY_PARAMS)
            .contentType(APPLICATION_JSON))
        .andExpect(status().isOk());
  }
  
  // DELETE
  @Test
  void whenDELETEIsCalledWithValidIdThenNoContentStatusMustBeReturned() throws Exception {
    // when
    doNothing().when(this.productService).delete(PRODUCT_ID);
    
    // then
    this.mockMvc.perform(delete(PRODUCT_API_URL_PATH + "/" + PRODUCT_ID)
            .contentType(APPLICATION_JSON))
        .andExpect(status().isNoContent());
  }
  
  @Test
  void whenDELETEIsCalledWithInvalidIdThenNotFoundStatusMustBeReturned() throws Exception {
    // when
    doThrow(ProductNotFoundException.class)
        .when(this.productService).delete(INVALID_PRODUCT_ID);
    
    // then
    this.mockMvc.perform(delete(PRODUCT_API_URL_PATH + "/" + INVALID_PRODUCT_ID)
            .contentType(APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }
  
  // PATCH Increase
  @Test
  void whenPATCHIsCalledToIncreaseStockThenOkStatusMustBeReturned() throws Exception {
    // given
    var quantityForm = new QuantityForm(10);
    var productDTO   = ModelUtils.getProductDTO();
    
    // when
    when(this.productService.increaseStock(PRODUCT_ID, quantityForm))
        .thenReturn(productDTO);
    
    // then
    this.mockMvc.perform(patch(PRODUCT_API_URL_PATH + "/" + PRODUCT_ID + PRODUCT_API_SUBPATH_INCREASE_URL)
            .contentType(APPLICATION_JSON)
            .content(asJsonString(quantityForm)))
        .andExpect(status().isOk());
  }
  
  @Test
  void whenPATCHIsCalledToIncreaseStockWithoutRequiredFieldThenBadRequestStatusMustBeReturned() throws Exception {
    // given
    var quantityForm = new QuantityForm(null);
    
    // then
    this.mockMvc.perform(patch(PRODUCT_API_URL_PATH + "/" + PRODUCT_ID + PRODUCT_API_SUBPATH_INCREASE_URL)
            .contentType(APPLICATION_JSON)
            .content(asJsonString(quantityForm)))
        .andExpect(status().isBadRequest());
  }
  
  @Test
  void whenPATCHIsCalledToIncreaseStockBeyondTheMaximumLimitThenBadRequestStatusMustBeReturned() throws Exception {
    // given
    var quantityForm = new QuantityForm(91);
    
    // when
    when(this.productService.increaseStock(PRODUCT_ID, quantityForm))
        .thenThrow(ProductStockExceededException.class);
    
    // then
    this.mockMvc.perform(patch(PRODUCT_API_URL_PATH + "/" + PRODUCT_ID + PRODUCT_API_SUBPATH_INCREASE_URL)
            .contentType(APPLICATION_JSON)
            .content(asJsonString(quantityForm)))
        .andExpect(status().isBadRequest());
  }
  
  @Test
  void whenPATCHIsCalledWithInvalidProductIdToIncreaseStockThenNotFoundStatusMustBeReturned() throws Exception {
    // given
    var quantityForm = new QuantityForm(30);
    
    // when
    when(this.productService.increaseStock(INVALID_PRODUCT_ID, quantityForm))
        .thenThrow(ProductNotFoundException.class);
    
    // then
    this.mockMvc.perform(patch(PRODUCT_API_URL_PATH + "/" + INVALID_PRODUCT_ID + PRODUCT_API_SUBPATH_INCREASE_URL)
            .contentType(APPLICATION_JSON)
            .content(asJsonString(quantityForm)))
        .andExpect(status().isNotFound());
  }
  
  // PATCH Decrease
  @Test
  void whenPATCHIsCalledToDecreaseStockThenOKStatusMustBeReturned() throws Exception {
    // given
    var quantityForm = new QuantityForm(9);
    var productDTO   = ModelUtils.getProductDTO();
    
    // when
    when(this.productService.decreaseStock(PRODUCT_ID, quantityForm))
        .thenReturn(productDTO);
    
    // then
    this.mockMvc.perform(patch(PRODUCT_API_URL_PATH + "/" + PRODUCT_ID + PRODUCT_API_SUBPATH_DECREASE_URL)
            .contentType(APPLICATION_JSON)
            .content(asJsonString(quantityForm)))
        .andExpect(status().isOk());
  }
  
  @Test
  void whenPATCHIsCalledToDecreaseStockWithoutRequiredFieldThenBadRequestStatusMustBeReturned() throws Exception {
    // given
    var quantityForm = new QuantityForm(null);
    
    // then
    this.mockMvc.perform(patch(PRODUCT_API_URL_PATH + "/" + PRODUCT_ID + PRODUCT_API_SUBPATH_DECREASE_URL)
            .contentType(APPLICATION_JSON)
            .content(asJsonString(quantityForm)))
        .andExpect(status().isBadRequest());
  }
  
  @Test
  void whenPATCHIsCalledToLowerTheStockBelowZeroThenBadRequestStatusMustBeReturned() throws Exception {
    // given
    var quantityForm = new QuantityForm(11);
    
    // when
    when(this.productService.decreaseStock(PRODUCT_ID, quantityForm))
        .thenThrow(ProductStockUnderThanZeroException.class);
    
    // then
    this.mockMvc.perform(patch(PRODUCT_API_URL_PATH + "/" + PRODUCT_ID + PRODUCT_API_SUBPATH_DECREASE_URL)
            .contentType(APPLICATION_JSON)
            .content(asJsonString(quantityForm)))
        .andExpect(status().isBadRequest());
  }
  
  @Test
  void whenPATCHIsCalledWithInvalidProductIdToDecreaseStockThenNotFoundStatusMustBeReturned() throws Exception {
    // given
    var quantityForm = new QuantityForm(5);
    
    // when
    when(this.productService.decreaseStock(INVALID_PRODUCT_ID, quantityForm))
        .thenThrow(ProductNotFoundException.class);
    
    // then
    this.mockMvc.perform(patch(PRODUCT_API_URL_PATH + "/" + INVALID_PRODUCT_ID + PRODUCT_API_SUBPATH_DECREASE_URL)
            .contentType(APPLICATION_JSON)
            .content(asJsonString(quantityForm)))
        .andExpect(status().isNotFound());
  }
  
}
