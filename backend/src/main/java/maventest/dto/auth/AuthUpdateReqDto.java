package maventest.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "displayname is required")
    private String displayname;

    @JsonProperty("STATUS")
    private String status;

    @JsonProperty("ROLE")
    @NotBlank(message = "role is required")
    private String role;
}
