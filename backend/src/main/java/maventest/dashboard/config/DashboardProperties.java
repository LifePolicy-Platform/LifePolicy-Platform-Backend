package maventest.dashboard.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.dashboard")
public class DashboardProperties {

    /** 月投保目標件數 */
    private int monthlyTarget = 200;

    /** 審核 SLA 天數（收到流程後） */
    private int slaReviewDays = 7;
}
