package com.wjc.codetest.product.controller;

import com.wjc.codetest.product.model.request.CreateProductRequest;
import com.wjc.codetest.product.model.request.GetProductListRequest;
import com.wjc.codetest.product.model.domain.Product;
import com.wjc.codetest.product.model.request.UpdateProductRequest;
import com.wjc.codetest.product.model.response.ProductListResponse;
import com.wjc.codetest.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/*
    ps. 해당 클래스에 있는 메소드의 전반적인 공통 문제를 상위에 코멘트합니다.
        이후 각 메소드에 해당하는 부분은 메소드 상단 혹은 블럭 내부에 언급했습니다.
        또한 이 곳에서 언급한 내용의 일부는 dto, service 레이어와도 관련이 있으므로
        종합적으로 연결되는 내용은 각각의 레이어에서 상세설명 추가했습니다.

    [문제점 및 의견]
        1. restful하지 않은 요청 url 설계 및 http 메소드 사용
           일반적으로 http 메소드에 요청의 행위를, url에 대상을 복수형으로 명시하는 방식으로
           resftul하게 api를 설계하라는 권장사항이 있습니다.
           하지만 현재 모든 메소드의 url에 행위와 도메인이 혼재되어있으며, 만약 이 설계가
           의도하에 작성된 것이라고 할지라도 통일되지 않은 형태가 문제점이라고 판단됩니다.
           자세한 개선점 사례에 대해서는 각 메소드에서 다시 코멘트하겠습니다.

        2. 응답 및 반환 형식
           서비스레이어에서 entity객체를 바로 리턴받아서 응답json에 포함하고 있습니다.
           이는 영속성 컨텍스트의 생명주기 관점에서도 동작 방식을 다시 검토해볼 필요가 있다고 생각합니다.
           또한 crud 요청에 따라 데이터를 반환할 것인지, 성공/실패 여부를 반환할 것인지에 따라
           구분되어있는 모습으로 확인했는데, 역시 의도한 것일 수 있다고 생각합니다.
           따라서 이 의도를 잘 보여줄 수 있는 api 응답 전용 responseDTO를 설계하는 방식으로
           응답 포맷을 일관성있게 관리하도록 개선하는 것은 어떨까 싶습니다.

        ps. 현재 애플리케이션 내에서 jpa.osiv 설정에 대한 명시가 없고,
            Product 엔티티에 연관관계가 맺혀있지 않기 때문에 지금 코드에서 동작에 문제가 없습니다.
            다만 확장성을 고려하거나, jpa 동작과 애플리케이션 리소스를 고려한다면
            충분히 고려해볼만한 사항이 된다고 생각합니다.


    [요약]
        1. 전반적으로 restful하지 않은 url와 모호한 http 메소드의 사용은 개선 여지가 있어보임
           - http 메소드로 행동을 명시
           - 도메인은 복수 형태로 작성
           - depth가 깊어질 경우에 '/'로 구분
        2. view로 전달될 응답에 entity 객체를 직접 넘겨주는 방식과 일관되지 않은 응답 포맷에 대해서도
           더 나은 개선이 있는지 검토할 필요가 있어보임 (사용자에게 보여줄 용도의 응답이라면)
 */
@RestController
@RequestMapping
/*
    [의견] @RequestMapping 사용에 관한 의견

      이 어노테이션은 현재 존재 이유가 없습니다.
      앞서 언급드린대로 url 재설계 먼저 검토 후 해당 어노테이션을 활용하거나
      각 메소드에만 mapping 경로를 설정하는 방법 중 하나를 선택하면 될 것 같습니다.

      개인적으로는 mapping 경로에만 명시하는 것이 가독성이 낫다고 생각하는 입장입니다.
      이는 팀 내 컨벤션을 우선으로 두되 정해지지 않았다면 의견 공유 후 결정하면 될 것 같습니다.
 */
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    /*
        [보완 의견]
            파라미터로 id를 받아서 단건 조회를 수행하므로
            GET: /products/{productId} 로 개선해볼 수 있을 것 같습니다.
            이렇게 하면 http메소드에 get 하겠다는 행동을 명시했기 때문에 url에 get을 포함시킬 필요가 없어집니다.
            도메인을 복수형으로 명시하면 복수조회인지 단건조회인지 헷갈리지 않느냐는 의견에 대해서는
            파라미터 여부에 따라 구분이 가능하다는 생각입니다.
            이에 관련해서 아래의 목록 조회 메소드와 비교해보시면 좋을 것 같습니다.
     */
    @GetMapping(value = "/get/product/by/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable(name = "productId") Long productId){
        Product product = productService.getProductById(productId);
        // 앞서 언급드린대로 entity 객체를 직접 리턴하고 있는 것에 대한 코멘트입니다.
        // responseDto를 통해 응답할 것을 권장드렸고 이 방법을 적용하기 위해서
        // service 레이어 메소드의 리턴타입과 dto 객체 변환 로직에 관해서도 추가로 고려해보면 좋을 것 같습니다.
        return ResponseEntity.ok(product);
    }

    /*
        [보완 의견]
            데이터 생성 메소드이므로
            POST: /products 로 수정할 수 있을 것 같습니다.
            수정 목적과 기대효과 등은 앞서 get요청 케이스의 맥락과 동일합니다.
     */
    @PostMapping(value = "/create/product")
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequest dto){
        Product product = productService.create(dto);
        // controller 메소드 이름은 createProduct,
        // service 메소드 이름은 create 입니다.
        // 물론 현재로써도 메소드 이름이 충분히 의도가 잘 표현됐긴 합니다
        // 여기서 작은 의견을 내보자면 두 레이어는 아주 밀접하게 관련돼있으므로
        // 메소드 이름을 통일하는 것은 어떨까 합니다. (createProduct)
        return ResponseEntity.ok(product);
    }

    /*
        [보완 의견]
            데이터 단건 삭제이므로
            DELETE: /products/{productId} 를 추천해봅니다.
            GET 제외, http 메소드는 데이터를 수정한다는 점에서 어떤 것을 쓰더라도 무방하다고 하지만,
            역시 행위를 명확하게 보여주기 위해서는 api 목적과 동작에 일치하는 메소드인 @DeleteMapping을 쓰는 것이 어떨까 합니다.
     */
    @PostMapping(value = "/delete/product/{productId}")
    public ResponseEntity<Boolean> deleteProduct(@PathVariable(name = "productId") Long productId){
        productService.deleteById(productId);
        // 마찬가지로 controller, service 간 메소드 이름의 일치에 관련한 코멘트입니다.
        // get요청의 경우 'byId'를 포함해 단건 요청임을 명시했는데
        // 이곳에서는 deleteProduct라는 이름으로 작성돼있고, service 메소드는 다시 'byId'로 사용됐습니다.
        // 마찬가지로 deleteProductById() 와 같은 이름으로 메소드 의도를 정확히 명시하는 것이 좋아보입니다.
        return ResponseEntity.ok(true);
    }

    /*
        [보완 의견]
            데이터 단건 수정이므로
               a) PUT: /products/{productId} 혹은 /products
               b) PATCH: /products/{productId} 혹은 /products
            http 메소드의 경우 팀 내 컨벤션에 따라 결정하거나,
            데이터 수정이 전체입력 기반인지 부분입력 기반인지 목적에 따라 결정하면 될 것 같습니다.
            다만 dto 코멘트에도 언급했지만, url이 아닌 requestDTO에 타겟ID를 포함하고 있는 점,
            dto 클래스의 생성자가 부분필드를 받고 있는 점을 보아 (이 부분에 대한 언급은 분리했습니다.)
            부분 수정을 의도했다고 판단되어 @PatchMapping을 추천해보겠습니다.
     */
    @PostMapping(value = "/update/product")
    public ResponseEntity<Product> updateProduct(@RequestBody UpdateProductRequest dto){
        // url에 {productId} 같은 파라미터를 받지 않고
        // dto에 포함하여 요청하도록 설계되어있습니다.
        // 개인적으로 설계적 관점에 따라 의견이 나뉠 수 있으나 계속 언급드리는 대로 restful한 api설계 권장에 따르면
        // dto에서 id필드를 제거, url에 {productId}를 추가하고 메소드 이름을 수정해보는 것은 어떨까 합니다.
        Product product = productService.update(dto);
        return ResponseEntity.ok(product);
    }

    /*
        [보완 의견]
            데이터 목록 조회 이므로
            GET: /products 로 바꾸면 어떨까 합니다.
            url에 '목록조회' 임을 명시하기 위해 list 키워드를 사용했는데,
            이는 도메인을 복수형으로 명시했다는 점에서 충분히 의도를 포함할 수 있다고 생각합니다.
            (products/{id}의 경우는 단건 조회를 명시함)
            또한 read-only 목적의 get과 post의 경우 브라우저 내에서 동작의 차이가 존재하므로
            @PostMapping이 아니라 @GetMapping으로 수정하는 것이 좋아보입니다.
     */
    @PostMapping(value = "/product/list")
    public ResponseEntity<ProductListResponse> getProductListByCategory(@RequestBody GetProductListRequest dto){
        // 마찬가지로 메소드 이름의 일치 여부에 대한 의견입니다.
        // 카테고리별 product 리스트 조회가 목적이라면
        // getProductListByCategory() 혹은 getProductsByCategory() 정도로 수정 제안을 드려봅니다.
        // 마찬가지로 서비스 레이어와의 일치 여부도 고려해보면 좋을 것 같습니다.
        Page<Product> productList = productService.getListByCategory(dto);
        return ResponseEntity.ok(new ProductListResponse(productList.getContent(), productList.getTotalPages(), productList.getTotalElements(), productList.getNumber()));
        // 예외적으로 해당 응답의 경우만 responseDTO를 사용했습니다.
        // 조회할 데이터 외에 페이지 정보까지 응답에 포함시키려는 것이 목적으로 보입니다.
        // 요청DTO는 파라미터가 많아짐에 따라 가독성을 고려해 dto로 관리했다는 저의 추측이 맞다면
        // 응답DTO 파라미터에 의한 가독성 저하 문제도 고려해볼만 하다고 생각합니다.
        // 이 경우 빌더를 사용해보는 방법을 고려해볼 수 있을 것 같습니다.
        // 이 외에도 1. reqeustDTO에 기본 생성자가 없는 문제, 2.size,page 필드값 검증이 없다는 점
        // 이 문제는 현재 코드가 정상 동작하지 않을 것이라고 쉽게 추측됩니다.
    }

    /*
        [보완 의견]
            데이터 목록 조회로 우선 확인됩니다.
            메소드만 보고서는 바로 위의 메소드와 어떤 차이가 있는지 파악하기가 어렵습니다.
            service 메소드와 쿼리메소드 확인 후 동작의 차이를 인지할 수 있었습니다.
            따라서 GET: /products/categories 로 수정하면 어떨까 합니다.
            category를 조회하는 쿼리이므로 GET: /categories 를 고려해볼 수도 있었으나
            category는 product 내에 포함된 속성이고, product -> category 로 1 depth 만큼 넘어가므로
            /products/categories 의 형식으로 추천드렸습니다.
     */
    @GetMapping(value = "/product/category/list")
    public ResponseEntity<List<String>> getProductListByCategory(){
        // 마찬가지로 컨트롤러, 서비스 메소드를 통일하는 것이 어떨까 합니다.
        // 특히나 특수한 조건 하에 동작하는 메소드이므로 포괄된 의미보다는 구체적으로 명시하면 좋을 것 같습니다.
        // service의 getUniqueCategories()로 이름을 통일하는 것이 괜찮아보입니다.
        List<String> uniqueCategories = productService.getUniqueCategories();
        return ResponseEntity.ok(uniqueCategories);
    }
}