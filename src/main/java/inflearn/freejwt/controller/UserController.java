package inflearn.freejwt.controller;

import inflearn.freejwt.dto.UserDto;
import inflearn.freejwt.entity.User;
import inflearn.freejwt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    /**
     * 사용자 회원가입을 처리하는 엔드포인트.
     *
     * @param userdto 회원가입 요청에 필요한 사용자 정보를 담은 DTO 객체
     * @return 회원가입이 성공하면 사용자 정보를 담은 ResponseEntity를 반환
     */
    @PostMapping("/signup")
    public ResponseEntity<User> siginup(@Valid @RequestBody UserDto userdto) {
        return ResponseEntity.ok(userService.signup(userdto));
    }



    /**
     * 현재 사용자의 정보를 조회하는 엔드포인트.
     * 사용자 및 관리자 역할을 가진 사용자만 접근할 수 있다.
     *
     * @return 현재 사용자의 정보를 담은 ResponseEntity를 반환
     */
    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<User> getMyUserInfo() {
        // userService를 사용하여 현재 사용자의 정보를 조회하고, 그 결과를 ResponseEntity로 반환.
        // getMyUserWithAuthorities()는 현재 사용자의 정보와 권한 정보를 함께 조회하는 서비스 메서드.
        // 반환된 정보는 ResponseEntity.ok()를 사용하여 HTTP 200 OK 상태와 함께 반환됨.
        return ResponseEntity.ok(userService.getMyUserWithAuthorities().get());
    }

    // 추가
    @PostMapping("/test-redirect")
    public void testRedirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("/api/user");
    }



    /**
     * 특정 사용자의 정보를 조회하는 엔드포인트.
     * 관리자 역할을 가진 사용자만 접근할 수 있다.
     *
     * @param username 조회할 사용자의 이름
     * @return 특정 사용자의 정보를 담은 ResponseEntity를 반환
     */
    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<User> getMyUserInfo(HttpServletRequest request) {
//       System.out.println(request.getHeader("Authorization"));
        // userService를 사용하여 특정 사용자의 정보를 조회하고, 그 결과를 ResponseEntity로 반환함.
        // getUserWithAuthorities(username)는 특정 사용자 정보와 권한 정보를 함께 조회하는 서비스 메서드.
        // 반환된 정보는 ResponseEntity.ok()를 사용하여 HTTP 200 OK 상태와 함께 반환됨.
        return ResponseEntity.ok(userService.getMyUserWithAuthorities().get());
    }
}
