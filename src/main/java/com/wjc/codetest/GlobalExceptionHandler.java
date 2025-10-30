package com.wjc.codetest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice(value = {"com.wjc.codetest.product.controller"})
/*
    [문제 및 의견] 어노테이션 사용
        1. 현재 클래스 상단의 @ControllerAdvice와 메소드 상단의 @ResponseBody의 사용법에 관한 의견입니다.
           이 전역 핸들러 클래스는 json 응답을 위해 구성된 것으로 판단됩니다.
           그렇다면 이 둘의 기능을 하나로 통합한 @RestControllerAdvice로 대체하여 중복 코드를 방지하는 방법도 있습니다.
           지금은 하나의 Handler 메소드만 있으므로 중복 정도가 낮지만,
           이후 핸들러 클래스가 확장됨에 따라 가독성 및 중복 방지 차원에서 고려해볼만하다고 생각합니다.

    [요약]
        메소드의 @ResponseBody 삭제 후 클래스 상단에는 @RestControllerAdvice로 변경
 */
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> runTimeException(Exception e) {
        log.error("status :: {}, errorType :: {}, errorCause :: {}",
                HttpStatus.INTERNAL_SERVER_ERROR,
                "runtimeException",
                e.getMessage()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
/*
    [문제 및 의견] 응답 형태의 개선점 및 확장을 고려했을 때 고려해볼 점

        1. 현재 응답 구조(1)
           - 단순히 RuntimeException에 대해서만 예외를 잡아주고 있습니다.
             전체 소스코드의 규모를 감안하여 충분하다고 볼 수 있으나 확장성 관점에서는 개선이 필요하다고 생각합니다.
             전역 예외처리 핸들러의 존재는
              1) 비즈니스 로직 단에서 발생하는 문제를 가독성 있게 자동처리해준다는 점
              2) 개발자가 미쳐 잡지 못한 런타임 예외 발생에 대해 처리해준다는 점
             에서 매력이 있다고 생각합니다.
             현재는 도메인별 커스텀 예외가 존재하지 않으므로 충분해보인다고 말씀드렸지만,
             우선 당장의 controller 구현으로 인한 요청 에러 역시 500 internal server error로 일괄 처리되고 있습니다.
             이는 개발자경험 향상을 위해서라도 개선 여지가 있어보입니다.
             따라서 http 요청에서 발생할 수 있는 잘 알려진 예외(illegal, npe, valid 등..)도 핸들러로 등록하고
             각각 알맞는 상태코드와 메세지를 분리해주는 것이 어떨까 싶습니다.

         2. 현재 응답 구조(2)
            - 역시 단순하게 ResponseEntity.status() 형태로 응답하고 있습니다.
              이러한 응답 구조는 더 나은 ux/dx을 위해서 개선할 필요가 있다고 봅니다.
              특히 개발자 경험 관점에서는 어떤 예외가 발생했는지 식별할 수 있게끔 구체적일수록 좋다고 생각합니다.
              따라서 서버 에러같은 경우 잘 알려진 예외들에 대해서 별도의 핸들러를 추가하고,
              사용자 에외의 경우 도메인별 CustomException을 구현해서,
              비즈니스 예외처럼 직접 예상 가능한 에러는 구체적으로 다듬어서 응답해주면 좋을 것 같습니다.

              또한 구체적인 예외 원인과 메세지를 보기좋게 전달해주기 위해서
              별도의 ErrorResponse같은 응답 객체를 커스텀해서 반환해주면 dx향상에 도움이 될 것 같습니다.

              현재 로그에 status, errorType, errorCause 별로 명확하게 구분하고 있는 모습을 보니,
              어느정도 '예외 처리에 대한 식별성을 보장하려는 목적이 있었다' 라는 가정 하에 올린 리뷰입니다.


          ps. "runtimeException" 같은 하드코딩 보다는 예외 타입별 enum 클래스를 구성해서 상수화하는 것은 어떨까 합니다.
              또는 e.getClass().getSimpleName()을 사용하는 방법도 있습니다.
              다만 http 상태코드로 어떤 예외인지 예외 이름을 볼 수 있도록 로그를 구성했다는 점에서 의미가 중복된다고 봅니다.
              또한 이 핸들러의 메소드 인자로 Exception이 들어있습니다.
              물론 RuntimeException의 경우에 발동하는 핸들러라고는 하지만 파라미터를 통일해주면 좋을 것 같습니다.

          ps. 개인적으로 클래스 이름이 GlobalExceptionHandler이므로
              메소드 이름도 OOOExceptionHandler로 세부 역할임을 표현해주면 어떨까 싶습니다.

    [최종 의견 및 요약]
        1. 너무 포괄적으로 예외를 잡고 있는데 확장을 고려해서라도 세분화하는 것을 추천 (최소한 자주 발생하는 예외들)
        2. 일관된 응답 포맷을 리턴하는 응답 객체를 커스텀해서 구체적인 예외 식별 가능하도록 개선

 */