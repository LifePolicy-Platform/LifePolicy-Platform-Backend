package maventest.customer.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 客戶資料管理（customer_info）
 */
@Data
@TableName("customer_info")
public class CustomerInfo {

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /** 客戶姓名（顯示名稱） */
    private String name;

    /** 身分證字號 */
    private String identityNo;

    /** 出生日期 */
    private LocalDateTime birthDate;

    /** 電子信箱 */
    private String mail;

    /** 狀態：1=啟用，0=停用 */
    private Integer status;

    /** 建立時間 */
    private LocalDateTime createTime;

    /** 登入帳號 */
    private String username;

    /** BCrypt 密碼雜湊 */
    private String passwordHash;
}
