package maventest.dto.auth;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRegisterRespDto {

    @JsonProperty("USERNAME")
    private String username;

    @JsonProperty("DISPLAY_NAME")
    private String displayName;

    @JsonProperty("ROLES")
    private String roles;
}
