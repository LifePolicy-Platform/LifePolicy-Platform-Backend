package maventest.policy.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 保單審核歷程（tb_policy_aprv_log）
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_policy_aprv_log")
public class PolicyAprvLogEntity {

    /** 保單審核歷程流水號 */
    @TableId(value = "POLICY_LOG_NO", type = IdType.AUTO)
    private Long policyLogNo;

    /** FK → tb_policy(POLICY_NO) */
    @TableField("POLICY_NO")
    private String policyNo;

    /** SUBMIT/PENDING/APPROVED/REJECTED/RETURN */
    @TableField("APRV_STATUS")
    private String aprvStatus;

    /** 審核人員帳號 */
    @TableField("APRV_USER")
    private String aprvUser;

    /** 審核時間 */
    @TableField("APRV_TIME")
    private LocalDateTime aprvTime;

    /** 審核意見 */
    @TableField("APRV_REMARK")
    private String aprvRemark;
}
