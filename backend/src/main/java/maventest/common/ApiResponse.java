package maventest.common;

import com.fasterxml.jackson.annotation.JsonProperty; // 🌟 記得加這行 import
import lombok.Data;

@Data
public class ApiResponse<T> {

    @JsonProperty("CODE")
    private String code; 

    @JsonProperty("MESSAGE")
    private String message;

    @JsonProperty("DATA")
    private T data;

    @JsonProperty("SUCCESS")
    private boolean success;

    public ApiResponse() {
    }

    public ApiResponse(String code, String message, boolean success, T data) {
        this.code = code;
        this.message = message;
        this.success = success;
        this.data = data;
    }

    /** 包裝成功回應 */
    public static <T> ApiResponse<T> ok(T data) {
        // 假設 ResultCode 拿出來的是字串，若原本是 int，可以用 String.valueOf() 轉一下
        return new ApiResponse<>("200", "SUCCESS", true, data);
    }

    /** 包裝失敗回應 */
    public static <T> ApiResponse<T> fail(int code, String message) {
        return new ApiResponse<>(String.valueOf(code), message, false, null);
    }
}