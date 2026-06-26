package maventest.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 登入請求 DTO（對應前端大寫欄位名稱）
 */
@Data
public class LoginRequest {

    @NotBlank(message = "帳號不可為空")
    @JsonProperty("USERNAME")
    private String username;

    @NotBlank(message = "密碼不可為空")
    @JsonProperty("PASSWORD")
    private String password;
}
