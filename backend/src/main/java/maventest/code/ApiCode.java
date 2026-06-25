package maventest.code;

public enum ApiCode {
    SUCCESS("INS-0000", "成功"),
    INPUT_INVALID("INS-COM-4000", "輸入資料驗證失敗"),
    QUERY_CONDITION_REQUIRED("INS-POL-4001", "查詢至少需要一個條件"),
    PRODUCT_NOT_FOUND("INS-POL-4002", "商品不存在"),
    MEMBER_NOT_FOUND("INS-POL-4006", "查無此身分證會員資料"),
    APPLICATION_NOT_FOUND("INS-POL-4003", "保單申請不存在"),
    SORT_DIRECTION_INVALID("INS-POL-4004", "排序方向必須為 ASC 或 DESC"),
    PRODUCT_LIST_EMPTY("INS-POL-4005", "查無可用商品設定"),
    APPLICANT_AGE_INVALID("INS-POL-4221", "要保人須年滿 18 歲"),
    INSURED_AGE_INVALID("INS-POL-4222", "被保人年齡超出商品承保範圍"),
    RELATIONSHIP_INVALID("INS-POL-4223", "同一人投保須選擇「本人」關係"),
    SUM_INSURED_INVALID("INS-POL-4224", "保額超出商品承保範圍"),
    PREMIUM_RATIO_INVALID("INS-POL-4225", "年繳保費不得超過保額的 5%"),
    PREMIUM_LIMIT_INVALID("INS-POL-4226", "年繳保費超過商品上限"),
    DUPLICATE_PENDING_APPLICATION("INS-POL-4227", "同一被保人與商品已有進行中案件"),
    REVIEW_STATUS_INVALID("INS-POL-4228", "目前審核階段不允許此操作"),
    REVIEW_REASON_REQUIRED("INS-POL-4229", "退件或駁回時必須填寫原因"),
    APPLICATION_REVIEWED("INS-POL-4230", "僅待業務或待主管審核案件可審核"),
    APPLICATION_UPDATE_NOT_ALLOWED("INS-POL-4231", "僅退回補件案件可修改"),
    DOCUMENTS_INCOMPLETE("INS-POL-4232", "審核前須確認身分證明與要保書"),
    POLICY_FILES_REQUIRED("INS-POL-4233", "請上傳必要保單文件"),
    AUTHENTICATION_REQUIRED("INS-AUTH-4010", "請先登入"),
    INVALID_CREDENTIALS("INS-AUTH-4011", "帳號或密碼錯誤"),
    TOKEN_INVALID("INS-AUTH-4012", "登入已失效，請重新登入"),
    ACCESS_DENIED("INS-AUTH-4030", "您沒有權限執行此操作"),
    USER_DISABLED("INS-AUTH-4031", "帳號已停用"),
    PRODUCT_CODE_DUPLICATE("INS-PROD-4039", "商品代碼已存在"),
    PRODUCT_AMOUNT_RANGE_INVALID("INS-PROD-4040", "最低保額不可大於最高保額"),
    PRODUCT_AGE_RANGE_INVALID("INS-PROD-4041", "最低投保年齡不可大於最高投保年齡"),
    PRODUCT_STATUS_INVALID("INS-PROD-4042", "商品狀態必須為啟用或停用"),
    PRODUCT_TYPE_INVALID("INS-PROD-4043", "商品類型不正確"),
    PRODUCT_NAME_DUPLICATE("INS-PROD-4044", "商品名稱已存在"),
    SYSTEM_ERROR("INS-SYS-5000", "系統錯誤，請稍後再試"),
    USERNAME_ALREADY_EXISTS("INS-SYS-6000", "使用者名稱已存在");

    private final String code;
    private final String message;

    ApiCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
