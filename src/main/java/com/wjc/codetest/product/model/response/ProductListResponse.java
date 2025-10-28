package com.wjc.codetest.product.model.response;

import com.wjc.codetest.product.model.domain.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author : 변영우 byw1666@wjcompass.com
 * @since : 2025-10-27
 */
@Getter
@Setter
public class ProductListResponse {
    private List<Product> products;
    private int totalPages;
    private long totalElements;
    private int page;

    public ProductListResponse(List<Product> content, int totalPages, long totalElements, int number) {
        this.products = content;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.page = number;
    }
}
/*
    ps. CreateProductRequest.java 리뷰 코멘트에 dto 전반에 관한 설명을 포함했습니다.
        이하 공통된 의견은 제외하고 코멘트하였습니다.

    [문제 및 의견] 전반적인 응답 DTO 설계 관점

        1. 필드에 노출된 Entity 객체
           - 현재 Paging 응답 전문 중 필요한 필드만 받아오기 위해서 필드를 구성했다는 의도가 보입니다.
             하지만 이 중에서 List<Product> 는 문제가 있어보입니다.
               i) '데이터 전달' 을 목적으로 하는 것을 넘어 엔티티 객체의 모든 것(내부 모든 것)을 노출할 수 있습니다.
                   따라서 별도의 ProductResponse.java 형태로 단일 응답 dto를 만들고 이를 List로 응답하는 형태를
                   취하는 것이 좋아보입니다.
                   - 만약 이렇게 할 경우 paging 정보인 메타데이터는 누락되는데, 이 역시 별도 클래스를 만들어서
                     분리하는 것이 좋다고 생각됩니다. (의견)
              ii) N+1 발생 가능성이 있습니다.
                  현재는 Product에 아무런 연관 매핑이 걸려있지 않아 문제가 없습니다.
                  추후 @ToMany 관계가 걸리는 경우 외에도 ProductListResponse.getProducts()를 통해
                  Product(엔티티객체)에 접근한다면 추가 쿼리가 발생하게 됩니다.
                  또한 ex) responseDTO.getDate.getId.. 이런 식의 연쇄적인 get 사용은 객체지향 관점에서
                  올바른 설계가 아니라고 판단됩니다.



    [최종 의견 및 요약]
        - 내부 엔티티 객체를 필드로 갖게 하지 말 것
        - 차라리 단일 Response랑 Page Metadata 분리해서 단일 Response를 포함한 ListResponse로 만들 것
    [예시 클래스 설계]
        ProductResponse(...){...}
        PageMetaResponse(...){...}
        ProductListResponse(List<ProductResponse>, PageMetaResponse){...}
*/