package maventest.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 保戶帳號 — 前台（tb_cust_user）
 */
@Data
@TableName("tb_cust_user")
public class CustomerInfo {

    /** 前台會員流水號 ID（PK） */
    @TableId(value = "MEMBER_ID", type = IdType.AUTO)
    private Long memberId;

    /** 使用者帳號 */
    @TableField("USERNAME")
    private String username;

    /** BCrypt 加密密碼 */
    @TableField("PASSWORD")
    private String password;

    /** 主要保單號碼 */
    @TableField("POLICY_NO")
    private String policyNo;

    /** 身分證字號 */
    @TableField("IDENTITY_CARD")
    private String identityCard;

    /** 性別（MALE/FEMALE） */
    @TableField("GENDER")
    private String gender;

    /** 電子信箱 */
    @TableField("EMAIL")
    private String email;

    /** 手機號碼 */
    @TableField("MOBILE")
    private String mobile;

    /** 生日 */
    @TableField("BIRTHDAY")
    private LocalDate birthday;

    /** 帳號狀態（ACTIVE/INACTIVE 或 1/0） */
    @TableField("STATUS")
    private String status;

    /** 最後登入時間 */
    @TableField("LAST_LOGIN_TIME")
    private LocalDateTime lastLoginTime;

    /** 建立時間 */
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    /** 更新時間 */
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    public boolean isEnabled() {
        if (status == null || status.isBlank()) {
            return true;
        }
        String normalized = status.trim();
        if ("INACTIVE".equalsIgnoreCase(normalized)
                || "DISABLED".equalsIgnoreCase(normalized)
                || "0".equals(normalized)
                || "N".equalsIgnoreCase(normalized)
                || "FALSE".equalsIgnoreCase(normalized)) {
            return false;
        }
        return "ACTIVE".equalsIgnoreCase(normalized)
                || "1".equals(normalized)
                || "Y".equalsIgnoreCase(normalized)
                || "TRUE".equalsIgnoreCase(normalized)
                || "ENABLED".equalsIgnoreCase(normalized);
    }
}
