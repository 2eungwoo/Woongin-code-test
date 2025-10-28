package com.wjc.codetest.product.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProductRequest {
    private String category;
    private String name;

    public CreateProductRequest(String category) {
        this.category = category;
    }

    public CreateProductRequest(String category, String name) {
        this.category = category;
        this.name = name;
    }
}
/*
    [문제 및 의견] DTO 클래스의 설계적 보완점 및 디렉토리 위치

        1. 디렉토리 위치(의견)
           - dto 클래스의 본질은 '데이터 전달 목적' 입니다.
             해당 dto를 포함한 requestDTO 모두가 외부(사용자요청)에서 값이 들어오는 형태이므로
             controller 레이어와 연관이 가장 깊다고 생각합니다. (물리적으로도, 개념적으로도)
             현재는 /model/domain 에서 핵심 비즈니스 모델과 공존하고 있으므로
             /controller/dto/request/ 로 이동하는 것이 어떨까 하는 의견입니다.
           - 추가로 dto 클래스는 기능이 확장됨에따라 개수가 매우 많아질 가능성이 높습니다.
             (도메인이 확장되거나 요청 목적에 따라 응답 형식이 달라진다거나 등 케바케가 너무 많음)
             이를 고려한다면 /model/domain 하위보다 /controller/dto/.. 로 위치를 바꾸는 것이
             좋지 않을까 하는 생각입니다.

        2. 설계적 보완점(1) - 불변성
            - 역시 dto 객체는 '데이터 전달 목적' 이라는 명제 하에 언급합니다.
            - 현재 '@Setter를 사용한 점', '필드가 final이 아닌 점' 에 따라 이 객체는 불변 객체가 아닙니다.
            - 따라서 불변성을 보장하기 위해서는
               1. @Setter 제거 + 필드에 final 추가
               2. class -> record 로 타입 변경
              방법을 적용해보면 좋을 것 같습니다.
              참고로 저는 가독성과 불변성이 자연스럽게 지켜지는 record를 선호합니다.

        3. 설계적 보완점(2) - 생성자(1)
            - 현재 작성된 생성자를 보고 dto 객체 뿐 아니라 전반적인 요청-응답 흐름이 잘 구현됐는지
              점검이 필요하다고 생각이 들었습니다.
            - 해당 객체를 사용하는 controller의 요청의 HTTP 메소드는 @PostMapping 입니다.
              이는 입력 json의 전체 필드를 포함하여 필드를 받습니다.
              그러나 현재 생성자는 필드 누락의 경우에 대비했다는 의도가 보입니다. (그러면 또 null 체크 없는 것 역시 문제)
            - 만약 부분 허용을 입력하려는 목적이 우선이라면
                1. 필드에 default 값 적용
                2. null 체크 등 입력 값 검증 로직 추가
                3. @PatchMapping 적용 + (2번)
            - 팀 내에서 조회 요청 제외 모두 POST로 처리하는 규칙이 없다면
              restful한 방식으로 controller를 수정하고 그에 맞게 dto 수정도 필요해보입니다.

         4. 설계적 보완점(3) - 생성자(2)
            - 현재 생성자 설계는 기능적인 문제도 있습니다.
              해당 dto가 사용되고 있는 create 요청을 보내보면
              ' Cannot construct instance of ... ' 라는 메세지와 함께 JSON parse 에러가 발생합니다.
              기본적으로 서버-클라이언트 사이 요청-응답 데이터는 json입니다.
              이 때 필요한 기본 생성자가 없기 때문에 해당 에러가 발생하는 것이므로 기본 생성자의 추가가 필요합니다.


    [최종 의견 및 요약]
        1. dto 전체를 /controller/dto/.. 하위로 이동
        2. 불변객체로 전환
        3. controller 설계에 따라 dto 재설계 고려할 것
           (개인적으로 record로 타입을 바꾸거나
           @PostMapping 유지해서 전체입력 받고 필드 다 받는 생성자 하나만 받도록 수정 할 것 같습니다.)
        4. 기본 생성자 누락

*/


