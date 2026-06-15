package maventest.common;

/**
 * 統一回應代碼列舉。
 * <p>
 * 把常用結果代碼集中管理，避免到處寫 magic number。
 * </p>
 */
public enum ResultCode {

    /** 成功 */
    SUCCESS(0, "成功"),
    /** 參數錯誤（400） */
    PARAM_ERROR(400, "參數錯誤"),
    /** 未授權（401） */
    UNAUTHORIZED(401, "未授權"),
    /** 禁止存取（403） */
    FORBIDDEN(403, "禁止存取"),
    /** 資源不存在（404） */
    NOT_FOUND(404, "資源不存在"),
    /** 伺服器內部錯誤（500） */
    SERVER_ERROR(500, "伺服器內部錯誤");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
