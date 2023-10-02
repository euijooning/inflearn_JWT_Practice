package inflearn.freejwt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import inflearn.freejwt.entity.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @NotNull
    @Size(min = 3, max = 50)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    @Size(min = 3, max = 100)
    private String password;

    @NotNull
    @Size(min = 3, max = 50)
    private String nickname;


    // 추가: UserDto 클래스에 있는 authorityDtoSet 필드를 선언.
    private Set<AuthorityDto> authorityDtoSet;

    // 정적 메서드인 from(User user)를 정의.
    public static UserDto from(User user) {
        // 만약 입력으로 주어진 user가 null이면 null을 반환.
        if (user == null) return null;

        // UserDto 객체를 빌더 패턴을 사용하여 생성.
        return UserDto.builder()
                // User 객체의 username 값을 UserDto 객체의 username에 설정.
                .username(user.getUsername())
                // User 객체의 nickname 값을 UserDto 객체의 nickname에 설정.
                .nickname(user.getNickname())
                // User 객체의 authorities (권한) 집합을 스트림으로 변환한 후,
                // 각 권한을 AuthorityDto 객체로 매핑하여 새로운 Set으로 수집.
                .authorityDtoSet(user.getAuthorities().stream()
                        .map(authority -> AuthorityDto.builder().authorityName(authority.getAuthorityName()).build())
                        .collect(Collectors.toSet()))
                // UserDto 객체를 빌드하여 반환합니다.
                .build();
    }
}
