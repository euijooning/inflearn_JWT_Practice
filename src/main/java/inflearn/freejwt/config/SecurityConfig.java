package inflearn.freejwt.config;


import inflearn.freejwt.jwt.JwtAccessDeniedHandler;
import inflearn.freejwt.jwt.JwtAuthenticationEntryPoint;
import inflearn.freejwt.jwt.JwtSecurityConfig;
import inflearn.freejwt.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity // 기본적인 웹 시큐리티 설정을 활성화하겠다.
@EnableMethodSecurity // 추가
@Configuration
@RequiredArgsConstructor
public class SecurityConfig { // extend 삭제

    private final TokenProvider tokenProvider;
    private final CorsFilter corsFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     *
     * @param http the {@link HttpSecurity} to modify
     * @throws Exception
     *
     * 많은 부분들을 추가
     * : 토큰을 사용하기 때문에 csrf 설정은 disable
     *  Exception을 핸들링할 때 만들었던 클래스들을 추가한다.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // token을 사용하는 방식이기 때문에 csrf를 disable합니다.
                .csrf(csrf -> csrf.disable())

                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )

                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers("/api/hello", "/api/authenticate", "/api/signup").permitAll()
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        .anyRequest().authenticated()
                )

                // 세션을 사용하지 않기 때문에 STATELESS로 설정
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // enable h2-console
                .headers(headers ->
                        headers.frameOptions(options ->
                                options.sameOrigin()
                        )
                )

                .apply(new JwtSecurityConfig(tokenProvider));
        return http.build();
    }
}

