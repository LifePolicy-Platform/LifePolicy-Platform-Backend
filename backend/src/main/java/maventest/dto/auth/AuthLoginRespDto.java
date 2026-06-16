package maventest.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @JsonProperty("ROLES")
    private List<String> roles;
}