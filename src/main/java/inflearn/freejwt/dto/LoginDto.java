package inflearn.freejwt.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

    @NotNull
    @Size(min = 3, max = 50) // 유효성 검사
    private String username;

    @NotNull
    @Size(min = 3, max = 100)
    private String password;

}