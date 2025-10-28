package com.wjc.codetest.product.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetProductListRequest {
    private String category;
    private int page;
    private int size;
}
/*
    ps. CreateProductRequest.java 리뷰 코멘트에 dto 전반에 관한 설명을 포함했습니다.
        이하 공통된 의견은 제외하고 코멘트하였습니다.

    [문제 및 의견]

        - 역시 이 객체가 사용되는 지점을 보고 생성 의도를 유추해봤습니다.
           1) 페이징 조회 시 요청에 사용됨
           2) 페이징에 필요한 page, size 랑 정렬 조건으로 쓸 category 컬럼을 요청에서 받아야됨
           3) 지금은 3개지만 요구사항 늘어날수록 controller 파라미터 몸집이 커질 수 있음
           4) 따라서 requestDTO를 만들어서 관리하겠다.

           만약 이러한 의도가 맞다면.. 이 내용에 깊이 공감하여 유지해도 좋다고 생각합니다. (ps 언급 공통 문제 제외)

           다만 두 가지 정도 문제가 존재하고,
           이는 dto 설계 자체보다 controller 메소드에서의 보완에 더 가깝다고 생각하는 관계로
           controller.getProductListByCategory() 코멘트에 첨부하도록 하겠습니다.

        - 미리 요약하자면,
          HTTP GET 요청 시 @RequestBody로 dto 받는 것은 지양됨 (규약)
          page,size 입력 값 검증이 안돼있음

    [최종 요약]
        - 이 dto 설계 의도를 유추해봤음
        - controller 메소드에서 보완이 필요함
 */