package maventest.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 保單審核歷程（tb_policy_aprv_log）
 */
@Data
@TableName("tb_policy_aprv_log")
public class PolicyAprvLog {

    /** 保單審核歷程流水號 ID（PK） */
    @TableId(value = "POLICY_LOG_NO", type = IdType.AUTO)
    private Long policyLogNo;

    /** 保單號碼（FK: tb_policy.POLICY_NO） */
    private String policyNo;

    /** 審核狀態（SUBMIT/PENDING/APPROVED/REJECTED/RETURN） */
    private String aprvStatus;

    /** 審核人員帳號 */
    private String aprvUser;

    /** 審核時間 */
    private LocalDateTime aprvTime;

    /** 審核意見 */
    private String aprvRemark;
}
