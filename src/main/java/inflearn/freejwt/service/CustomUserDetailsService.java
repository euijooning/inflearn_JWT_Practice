package inflearn.freejwt.service;

import inflearn.freejwt.entity.User;
import inflearn.freejwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // UserRepository를 사용하여 사용자 정보를 데이터베이스에서 검색
        return userRepository.findOneWithAuthoritiesByUsername(username)
                .map(user -> createUser(username, user))
                .orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));
    }

    // 사용자 정보를 기반으로 UserDetails 객체를 생성하는 메서드
    private org.springframework.security.core.userdetails.User createUser(String username, User user) {
        if (!user.isActivated()) { // 사용자가 활성화되지 않은 경우 예외 던짐
            throw new RuntimeException(username + " -> 활성화되어 있지 않습니다.");
        }

        // 사용자의 권한 정보를 가져와서 SimpleGrantedAuthority 객체로 변환
        List<GrantedAuthority> grantedAuthorities = user.getAuthorities()
                .stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
                .collect(Collectors.toList());

        // UserDetails 인터페이스를 구현한 객체를 생성하여 반환
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), grantedAuthorities);
    }
}