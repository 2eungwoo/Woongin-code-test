package com.wjc.codetest.product.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.wjc.codetest.product.model.domain.Product;
import com.wjc.codetest.product.repository.ProductRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DistinctCategoriesTest {

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.save(Product.createProduct("빵", "식빵"));
        productRepository.save(Product.createProduct("빵", "크로아상"));
        productRepository.save(Product.createProduct("음료", "커피"));
    }

    @AfterEach
    void setDown() {
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("JPQL에 작성된 쿼리가 실제로 잘 동작한다(distinct로 카테고리 중복 제외)")
    void distinct_query_success() {
        // when
        List<String> categories = productRepository.findDistinctCategories();

        // then
        // ㅇ -> ㅃ 순
        assertThat(categories)
            .hasSize(2)
            .containsExactlyInAnyOrder("음료", "빵");
    }
}
