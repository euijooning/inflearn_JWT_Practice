package inflearn.freejwt.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.*;

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