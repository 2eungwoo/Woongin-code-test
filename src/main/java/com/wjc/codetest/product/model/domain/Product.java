package com.wjc.codetest.product.model.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
/*
    [문제] @Setter의 사용과 적절한 옵션 값 부재

        1.  @Setter의 사용
            - Entity에서 Setter 지양하라는 말은 유명합니다.
              설게적 관점에서 문제가 있다고 보는 의견인데, 이에 동감하기 때문에 언급합니다.
            - 현재 setter는 service 레이어에서 update 로직 처리에 쓰이고 있는 것으로 확인했습니다.
              필드값 변경이 목적이라면, updateCategory()와 같이 의미와 목적을 명확히 하는 메소드로
              변경하는 것이 좋아보입니다.
            - '이름만 다르고 똑같지 않느냐' 라는 의견에 대해서는 '이름이 다르다'가 판단 근거의 힌트입니다.

        2. 적절한 옵션 값 부재
            - rdb 설계 시 테이블 네이밍 관례에 의하면,
              테이블 이름은 '소문자 + 복수형' 으로 짓는 것이 어떨까 싶습니다.
              이러한 방법은 테이블 이름을 적절하게 지어주지 못하는 이유로 발생하는 에러를 방지할 수 있습니다.
              ex) 랭킹 테이블을 위한 Rank 테이블은 ranks로 지어야 충돌이 안남 (MySQL 기준)
            - 다만 ProductRepository에서 JPQL 구문에 Product 로 해당 이미 작성돼있기 때문에 에러가 발생할 것입니다.
              테이블 이름과 별개로 객체 이름을 명시해주는 방법이 있습니다.

    [최종 의견 및 요약]
        1. @Setter를 제거하고 명확한 이름의 메소드를 사용하도록 변경
        2. rdb 네이밍 관례와 JPQL 사용 상황 따라 테이블 이름과 객체 이름을 명시
            @Entity(name = "Product")
            @Table(name = "products")

        ps. 개인적으로 ProjectEntity 처럼 ~Entity 이렇게 엔티티 객체임을 명시하는 것도 좋다고 생각합니다.
 */
public class Product {

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    /*
        [문제 및 의견] GenerationType.AUTO 설정에 관하여

            1. 이 설정은 문제가 될 수 있다는 내용이 널리 알려져있습니다.
               따라서 일반적으로 IDENTITY로 설정하여 JPA와 Hibernate를 일치시켜주는 것이 가장 쉬운 대안입니다.
            2. AUTO를 사용해야하는 경우 다른 대안으로는
               application.properties/yml에서 옵션을 설정하는 방법이 있습니다.
               jpa.hibernate.use-new-id-generator-mappings=false
     */
    private Long id;

    /*
        [문제 및 의견] 컬럼제약 없는 필드 선언의 위험성 및 컬럼명 지정에 관한 의견

            1. 컬럼제약 없는 필드 선언
                - 테이블 생성 시 코드 레벨에서 필드값 검증으로 어느정도 제어할 수는 있으나
                  db레벨에서 null여부, 길이 등의 제약이 없으면 잘못된 데이터가 영속되는 문제가 생길 수 있습니다.
                   (에러 나서 알아챌 수도 있고 에러 안나서 넘어갈 수도 있음 -> 위험)
                - 또 예외 처리나 검증 로직 누락이 누락됨에 따라 db에서 에러가 날 가능성도 높아집니다.

            2. 컬럼명 지정에 관한 의견
               - 현재 pk는 product_id로 '테이블_컬럼' 형태로 명시됐습니다.
               - pk 외의 컬럼에도 동일한 규칙을 적용하거나 pk를 id로 단순화 하는 등 통일하는 것이 어떨까 싶습니다.
               - rdb 설계 시 table, column 네이밍에 관한 관례를 따르는 것도 방법입니다.
               - 만약 팀 내 컨벤션에 의한 것이라면 관례를 따를 것인지 팀 내규를 우선시 할 것인지 정해볼 수 있을 것 같습니다.
                 ps. 애초에 필드명과 name 옵션에 동일한 값을 준 것은 의미가 없어보입니다.

        [최종 의견 요약]
            따라서 다음 예시처럼 보완이 필요해보입니다. a or b
              a) @Column(name = "category", nullable = false, length = 10)
              b) @Column(name = "product_category", nullable = false, length = 10)
            다만 위의 pk와 마찬가지로, 쿼리 작성 시 table.column 형태로 작성하기 때문에 a)가 낫다고 생각합니다.
     */
    @Column(name = "category")
    private String category;

    @Column(name = "name")
    private String name;

    /*
        [개인적인 의견] 기본 생성자 선언 스타일

            Entity 객체 내에서 기본생성자를 사용하는 이유는 JPA 사용과 관련이 있습니다.
            또한 protected로 접근제어 범위를 설정하는 것 역시 설계 관점에서 의도가 분명합니다.
            개인적으로 위와 같이 의도가 설계적/필수적 의도가 명확한 경우 어노테이션을 활용해서
            눈에 띄는 코드 표현을 통해 누락으로 인한 실수를 미연에 방지하는 것을 선호합니다.

        ps. 팀 내 코드컨벤션이나 코드 설계 공유 상황에 맞추면 되는 문제이지만,
            자유롭게 의견 제시가 가능하다면 이 부분에 대해서 논의해볼만 하다고 생각합니다.
 */
    protected Product() {
    }

    /*
        [개인적인 의견] Entity 내에서 public 생성자는 위험할 수 있다는 의견

            Entity 클래스는 db에 직접 영향을 주는 클래스입니다.
            지금 작성된 이 생성자는 다른 곳에서 인스턴스를 생성하거나 필드를 선택적으로 초기할 수 있는 여지를 남긴 것이 됩니다.

            PK 필드인 id는 자동 생성하도록 설정돼있으므로 생성자에서 id를 제외한 것은 정상입니다.
            하지만 public으로 열어둔 생성자로 인해
            i) db에 종속되기 위한 객체가 가져야할 비즈니스 제약을 모두 무시한 채 생성될 위험이 있습니다.
               (ex: 필드값 null 여부, pk, fk 제약조건 등)
            ii) 또한 Entity 객체 생성이 JPA가 아니라 코드로 가능하게 한다면 영속성 컨텍스트에 관리되지 않는
                엔티티 객체가 생길 위험이 있습니다.

            만약 테스트 코드 등의 이유로 Entity 객체 인스턴스가 필요하다면 정잭 팩토리 메소드를 활용하여
            통제력이 있는 코드를 작성하는 것이 어떨까 합니다.
            ex) public static Product createProduct(String c, String n) { ... }

        [요약]
            Entity 객체가 public 생성자를 갖게 하는 것에 대한 고민을 해봐야함
            만약 정말 필요하다면 생성자는 private으로 두고,
            해당 엔티티를 생성할 수 있는 static 메소드를 public으로 열어두는 방식을 추천함
            '생성'을 가능하게 한다는 것은 같으나 new Product(..)의 위험은 막고
            정적 팩토리 메소드의 설계적 이점을 살리는 방안으로 모색해볼 수 있음
     */
    public Product(String category, String name) {
        this.category = category;
        this.name = name;
    }

    /*
        [문제] Lombok @Getter와 getter의 중복 문제

        [개선 의견]
            클래스 상단에 @Getter를 명시했는데 get 메소드를 다시 정의한 것은 중복입니다.
            getter를 커스텀해서 사용하고 싶다면 @Getter를 제거할 필요가 있어보입니다.
            단, 개인적으로 getter 메소드에 '필드 가져오기' 외의 책임을 추가하는 것은 불필요하다고 생각합니다.

        [침고]
            이전에 호기심에 실험해본 결과,
            Lombok과 중복으로 동일 시그니처 메소드를 생성해서 호출하면 Lombok이 무시되는 현상을 확인해본 적이 있습니다.

            이는 직접적인 에러를 유발하지는 않았으나,
            설계적 관점에서 코드 일관성이나 유지보수 혼란을 일으킬 여지가 있으므로 지양하는 것이 맞다고 봅니다.            지양하는 것이 맞다고 봅니다.
     */

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }
}