package maventest.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 登入成功回應資料（對應前端 LoginResponseData 大寫欄位）
 */
@Data
@Builder
public class LoginResponseData {

    @JsonProperty("ACCESS_TOKEN")
    private String accessToken;

    @JsonProperty("TOKEN_TYPE")
    @Builder.Default
    private String tokenType = "Bearer";

    @JsonProperty("EXPIRES_IN")
    private long expiresIn;

    @JsonProperty("USERNAME")
    private String username;

    @JsonProperty("DISPLAY_NAME")
    private String displayName;

    @JsonProperty("ROLES")
    private List<String> roles;
}
