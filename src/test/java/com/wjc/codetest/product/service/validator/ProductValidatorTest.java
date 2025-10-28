package com.wjc.codetest.product.service.validator;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.wjc.codetest.product.repository.ProductRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductValidatorTest {

    @InjectMocks
    private ProductValidator productValidator;

    @Mock
    private ProductRepository productRepository;

    @Test
    @DisplayName("findbyid 실패하면 ProductNotFoundException으로 예외가 잡힌다.")
    void product_not_found_exception_test() {
        // given
        Long productId = 1L;
        given(productRepository.findById(productId)).willReturn(Optional.empty());

        // when + then
        assertThatThrownBy(() -> productValidator.validateExistOrThrow(productId))
            .isInstanceOf(ProductNotFoundException.class)
            .hasMessageContaining("해당 프로덕트를 찾을 수 없습니다 ID :: " + productId);
    }
}