package com.wjc.codetest.product.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProductRequest {
    private Long id;
    private String category;
    private String name;

    public UpdateProductRequest(Long id) {
        this.id = id;
    }

    public UpdateProductRequest(Long id, String category) {
        this.id = id;
        this.category = category;
    }

    public UpdateProductRequest(Long id, String category, String name) {
        this.id = id;
        this.category = category;
        this.name = name;
    }
}
/*
    ps. CreateProductRequest.java 리뷰 코멘트에 dto 전반에 관한 설명을 포함했습니다.
        이하 공통된 의견은 제외하고 코멘트하였습니다.

    [문제 및 의견] CreateProductRequest.java 와 마찬가지

        1. 전반적인 설계 문제
           - 해당 dto 역시 CreateProductRequest와 유사한 문제와 보완점이 보입니다.
           - 현재 생성자를 세 버전 만들어서 오버로딩하는 것 역시 controller를 포함한 재설계가 필요해보입니다.
           - [다시 요약] @PostMapping을 통해 전체 필드 입력을 받게 되어있는데 필드 누락을 대비한 생성자 설계
           - 다만 '수정' 요청에 관한 건이므로 controller에서 @PatchMapping으로 변경하고 필드값 검증 로직을 추가하여
             선택적인 필드값 수정을 의도하는 방식으로의 수정은 가능해보입니다.

        2. 필드에 id가 있음(의견)
           - 현재 컨트롤러에서 @PathVariable이나 @RequestParam이 아닌 dto를 통해서 id를 받도록 돼있습니다.
             물론 이는 의도된 것일 수 있으나 개인적으로 최적의 설계인지 검토해볼 필요가 있어보입니다.
               1) restful 하지 않음
               2) 다른 controller + dto 와 통일되지 않은 방식임


    [최종 의견 요약]
        - createDTO와 마찬가지로 설계 의도와 구현 상태를 재점검해볼 것
        - id를 dto로 전달시키는 것이 옳은 방식인지 고민해볼 것
        - put/patch 어떤 것을 쓸지 확실히 정하고 dto를 알맞게 재설계할 것
 */