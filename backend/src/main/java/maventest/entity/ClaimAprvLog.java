package maventest.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 理賠審核歷程（tb_claim_aprv_log）
 */
@Data
@TableName("tb_claim_aprv_log")
public class ClaimAprvLog {

    /** 理賠歷程流水號 ID（PK） */
    @TableId(value = "CLAIM_LOG_NO", type = IdType.AUTO)
    private Long claimLogNo;

    /** 理賠案號（FK: tb_claim.CLAIM_NO） */
    private String claimNo;

    /** 保單號碼（FK: tb_policy.POLICY_NO） */
    private String policyNo;

    /** 理賠狀態（SUBMIT/PENDING/APPROVED/REJECTED/RETURN） */
    private String claimStatus;

    /** 本次核准金額 */
    private BigDecimal approveAmount;

    /** 審核意見 */
    private String aprvRemark;

    /** 建立時間 */
    private LocalDateTime createTime;

    /** 審核時間 */
    private LocalDateTime aprvTime;

    /** 審核人員帳號 */
    private String aprvUser;
}
