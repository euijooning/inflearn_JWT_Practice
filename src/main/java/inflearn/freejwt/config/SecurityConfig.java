package inflearn.freejwt.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity // 기본적인 웹 시큐리티 설정을 활성화하겠다.
// 추가적 설정을 위해서 WebSecurityConfigurer 을 implement 하거나
// WebSecurityConfigureAdapter 를 extends 하는 방법이 있다.
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests() // HttpServletRequest를 사용하는 요청들에 대한 접근 제한을 설정하겠다.
                .antMatchers("/api/hello").permitAll() // /api/hello 에 대한 요청은 인증 없이 접근을 허용하겠다.
                .anyRequest().authenticated(); // 나머지 요청들에 대해서는 인증을 받아야 한다.
        // 이렇게 하고 나서 get 요청 보내보면 401 에러 사라짐.


    }
}
