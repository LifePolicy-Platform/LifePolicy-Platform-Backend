package maventest.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 理賠檔（tb_claim）
 */
@Data
@TableName("tb_claim")
public class Claim {

    /** 理賠案號（PK） */
    @TableId(value = "CLAIM_NO", type = IdType.INPUT)
    private String claimNo;

    /** 申請理賠的會員（FK: tb_cust_user.MEMBER_ID） */
    private Long memberId;

    /** 保單號碼（FK: tb_policy.POLICY_NO） */
    private String policyNo;

    /** 申請金額 */
    private BigDecimal claimAmount;

    /** 核准金額 */
    private BigDecimal approveAmount;

    /** 理賠狀態（SUBMIT/PENDING/APPROVED/REJECTED/RETURN） */
    private String claimStatus;

    /** 負責審理的理賠經辦人員（FK: tb_user.USER_ID） */
    private Long agentId;

    /** 申請時間 */
    private LocalDateTime applyTime;

    /** 理賠處理逾期截止時間 */
    private LocalDateTime expiryTime;

    /** 結案時間 */
    private LocalDateTime settleTime;

    /** 備註 / 拒絕原因 */
    private String remark;

    /** 建立時間 */
    private LocalDateTime createTime;

    /** 更新時間 */
    private LocalDateTime updateTime;

    /** 更新人員 */
    private String updateUser;
}
