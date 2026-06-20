package maventest.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 保單審核歷程列表（tb_policy_aprv_log + tb_user.DISPLAY_NAME）
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyAprvLogList {

    @JsonProperty("POLICY_LOG_NO")
    private Long policyLogNo;

    @JsonProperty("POLICY_NO")
    private String policyNo;

    @JsonProperty("APRV_STATUS")
    private String aprvStatus;

    @JsonProperty("DISPLAY_NAME")
    private String displayName;

    @JsonProperty("APRV_TIME")
    private LocalDateTime aprvTime;

    @JsonProperty("APRV_REMARK")
    private String aprvRemark;
}
