package maventest.dashboard.dto;

import lombok.Getter;
import lombok.Setter;

/** MyBatis 對應：SLA 逾期保單清單 */
@Getter
@Setter
public class DashboardSlaOverduePolicyRow {

    private String policyNo;
    private String policyStatus;
}
