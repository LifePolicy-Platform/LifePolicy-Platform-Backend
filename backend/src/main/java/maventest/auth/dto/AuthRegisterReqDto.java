package maventest.auth.dto;

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
public class AuthRegisterReqDto {

    @JsonProperty("USERNAME")
    @NotBlank(message = "username is required")
    private String username;

    @JsonProperty("PASSWORD")
    @NotBlank(message = "password is required")
    private String password;

    @JsonProperty("DISPLAY_NAME")
    @NotBlank(message = "displayname is rewquired")
    private String displayname;
}
