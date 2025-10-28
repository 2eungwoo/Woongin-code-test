package com.wjc.codetest.product.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.wjc.codetest.product.controller.dto.request.CreateProductRequest;
import com.wjc.codetest.product.controller.dto.request.GetProductListRequest;
import com.wjc.codetest.product.controller.dto.request.UpdateProductRequest;
import com.wjc.codetest.product.controller.dto.response.ProductListResponse;
import com.wjc.codetest.product.controller.dto.response.ProductResponse;
import com.wjc.codetest.product.model.domain.Product;
import com.wjc.codetest.product.repository.ProductRepository;
import com.wjc.codetest.product.service.validator.ProductValidator;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductValidator productValidator;

    private Product exampleProduct;
    private Long proudctId = 1L;

    @BeforeEach
    void setUp() {
        exampleProduct = Product.createProduct("빵", "소금빵");
    }

    @Test
    @DisplayName("proudct 생성 성공")
    void create_product_success() {
        // given
        CreateProductRequest requestDto = new CreateProductRequest("빵", "소금빵");
        given(productRepository.save(any(Product.class))).willReturn(exampleProduct);

        // when
        ProductResponse responseDto = productService.createProduct(requestDto);

        // then
        assertThat(responseDto.category()).isEqualTo("빵");
        assertThat(responseDto.name()).isEqualTo("소금빵");
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("product 단건 조회 성공")
    void get_product_byid_success() {
        // given
        given(productValidator.validateExistOrThrow(proudctId)).willReturn(exampleProduct);

        // when
        ProductResponse responseDto = productService.getProductById(proudctId);

        // then
        assertThat(responseDto.category()).isEqualTo("빵");
        assertThat(responseDto.name()).isEqualTo("소금빵");
        verify(productValidator, times(1)).validateExistOrThrow(proudctId);
    }

    @Test
    @DisplayName("product 수정 성공")
    void update_product_success() {
        // given
        UpdateProductRequest requestDto = new UpdateProductRequest("빵", "크로아상");
        given(productValidator.validateExistOrThrow(proudctId)).willReturn(exampleProduct);

        // when
        ProductResponse responseDto = productService.updateProductById(proudctId, requestDto);

        // then
        assertThat(responseDto.category()).isEqualTo("빵");
        assertThat(responseDto.name()).isEqualTo("크로아상");
        verify(productValidator, times(1)).validateExistOrThrow(proudctId);
    }

    @Test
    @DisplayName("proudct 삭제 성공")
    void delete_product_success() {
        // given
        given(productValidator.validateExistOrThrow(proudctId)).willReturn(exampleProduct);

        // when
        ProductResponse responseDto = productService.deleteProductById(proudctId);

        // then
        assertThat(responseDto.category()).isEqualTo("빵");
        assertThat(responseDto.name()).isEqualTo("소금빵");
        verify(productValidator, times(1)).validateExistOrThrow(proudctId);
        verify(productRepository, times(1)).delete(any(Product.class));
    }

    @Test
    @DisplayName("카테고리별 product list 조회 성공")
    void get_product_list_by_category_success() {
        // given
        GetProductListRequest dto = new GetProductListRequest("음료", 0, 10);
        Page<Product> mockPage = new PageImpl<>(List.of(exampleProduct), PageRequest.of(0, 10), 1);
        given(productRepository.findAllByCategory(any(), any())).willReturn(mockPage);

        // when
        ProductListResponse responseListDto = productService.getProductListByCategory(dto);

        // then
        assertThat(responseListDto.products()).hasSize(1);
        assertThat(responseListDto.products().get(0).category()).isEqualTo("빵");
        assertThat(responseListDto.products().get(0).name()).isEqualTo("소금빵");
        verify(productRepository, times(1)).findAllByCategory(any(), any());
    }

    @Test
    @DisplayName("distinct 카테고리 목록 조회 성공")
    void get_distinct_categories_success() {
        // given
        List<String> exmapleCategoryList = List.of("음료","디저트","음료");
        given(productRepository.findDistinctCategories()).willReturn(exmapleCategoryList);

        // when
        List<String> responseListDto = productService.getUniqueCategories();

        // then
        assertThat(responseListDto).hasSize(2);
        verify(productRepository, times(1)).findDistinctCategories();
    }
}