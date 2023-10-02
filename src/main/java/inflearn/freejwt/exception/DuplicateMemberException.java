package inflearn.freejwt.exception;

// 사용자 정의 예외 클래스인 DuplicateMemberException 을 정의.
public class DuplicateMemberException extends RuntimeException {

    // 기본 생성자
    public DuplicateMemberException() {
        super();
    }

    // 메시지와 원인 예외를 받는 생성자
    public DuplicateMemberException(String message, Throwable cause) {
        super(message, cause);
    }

    // 메시지를 받는 생성자
    public DuplicateMemberException(String message) {
        super(message);
    }

    // 원인 예외를 받는 생성자
    public DuplicateMemberException(Throwable cause) {
        super(cause);
    }
}