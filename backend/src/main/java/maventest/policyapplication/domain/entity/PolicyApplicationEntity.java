package maventest.policyapplication.domain.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import maventest.policyapplication.domain.enums.ApplicationStatus;
import maventest.policyapplication.domain.enums.Gender;
import maventest.policyapplication.domain.enums.RelationshipToInsured;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_policy")
public class PolicyApplicationEntity {

    /** 保單號碼 */
    @TableId("POLICY_NO")
    private String policyNo;

    /** 要保人/會員編號 FK → tb_cust_user(MEMBER_ID) */
    @TableField("MEMBER_ID")
    private Long memberId;

    /** 對應名單 FK → tb_call_list */
    @TableField("LIST_NO")
    private String listNo;

    /** 商品代碼 FK → tb_product */
    @TableField("PRODUCT_CODE")
    private String productCode;

    /** 保單狀態 SUBMIT/PENDING/APPROVED/REJECTED/RETURN */
    @TableField("POLICY_STATUS")
    private String policyStatus;

    /** 被保險人姓名 */
    @TableField("INSURED_NAME")
    private String insuredName;

    /** 被保險人身分證字號 */
    @TableField("INSURED_IDENTITY_CARD")
    private String insuredIdentityCard;

    /** 被保險人性別 MALE/FEMALE */
    @TableField("INSURED_GENDER")
    private String insuredGender;

    /** 被保險人生日 */
    @TableField("INSURED_BIRTHDAY")
    private LocalDate insuredBirthday;

    /** 保額 */
    @TableField("INSURED_AMOUNT")
    private BigDecimal insuredAmount;

    /** 保單手機號碼 */
    @TableField("POLICY_MOBILE")
    private String policyMobile;

    /** 每期保費 */
    @TableField("PREMIUM_AMOUNT")
    private BigDecimal premiumAmount;

    /** 保單負責業務員 FK → tb_user(USER_ID) */
    @TableField("AGENT_ID")
    private Long agentId;

    /** 風險等級 LOW/MEDIUM/HIGH */
    @TableField("RISK_LEVEL")
    private String riskLevel;

    /** 生效日 */
    @TableField("EFFECT_DATE")
    private LocalDate effectDate;

    /** 到期日 */
    @TableField("EXPIRE_DATE")
    private LocalDate expireDate;

    /** 申請時間 */
    @TableField("APPLY_TIME")
    private LocalDateTime applyTime;

    /** 建立時間 */
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    /** 更新時間 */
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    /** 更新人員 */
    @TableField("UPDATE_USER")
    private String updateUser;
}