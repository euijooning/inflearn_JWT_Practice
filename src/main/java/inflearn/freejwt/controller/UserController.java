package inflearn.freejwt.controller;

import inflearn.freejwt.dto.UserDto;
import inflearn.freejwt.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    /**
     * 사용자 등록을 처리하는 엔드포인트.
     *
     * @param userDto 등록할 사용자 정보를 포함한 UserDto 객체
     * @return ResponseEntity<UserDto> 등록된 사용자 정보를 포함한 ResponseEntity
     */
    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@Valid @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.signup(userDto));
    }

    /**
     * 현재 사용자의 정보를 조회하는 엔드포인트.
     *
     * @param request HttpServletRequest 객체
     * @return ResponseEntity<UserDto> 현재 사용자 정보를 포함한 ResponseEntity
     */
    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<UserDto> getMyUserInfo(HttpServletRequest request) {
        return ResponseEntity.ok(userService.getMyUserWithAuthorities());
    }

    /**
     * 리디렉션 테스트를 위한 엔드포인트.
     *
     * @param response HttpServletResponse 객체
     * @throws IOException 입출력 예외 발생 시
     */
    @PostMapping("/test-redirect")
    public void testRedirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("/api/user");
    }

    /**
     * 특정 사용자의 정보를 조회하는 엔드포인트.
     *
     * @param username 조회할 사용자의 이름
     * @return ResponseEntity<UserDto> 조회된 사용자 정보를 포함한 ResponseEntity
     */
    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<UserDto> getUserInfo(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserWithAuthorities(username));
    }
}