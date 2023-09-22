package inflearn.freejwt.util;

import lombok.NoArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@NoArgsConstructor
public class SecurityUtil {
    private static final Logger logger = LoggerFactory.getLogger(SecurityUtil.class);

    /**
     * 이 메서드의 역할은 Security Context의 Authentication 객체를 이용해 username을 리턴해주는 간단한 유틸성 메서드
     *
     * @return 현재 사용자의 username을 Optional<String> 형태로 반환. 인증 정보가 없으면 Optional.empty() 반환.
     */
    public static Optional<String> getCurrentUsername() {
        // 현재의 Authentication 객체를 가져온다.
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증 정보가 없는 경우
        if (authentication == null) {
            logger.debug("Security Context에 인증 정보가 없습니다.");
            return Optional.empty(); // 빈 Optional 반환
        }

        String username = null;

        // Authentication 객체의 주체(principal)를 확인
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
            username = springSecurityUser.getUsername(); // UserDetails에서 username을 가져옴
        }
        else if (authentication.getPrincipal() instanceof String) {
            username = (String) authentication.getPrincipal(); // 문자열로 된 주체의 경우, username으로 설정
        }

        return Optional.ofNullable(username); // Optional로 username 반환 (null인 경우에도 처리)
    }
}
