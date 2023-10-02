package inflearn.freejwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    // CORS 필터 빈을 정의하는 메서드
    @Bean
    public CorsFilter corsFilter() {
        // URL 기반의 CORS 구성을 관리하는 객체 생성
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // CORS 구성 설정 객체 생성
        CorsConfiguration config = new CorsConfiguration();

        // 요청에서 자격 증명(자격 증명 쿠키, 인증 등)을 허용
        config.setAllowCredentials(true);

//        // 모든 출처(Origin)를 허용
//        config.addAllowedOrigin("*");
        config.addAllowedOriginPattern("*");

        // 모든 HTTP 헤더를 허용
        config.addAllowedHeader("*");

        // 모든 HTTP 메서드(GET, POST, PUT, DELETE 등)를 허용
        config.addAllowedMethod("*");

        // "/api/**" 패턴에 해당하는 엔드포인트에 대해 CORS 구성을 등록
        source.registerCorsConfiguration("/api/**", config);

        // CORS 필터를 생성하고 반환
        return new CorsFilter(source);
    }
}