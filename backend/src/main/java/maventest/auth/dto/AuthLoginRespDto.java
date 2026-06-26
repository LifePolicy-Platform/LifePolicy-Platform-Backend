package maventest.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthLoginRespDto {

    @JsonProperty("ACCESS_TOKEN")
    private String accessToken;

    @JsonProperty("TOKEN_TYPE")
    private String tokenType;

    @JsonProperty("EXPIRES_IN")
    private long expiresIn;

    @JsonProperty("USERNAME")
    private String username;

    @JsonProperty("DISPLAY_NAME")
    private String displayName;

    @JsonProperty("ROLE_CODE")
    private String roleCode;
}
