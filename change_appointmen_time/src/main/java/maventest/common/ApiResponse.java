package maventest.common;

import lombok.Data;

/**
 * 統一 API 回應格式。
 * <p>
 * 所有 Controller 一律回傳這個結構，前端只要看 code 就知道結果。
 * </p>
 *
 * @param <T> 實際資料型別
 */
@Data
public class ApiResponse<T> {

    /** 結果代碼：0 = 成功，其餘為錯誤 */
    private int code;
    /** 訊息（給人看的） */
    private String message;
    /** 實際資料 */
    private T data;

    public ApiResponse() {
    }

    public ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 包裝成功回應的便捷方法。
     */
    public static <T> ApiResponse<T> ok(T data) {
        // 直接使用 ResultCode.SUCCESS 的代碼與訊息
        return new ApiResponse<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 包裝失敗回應的便捷方法。
     *
     * @param code    錯誤代碼
     * @param message 錯誤訊息
     */
    public static <T> ApiResponse<T> fail(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }
}
