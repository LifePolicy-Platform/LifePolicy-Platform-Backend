package maventest.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 保單主檔（tb_policy）
 */
@Data
@TableName("tb_policy")
public class Policy {

    /** 保單號碼（PK） */
    @TableId(value = "POLICY_NO", type = IdType.INPUT)
    private String policyNo;

    /** 要保人/會員編號（FK: tb_cust_user.MEMBER_ID） */
    private Long memberId;

    /** 對應名單（FK: tb_call_list.LIST_NO） */
    private String listNo;

    /** 商品代碼（FK: tb_product.PRODUCT_CODE） */
    private String productCode;

    /** 保單狀態（SUBMIT/PENDING/APPROVED/REJECTED/RETURN） */
    private String policyStatus;

    /** 被保險人姓名 */
    private String insuredName;

    /** 被保險人身分證字號 */
    private String insuredIdentityCard;

    /** 被保險人性別（MALE/FEMALE） */
    private String insuredGender;

    /** 被保險人生日 */
    private LocalDate insuredBirthday;

    /** 保額 */
    private BigDecimal insuredAmount;

    /** 每期保費 */
    private BigDecimal premiumAmount;

    /** 保單負責業務員（FK: tb_user.USER_ID） */
    private Long agentId;

    /** 風險等級（LOW/MEDIUM/HIGH） */
    private String riskLevel;

    /** 生效日 */
    private LocalDate effectDate;

    /** 到期日 */
    private LocalDate expireDate;

    /** 申請時間 */
    private LocalDateTime applyTime;

    /** 建立時間 */
    private LocalDateTime createTime;

    /** 更新時間 */
    private LocalDateTime updateTime;

    /** 更新人員 */
    private String updateUser;
}
