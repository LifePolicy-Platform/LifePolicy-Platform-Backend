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
public class AuthCurrentUserRespDto {

    @JsonProperty("USERNAME")
    private String username;

    @JsonProperty("DISPLAY_NAME")
    private String displayName;

    @JsonProperty("ROLES")
    private List<String> roles;
}