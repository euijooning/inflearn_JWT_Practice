package inflearn.freejwt.handler;

import inflearn.freejwt.dto.ErrorDto;
import inflearn.freejwt.exception.DuplicateMemberException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


// 이 어노테이션은 클래스가 예외 처리기로 사용될 때 우선순위를 지정.
// Ordered.HIGHEST_PRECEDENCE는 가장 높은 우선순위를 나타내며,
// 이 클래스가 다른 예외 처리기보다 먼저 실행됨을 의미.
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class MethodArgumentNotValidExceptionHandler {

    // @ControllerAdvice 어노테이션이 지정된 클래스는 전역 예외 처리를 위한 클래스임을 알려준다.
    @ControllerAdvice
    public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

        // @ResponseStatus 어노테이션은 해당 메서드가 호출될 때 HTTP 응답 상태 코드를 CONFLICT(409)로 설정하도록 지정한다.
        @ResponseStatus(HttpStatus.CONFLICT)
        // @ExceptionHandler 어노테이션은 특정 예외 타입(DuplicateMemberException)을 처리하기 위한 메서드를 지정한다.
        @ExceptionHandler(value = { DuplicateMemberException.class })
        // @ResponseBody 어노테이션은 해당 메서드가 HTTP 응답 본문에 데이터를 직렬화하여 반환함을 나타낸다.
        @ResponseBody
        // ResponseEntityExceptionHandler 클래스를 확장하여 예외를 처리하는 메서드입니다.
        // 이 메서드는 RuntimeException을 받고, 해당 예외와 WebRequest를 사용하여 ErrorDto를 생성하고 반환.
        protected ErrorDto badRequest(RuntimeException ex, WebRequest request) {
            // ErrorDto 객체를 생성하고 HTTP 응답 상태 코드와 예외 메시지를 설정하여 반환.
            return new ErrorDto(HttpStatus.CONFLICT.value(), ex.getMessage());
        }
    }

}