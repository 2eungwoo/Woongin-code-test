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

@Slf4j // 로깅을 위한 코드가 아예 없으므로 삭제해도 무방해보입니다.
@Service
@RequiredArgsConstructor
/*
    ps. 해당 클래스의 모든 메소드에 대한 공통 의견만 이 곳에 코멘트합니다.
        이후 각 메소드에는 메소드에만 국한되는 코멘트를 적었습니다. (각 메소드의 윗부분 or 내부)

    [문제 및 의견]
        1. (그냥 의견)코드 스타일 - 통일되지 못한 코드 형태
           현재 각 메소드마다 기능과 목적에 따라 코드가 달라질 수 있음을 감안하더라도,
           어떤 것은 인스턴스 변수명으로 리턴하고, 어떤 것은 inline으로 축약해서 리턴하는 등
           전반적으로 통일성이 없는데, 팀 내 컨벤션에 따라 통일하면 어떨까 싶습니다.

           현재는 단순 crud 형태이므로 단순한 로직에 의해
           예외 처리나 기타 비즈니스 로직 등 구현체가 단순하므로 문제가 두드러지지 않기 떄문에
           '그냥 의견' 이라고 적어놨지만 꽤 중요한 사항이라고 생각합니다.

        2. Entity 객체 리턴 --> controller layer로 넘겨주고 있는 형태
           먼저 이는 전반적인 crud 구현에 있어 충분한 이해가 보충되어야 한다고 생각합니다.
             1) jpa 영속 개념과 트랜잭션 경계에 대한 이해
             2) 응답으로 어떤 데이터를 보여줄 것인지에 대한 필요성과 이유 고민 필요

     [최종 의견 및 요약]
        1. 코드 스타일 통일 필요 (리턴 형식, 메소드 이름 등)
        2. 입력값 검증, 예외 처리 로직 필요
 */
public class ProductService {

    private final ProductRepository productRepository;

    /*
        [문제 및 의견] Entity 직접 생성

            1. new Product();
               /domain.Product.java에 언급되어 있는 내용과 연장됩니다.
               엔티티 객체 생성자를 public으로 열어두었으므로 비즈니스 로직 영역에서
               new Proudct()를 사용하고 있습니다.
               Entity객체 DB와 매핑되어야하는 객체이므로 단순 데이터를 담는 dto처럼 사용되면 안된다고 생각합니다.
               게다가 이 방식은 dto에 담긴 값 검증이 이루어지지 않은 채 entity 필드에 값을 넣고 있으며
               Product.java 에서도 컬럼값 제약이 명시되어있지 않으므로 이는 문제가 있어보입니다.

            2. return save();
               save() 결과를 바로 반환하는 것이 큰 문제는 아니지만
               지금의 코드는 Entity 자체를 노출시킨다는 점은 고려해야합니다.

          [요약]
            1. Entity 인스턴스를 new로 만들고 검증되지 않은 입력값을 넣는 것에 대한 문제 해결이 필요
            2. 역시 리턴 형식 고려 필요
     */
    public Product create(CreateProductRequest dto) {
        Product product = new Product(dto.getCategory(), dto.getName());
        return productRepository.save(product);
    }

    /*
        [의견] 예외 처리 방식
            1. 현재 단순 RuntimeException으로 예외를 잡아주고 있습니다.
               이 예외는 넘겨받은 id로 해당 데이터가 없을 경우를 대비하여 의도적으로 예외를 처리해준 것인데,
               그렇다면 단순 RuntimeException으로 처리하지 말고 ProductNotFoundException 과 같은
               도메인에 특정한 커스텀 예외를 만들어주면 어떨까 합니다.
               + (파라미터 정보-productId도 예외에서 볼 수 있게끔)

            2. findById() 검증 로직 반복에 의한 재사용
               controller의 getById() 메소드와 매핑되어 단일 컨텐츠 조회 용도 목적이지만,
               update, delete 메소드에서도 재사용되고 있는 모습입니다.
               러프하게 표현하자면 재사용이라기보다 한 번 만든 코드로 돌려막기 하고 있는 형태에 가까워보입니다.
               메소드 내부에서 Optional을 통한 null체크를 해주는 것과 연장해서 언급해보자면,
               ProductValidator.isExistOrThrow() 같은 이름으로 검증 전용 클래스를 빈으로 만들어서 재사용하면
               기존의 재사용 목적을 유지하면서 책임분리와 가독성 향상을 모두 챙길 수 있어보입니다.

         [요약]
            1. 예외 처리 구체화 필요
            2. 애매하게 재사용되고 있는 메소드 -> 명확하게 개선 필요
     */
    public Product getProductById(Long productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (!productOptional.isPresent()) {
            throw new RuntimeException("product not found");
        }
        return productOptional.get();
        // Optional을 사용했지만 null 체크에 따른 분기 수행으로 코드가 작성돼있습니다.
        // .orElseThrow() 형식으로 의도는 명확하게, 코드는 간결하게 바꾸는 방법도 고려해볼 수 있을 것 같습니다.
        // 앞서 위에서 언급한 validator 사용과 결합해서 개선해보면 어떨까 합니다.
    }

    public Product update(UpdateProductRequest dto) {
        // 역시 입력값 검증이 필요합니다.
        // 따로 비즈니스 규칙이 없다하더라도 최소 null 체크는 필요해보입니다.
        Product product = getProductById(dto.getId());
        // 이 부분은 domain/Product.java 에 언급한 내용과 연결됩니다.
        // 필드 값을 변경한다는 의도가 명확한 이름을 가진 메소드(ex: update@@)로 대체할 수 있어보입니다.
        // 단, controller에 작성한 메소드와 맥락을 통일하여 전체/일부 수정 중 어떻게 설계할 것인지에 따라
        // 업데이트 메소드를 구현해보면 될 것 같습니다.
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
        // 공통부분 언급에 언급한 내용입니다.
        // (메소드 이름은 deleteById인데 실제 동작은 entity로 delete)
    }

    public Page<Product> getListByCategory(GetProductListRequest dto) {
        // 현재 controller 메소드에서도 마찬가지이지만
        // dto.getPage, dto.getSize 에 대한 검증 처리가 필요해보입니다.
        // 만약 1.페이지 정보가 0 이하의 값이 입력되는 경우,
        //     2.category가 null인 경우
        // GlobalExceptionHandler에서 RuntimeException 발생하는 것 외에는
        // 아무 처리가 되어있지 않습니다.
        PageRequest pageRequest = PageRequest.of(dto.getPage(), dto.getSize(), Sort.by(Sort.Direction.ASC, "category"));
        return productRepository.findAllByCategory(dto.getCategory(), pageRequest);
        // 또한 반환 형식이 Page<Entity>로 되어 있으므로
        // Entity객체 내부의 모든 정보와 Page 정보에 대한 모든 데이터가
        // 그대로 응답에 노출되므로 역시 응답dto를 추가하는 것이 좋아보입니다.
        // 현재 ProductListResponse.java가 존재하므로 이 객체를 활용하면 좋을 것 같습니다.
        // 참고로 Page객체를 그대로 json으로 응답하면 의도하지 않은 필드가 모두 포함되기 때문에 권장되지 않는다는 내용이
        // Spring Data 공식문서에 언급되어 있습니다.
        // link : https://docs.spring.io/spring-data/commons/reference/repositories/core-extensions.html#core.web.page
    }

    // 기능 동작에 문제가 없고 응답이 단순히 product.category라면 역시 문제 없다고 생각합니다.
    // 현재 메소드 이름과 쿼리 메소드의 경우도
    // - getUniqueCategories(): 사람한테 직관적인 이름
    // - findDistinctCategories(): 쿼리 관점에서 직관적인 이름
    // 으로 구분하기 위한 의도라면 괜찮아보입니다.
    public List<String> getUniqueCategories() {
        return productRepository.findDistinctCategories();
    }
}