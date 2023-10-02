package inflearn.freejwt.config;


import inflearn.freejwt.jwt.JwtAccessDeniedHandler;
import inflearn.freejwt.jwt.JwtAuthenticationEntryPoint;
import inflearn.freejwt.jwt.JwtSecurityConfig;
import inflearn.freejwt.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity // 기본적인 웹 시큐리티 설정을 활성화하겠다.
// 추가적 설정을 위해서 WebSecurityConfigurer 을 implement 하거나
// WebSecurityConfigureAdapter 를 extends 하는 방법이 있다.
@EnableGlobalMethodSecurity(prePostEnabled = true) // @PreAuthorize 어노테이션을 메서드 단위로 추가하기 위해서 사용
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
     * h2-console 하위 모든 요청들과 파비콘 관련 요청은
     * Spring Security 로직을 수행하지 않고 접근할 수 있게 configure 메서드를 오버라이드해서 내용을 추가해준다.
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/h2-console/**"
                ,"/favicon.ico"
                ,"/error");
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
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // token을 사용하는 방식이기 때문에 csrf를 disable한다.
        httpSecurity.csrf().disable()
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // h2-console을 위한 설정
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()


                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // session을 사용하지 않음.

                .and()
                .authorizeRequests() // HttpServletRequest를 사용하는 요청들에 대한 접근 제한을 설정하겠다.
                .antMatchers("/api/hello").permitAll() // /api/hello 에 대한 요청은 인증 없이 접근을 허용하겠다.
                .antMatchers("/api/authenticate").permitAll()
                .antMatchers("/api/signup").permitAll()
                .anyRequest().authenticated() // 나머지 요청들에 대해서는 인증을 받아야 한다.

                .and()
                .apply(new JwtSecurityConfig(tokenProvider));

        return httpSecurity.build();
    }
}
