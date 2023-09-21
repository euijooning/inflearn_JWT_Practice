package inflearn.freejwt.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JwtFilter extends GenericFilterBean {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    // Authorization 헤더의 이름
    public static final String AUTHORIZATION_HEADER = "Authorization";

    private TokenProvider tokenProvider;

    public JwtFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    /**
     *
     * @param servletRequest  The request to process
     * @param servletResponse The response associated with the request
     * @param filterChain    Provides access to the next filter in the chain for this
     *                 filter to pass the request and response to for further
     *                 processing
     *
     * @throws IOException
     * @throws ServletException
     *
     * 실제 필터링 로직은 이 메서드 내부에 작성
     * 이것은 토큰의 인증정보를 SecurityContext에 저장하는 역할을 수행
     */
    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwt = resolveToken(httpServletRequest);
        String requestURI = httpServletRequest.getRequestURI();

        // 추출한 JWT 토큰이 유효한지 검증
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            // JWT 토큰을 사용하여 사용자 인증 정보 생성
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            // 정상 토큰이면 Spring Security의 SecurityContextHolder에 인증 정보 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 로깅
            logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
        } else {
            // 유효한 JWT 토큰이 없을 경우 로깅
            logger.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
        }
        // 다음 필터 또는 요청 처리로 전달
        filterChain.doFilter(servletRequest, servletResponse);
    }


    /**
     *
     * @param request
     * @return
     * Request Header에서 토큰 정보를 꺼내오기 위한 resolveToken 메서드
     */
    // Authorization 헤더에서 JWT 토큰 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}