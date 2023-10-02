package inflearn.freejwt.handler;

import inflearn.freejwt.dto.ErrorDto;
import inflearn.freejwt.exception.DuplicateMemberException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.FORBIDDEN;

// @ControllerAdvice 어노테이션이 지정된 클래스는 전역 예외 처리를 위한 클래스임을 나타냅니다.
@ControllerAdvice
public class RestResponseEntityException extends ResponseEntityExceptionHandler {

    // @ResponseStatus 어노테이션은 해당 메서드가 호출될 때 HTTP 응답 상태 코드를 CONFLICT(409)로 설정하도록 지정.
    @ResponseStatus(HttpStatus.CONFLICT)
    // @ExceptionHandler 어노테이션은 특정 예외 타입(DuplicateMemberException)을 처리하기 위한 메서드를 지정.
    @ExceptionHandler(value = { DuplicateMemberException.class })
    // @ResponseBody 어노테이션은 해당 메서드가 HTTP 응답 본문에 데이터를 직렬화하여 반환함.
    @ResponseBody
    // ResponseEntityExceptionHandler 클래스를 확장하여 예외를 처리하는 메서드.
    // 이 메서드는 RuntimeException을 받고, 해당 예외와 WebRequest를 사용하여 ErrorDto를 생성하고 반환.
    protected ErrorDto conflict(RuntimeException ex, WebRequest request) {
        // ErrorDto 객체를 생성하고 HTTP 응답 상태 코드와 예외 메시지를 설정하여 반환.
        return new ErrorDto(HttpStatus.CONFLICT.value(), ex.getMessage());
    }

    protected ErrorDto forbidden(RuntimeException ex, WebRequest request) {
        return new ErrorDto(FORBIDDEN.value(), ex.getMessage());
    }
}