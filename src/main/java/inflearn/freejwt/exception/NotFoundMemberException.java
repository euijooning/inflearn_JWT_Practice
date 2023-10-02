package inflearn.freejwt.exception;

// 사용자 정의 예외 클래스인 NotFoundMemberException을 정의.
public class NotFoundMemberException extends RuntimeException {

    // 기본 생성자
    public NotFoundMemberException() {
        super();
    }

    // 메시지와 원인 예외를 받는 생성자
    public NotFoundMemberException(String message, Throwable cause) {
        super(message, cause);
    }

    // 메시지를 받는 생성자
    public NotFoundMemberException(String message) {
        super(message);
    }

    // 원인 예외를 받는 생성자
    public NotFoundMemberException(Throwable cause) {
        super(cause);
    }
}
