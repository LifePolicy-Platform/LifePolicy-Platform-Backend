package maventest.customer.entity;

import java.time.LocalDate;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 客戶主檔（tbca008）
 */
@Data
@TableName("tbca008")
public class CustomerMain {

    /** 客戶編號（PK） */
    @TableId(value = "CUST_NO", type = IdType.INPUT)
    private String custNo;

    /** 客戶姓名 */
    private String custName;

    /** 身分證字號 */
    private String custIdentity;

    /** 出生日期 */
    private LocalDate custBirthday;

    /** 性別 */
    private String custGender;

    /** e-mail */
    private String custMail;

    /** 地址 */
    private String custAddress;

    /**
     * 帳號狀態
     * 0: 已停用、1: 使用中
     */
    private Integer custStatus;
}
