package maventest.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 前端 auth 模組期望的回應格式（大寫欄位），對應 ApiEnvelope&lt;T&gt;
 */
@Data
public class AuthApiResponse<T> {

    @JsonProperty("CODE")
    private String code;

    @JsonProperty("MESSAGE")
    private String message;

    @JsonProperty("SUCCESS")
    private boolean success;

    @JsonProperty("DATA")
    private T data;

    public static <T> AuthApiResponse<T> ok(T data) {
        AuthApiResponse<T> res = new AuthApiResponse<>();
        res.code = "0";
        res.message = "成功";
        res.success = true;
        res.data = data;
        return res;
    }

    public static <T> AuthApiResponse<T> fail(String code, String message) {
        AuthApiResponse<T> res = new AuthApiResponse<>();
        res.code = code;
        res.message = message;
        res.success = false;
        res.data = null;
        return res;
    }
}
