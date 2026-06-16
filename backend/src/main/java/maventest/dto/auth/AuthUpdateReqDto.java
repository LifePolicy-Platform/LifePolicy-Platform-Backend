package maventest.dto.auth;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthUpdateReqDto {

    @JsonProperty("PASSWORD")
    private String password;

    @JsonProperty("DISPLAY_NAME")
    @NotBlank(message = "displayname is rewquired")
    private String displayname;

    @JsonProperty("ENABLED")
    private boolean enable;

    @JsonProperty("ROLE")
    @NotBlank(message = "role is required")
    private String role;
}
