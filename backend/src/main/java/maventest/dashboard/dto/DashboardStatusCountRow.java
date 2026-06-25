package maventest.dashboard.dto;

import lombok.Getter;
import lombok.Setter;

/** MyBatis 對應：保單狀態分布 */
@Getter
@Setter
public class DashboardStatusCountRow {

    private String policyStatus;
    private Long count;
}