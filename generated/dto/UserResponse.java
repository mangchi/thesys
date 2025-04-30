package com.thesys.titan.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Setter;
import lombok.Getter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserResponse {

    @Schema(description = "사용자 ID", example = "id", nullable = true)
    private Long id;

    @Schema(description = "로그인 ID", example = "username", nullable = true)
    private String username;

    @Schema(description = "비밀번호 (암호화)", example = "password", nullable = true)
    private String password;

    @Schema(description = "사용자 이름", example = "name", nullable = true)
    private String name;

    @Schema(description = "이메일", example = "email", nullable = true)
    private String email;

    @Schema(description = "전화번호", example = "phone", nullable = true)
    private String phone;

    @Schema(description = "", example = "use_yn", nullable = true)
    private Boolean useYn;

}
