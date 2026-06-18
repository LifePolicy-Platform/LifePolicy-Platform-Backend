package maventest.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 保戶帳號 — 前台（tb_cust_user）
 */
@Data
@TableName("tb_cust_user")
public class CustomerMain {

    /** 前台會員流水號 ID（PK） */
    @TableId(value = "MEMBER_ID", type = IdType.AUTO)
    private Long memberId;

    /** 使用者帳號 */
    private String username;

    /** BCrypt 加密密碼 */
    private String password;

    /** 主要保單號碼 */
    private String policyNo;

    /** 身分證字號 */
    private String identityCard;

    /** 性別（MALE/FEMALE） */
    private String gender;

    /** 電子信箱 */
    private String email;

    /** 手機號碼 */
    private String mobile;

    /** 生日 */
    private LocalDate birthday;

    /** 帳號狀態（ACTIVE/INACTIVE 等） */
    private String status;

    /** 最後登入時間 */
    private LocalDateTime lastLoginTime;

    /** 建立時間 */
    private LocalDateTime createTime;

    /** 更新時間 */
    private LocalDateTime updateTime;
}
