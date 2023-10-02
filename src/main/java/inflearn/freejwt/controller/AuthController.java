package inflearn.freejwt.controller;

import inflearn.freejwt.dto.LoginDto;
import inflearn.freejwt.dto.TokenDto;
import inflearn.freejwt.jwt.JwtFilter;
import inflearn.freejwt.jwt.TokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;


    @PostMapping("/authenticate")
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto) {

        // 사용자가 제공한 로그인 정보(username과 password)를 사용하여
        // UsernamePasswordAuthenticationToken을 생성
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        // AuthenticationManagerBuilder를 사용하여
        // 생성된 authenticationToken을 인증
        Authentication authentication
                = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 인증에 성공한 경우, SecurityContextHolder에 현재 인증 정보를 설정
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // TokenProvider를 사용하여 JWT(JSON Web Token)를 생성
        String jwt = tokenProvider.createToken(authentication);

        // HTTP 응답 헤더에 JWT 토큰을 추가
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        // JWT 토큰을 포함한 TokenDto를 응답으로 반환
        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }
}
