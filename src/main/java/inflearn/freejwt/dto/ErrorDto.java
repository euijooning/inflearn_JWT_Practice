package inflearn.freejwt.dto;

import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

// ErrorDTO 클래스는 API 또는 웹 애플리케이션에서 예외 또는 오류 정보를 표현하는 데 사용됩니다.
public class ErrorDto {
    // 상태 코드를 나타내는 변수. HTTP 상태 코드와 관련이 있으며, 클라이언트에게 반환됨.
    private final int status;

    // 클라이언트에게 오류의 원인을 설명하는 데 사용.
    private final String message;

    // 필드 오류(FieldError) 객체들을 저장하는 리스트.
    // 필드 오류는 양식 유효성 검사와 관련이 있으며, 특정 필드에 대한 오류를 저장.
    private List<FieldError> fieldErrors = new ArrayList<>();



    // ErrorDTO 클래스의 생성자. 상태 코드와 메시지를 전달하여 객체를 초기화.
    public ErrorDto(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }


    public String getMessage() {
        return message;
    }

    // 필드 오류(FieldError)를 추가하는 메서드입
    // 이 메서드를 사용하여 특정 필드에 대한 오류를 ErrorDTO 객체에 추가할 수 있다.
    public void addFieldError(String objectName, String path, String message) {
        FieldError error = new FieldError(objectName, path, message);
        fieldErrors.add(error);
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }
}