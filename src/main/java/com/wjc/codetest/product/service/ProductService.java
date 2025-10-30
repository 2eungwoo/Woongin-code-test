package com.wjc.codetest.product.service;

import com.wjc.codetest.product.model.request.CreateProductRequest;
import com.wjc.codetest.product.model.request.GetProductListRequest;
import com.wjc.codetest.product.model.domain.Product;
import com.wjc.codetest.product.model.request.UpdateProductRequest;
import com.wjc.codetest.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
/*
    ps. 해당 클래스의 모든 메소드에 대한 공통 의견만 이 곳에 코멘트합니다.
        이후 각 메소드에는 메소드에만 국한되는 코멘트를 적었습니다.

    [문제 및 의견]
        1. (그냥 의견)코드 스타일 - 통일되지 못한 코드 형태
           현재 각 메소드마다 기능과 목적에 따라 코드가 달라질 수 있음을 감안하더라도,
           어떤 것은 인스턴스 변수명으로 리턴하고, 어떤 것은 inline으로 축약해서 리턴하는 등
           전반적으로 통일성이 없는데, 팀 내 컨벤션에 따라 통일하면 어떨까 싶습니다.

           현재는 단순 crud 형태이므로 단순한 로직에 의해
           예외 처리나 기타 비즈니스 로직 등 구현체가 단순하므로 문제가 두드러지지 않기 떄문에
           '그냥 의견' 이라고 적어놨지만 꽤 중요한 사항이라고 생각합니다.

        2. Entity 객체 리턴 --> controller layer로 넘겨주고 있는 형태
           먼저 이는 전반적인 crud 구현에 있어 충분한 이해가 없는 것으로 판단됩니다.
           1) jpa 영속 개념과 트랜잭션 범위에 대한 이해
           2) entity 객체



 */
public class ProductService {

    private final ProductRepository productRepository;

    public Product create(CreateProductRequest dto) {
        Product product = new Product(dto.getCategory(), dto.getName());
        return productRepository.save(product);
    }

    public Product getProductById(Long productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (!productOptional.isPresent()) {
            throw new RuntimeException("product not found");
        }
        return productOptional.get();
    }

    public Product update(UpdateProductRequest dto) {
        Product product = getProductById(dto.getId());
        product.setCategory(dto.getCategory());
        product.setName(dto.getName());
        Product updatedProduct = productRepository.save(product);
        return updatedProduct;

    }

    // 'public void deleteById()'
    // 개인적으로는 삭제의 경우라도, 비즈니스 로직을 수행하는 메소드가 void인 것을 선호하지 않습니다.
    // 최소한 어떤 데이터가 삭제됐는지, 성공했는지, 실패했는지 정도의 판단은 할 수 있도록 개선하면 어떨까 합니다.
    public void deleteById(Long productId) {
        Product product = getProductById(productId);
        productRepository.delete(product);
        // delete(entity) --> 이 코드는 의도했다면 좋은 시도라고 생각합니다.
        // jpa쿼리메소드에 엔티티 객체를 직접 넘겨주는 방식과 id를 넘겨주는 방식에는 동작의 차이가 있습니다.
        // 다만 메소드 이름이 deleteById인 것을 보아, 의도하지 않았다고 생각됩니다.
        // 그렇다면 select와 다르게 delete은 이렇게 하게 됐는지 배경이 궁금합니다.
    }

    public Page<Product> getListByCategory(GetProductListRequest dto) {
        PageRequest pageRequest = PageRequest.of(dto.getPage(), dto.getSize(), Sort.by(Sort.Direction.ASC, "category"));
        return productRepository.findAllByCategory(dto.getCategory(), pageRequest);
    }

    public List<String> getUniqueCategories() {
        return productRepository.findDistinctCategories();
    }
}