package inflearn.freejwt.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenProvider implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    private static final String AUTHORITIES_KEY = "auth";

    private final String secret;
    private final long tokenValidityInMilliseconds;

    private Key key;


    public TokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds) {
        this.secret = secret;
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
    }

    @Override
    public void afterPropertiesSet() {
        // JWT 서명을 위한 Key 생성
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }
    /**
     * InitializingBean을 implements 해서 afterPropertiesSet을 오버라이드 한 이유는
     * 빈이 생성 되고 주입을 받은 후에 secret 값을 Base64 Decode 해서 key 변수에 할당하기 위함.
     */


    /**
     *
     * @param authentication
     * @return
     *
     * Authentication 객체의 권한정보를 이용해서 토큰을 생성하는 createToken 메서드 추가
     */
    public String createToken(Authentication authentication) {
        // 사용자 권한을 JWT에 추가
        String authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds); // 토큰 만료 시간

        // JWT 토큰 생성
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }


    /**
     *
     * @param token
     * @return
     *
     * Token에 담겨있는 정보를 이용해 Authentication 객체를 리턴하는 메서드 생성
     */
    public Authentication getAuthentication(String token) {
        // JWT 토큰 파싱
        // 토큰을 파라미터로 받아서 클레임을 만들고
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();


        // 사용자 권한 파싱 및 Principal 생성
        Collection<? extends GrantedAuthority> authorities = // 클레임에서 권한정보들을 빼내서
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // 권한정보들을 이용해서 User 객체를 만들어 주고,
        User principal = new User(claims.getSubject(), "", authorities);

        // 유저객체와 토큰, 권한정보를 이용해서 최종적으로
        // Authentication 객체 반환
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    /**
     *
     * @param token
     * @return
     * 토큰을 파라미터로 받아서 토큰의 유효성 검사를 수행하는 validateToken 메서드 추가
     */
    public boolean validateToken(String token) {
        try {
            // 토큰을 파싱해보고
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;

            // 나오는 Exception들을 캐치하고
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            logger.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            logger.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            logger.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }
}
