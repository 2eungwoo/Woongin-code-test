package com.wjc.codetest.product.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.wjc.codetest.product.controller.dto.request.CreateProductRequest;
import com.wjc.codetest.product.controller.dto.response.ProductResponse;
import com.wjc.codetest.product.model.domain.Product;
import com.wjc.codetest.product.repository.ProductRepository;
import com.wjc.codetest.product.service.validator.ProductValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductValidator productValidator;

    @Test
    @DisplayName("신규 상품 생성 테스트")
    void 신규_상품_생성() {
        // given
        CreateProductRequest request = new CreateProductRequest("상의", "반팔티");
        Product product = Product.createProduct(request.category(), request.name());

        when(productRepository.save(any(Product.class))).thenReturn(product);

        // when
        ProductResponse response = productService.createProduct(request);

        // then
        assertEquals(request.category(), response.category());
        assertEquals(request.name(), response.name());
        verify(productRepository).save(any(Product.class));
    }
}
