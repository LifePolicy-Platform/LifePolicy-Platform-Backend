package maventest.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * /me 端點回應資料（對應前端 CurrentUser 大寫欄位）
 */
@Data
@Builder
public class CurrentUserResponse {

    @JsonProperty("USERNAME")
    private String username;

    @JsonProperty("DISPLAY_NAME")
    private String displayName;

    @JsonProperty("ROLES")
    private List<String> roles;
}
