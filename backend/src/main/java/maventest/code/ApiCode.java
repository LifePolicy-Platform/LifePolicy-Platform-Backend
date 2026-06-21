package maventest.code;

public enum ApiCode {
    SUCCESS("INS-0000", "SUCCESS"),
    INPUT_INVALID("INS-COM-4000", "Input validation failed"),
    QUERY_CONDITION_REQUIRED("INS-POL-4001", "At least one query condition is required"),
    PRODUCT_NOT_FOUND("INS-POL-4002", "Product does not exist"),
    MEMBER_NOT_FOUND("INS-POL-4006", "Member does not exist for the given identity card"),
    APPLICATION_NOT_FOUND("INS-POL-4003", "Application does not exist"),
    SORT_DIRECTION_INVALID("INS-POL-4004", "Sort direction must be ASC or DESC"),
    PRODUCT_LIST_EMPTY("INS-POL-4005", "No available product configuration found"),
    APPLICANT_AGE_INVALID("INS-POL-4221", "Applicant must be at least 18 years old"),
    INSURED_AGE_INVALID("INS-POL-4222", "Insured age is out of product range"),
    RELATIONSHIP_INVALID("INS-POL-4223", "Same person application must use SELF relationship"),
    SUM_INSURED_INVALID("INS-POL-4224", "Sum insured is out of product range"),
    PREMIUM_RATIO_INVALID("INS-POL-4225", "Annual premium exceeds 5 percent of sum insured"),
    PREMIUM_LIMIT_INVALID("INS-POL-4226", "Annual premium exceeds product service threshold"),
    DUPLICATE_PENDING_APPLICATION("INS-POL-4227", "Active application already exists for the same insured and product"),
    REVIEW_STATUS_INVALID("INS-POL-4228", "Target status is not allowed for the current review stage"),
    REVIEW_REASON_REQUIRED("INS-POL-4229", "Rejection reason is required when target status is REJECTED or RETURN"),
    APPLICATION_REVIEWED("INS-POL-4230", "Only SUBMIT or PENDING application can be reviewed"),
    APPLICATION_UPDATE_NOT_ALLOWED("INS-POL-4231", "Only RETURN application can be updated"),
    DOCUMENTS_INCOMPLETE("INS-POL-4232", "Required documents must be confirmed before review"),
    AUTHENTICATION_REQUIRED("INS-AUTH-4010", "Authentication is required"),
    INVALID_CREDENTIALS("INS-AUTH-4011", "Username or password is invalid"),
    TOKEN_INVALID("INS-AUTH-4012", "Access token is invalid or expired"),
    ACCESS_DENIED("INS-AUTH-4030", "You do not have permission to perform this action"),
    USER_DISABLED("INS-AUTH-4031", "User account is disabled"),
    PRODUCT_CODE_DUPLICATE("INS-PROD-4039", "Product code already exists"),
    PRODUCT_AMOUNT_RANGE_INVALID("INS-PROD-4040", "Min amount must be less than or equal to max amount"),
    PRODUCT_AGE_RANGE_INVALID("INS-PROD-4041", "Min age must be less than or equal to max age"),
    PRODUCT_STATUS_INVALID("INS-PROD-4042", "Product status must be ACTIVE or INACTIVE"),
    PRODUCT_TYPE_INVALID("INS-PROD-4043", "Invalid product type"),
    SYSTEM_ERROR("INS-SYS-5000", "System error"),
    USERNAME_ALREADY_EXISTS("INS-SYS-6000", "User is already eisits");

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