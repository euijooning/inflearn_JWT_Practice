package inflearn.freejwt.handler;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

// 이 어노테이션은 클래스가 예외 처리기로 사용될 때 우선순위를 지정.
// Ordered.HIGHEST_PRECEDENCE는 가장 높은 우선순위를 나타내며,
// 이 클래스가 다른 예외 처리기보다 먼저 실행됨을 의미.
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class MethodArgumentNotValidExceptionHandler {

    // 이 어노테이션은 해당 메서드가 호출될 때 HTTP 응답 상태 코드를 BAD_REQUEST로 설정하도록 지정.
    @ResponseStatus(HttpStatus.BAD_REQUEST)

    // 이 어노테이션은 해당 메서드가 HTTP 응답 본문에 데이터를 직렬화하여 반환함을 나타낸다.
    @ResponseBody

    // 이 어노테이션은 해당 메서드가 MethodArgumentNotValidException 예외를 처리한다는 것을 나타낸다.
    // 이 예외는 주로 Spring MVC 컨트롤러에서 요청 매개변수의 유효성 검사 실패 시 발생.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Error methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<org.springframework.validation.FieldError> fieldErrors = result.getFieldErrors();
        return processFieldErrors(fieldErrors);
    }

    // 필드 오류 목록을 처리하고 오류 응답 객체를 생성하는 메서드.
    private Error processFieldErrors(List<org.springframework.validation.FieldError> fieldErrors) {
        Error error = new Error(HttpStatus.BAD_REQUEST.value(), "@Valid Error");
        for (org.springframework.validation.FieldError fieldError : fieldErrors) {
            error.addFieldError(fieldError.getObjectName(), fieldError.getField(), fieldError.getDefaultMessage());
        }
        return error;
    }



    // 오류 응답을 나타내는 내부 클래스.
    static class Error {
        private final int status;
        private final String message;
        private List<FieldError> fieldErrors = new ArrayList<>();

        // 오류 객체를 생성할 때 HTTP 응답 상태 코드와 메시지를 설정.
        Error(int status, String message) {
            this.status = status;
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        // 필드 오류를 추가하는 메서드.
        public void addFieldError(String objectName, String path, String message) {
            FieldError error = new FieldError(objectName, path, message);
            fieldErrors.add(error);
        }

        public List<FieldError> getFieldErrors() {
            return fieldErrors;
        }
    }
}