package maventest.policyapplication.application.internal.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maventest.dashboard.config.DashboardProperties;
import maventest.dashboard.dto.DashboardSlaOverduePolicyRow;
import maventest.dashboard.mapper.DashboardMapper;
import maventest.notification.service.NotificationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PolicySlaOverdueNotificationScheduler {

    private static final String NOTIF_TYPE_POLICY = "POLICY";
    private static final String TITLE = "保單審核逾期";

    private final DashboardMapper dashboardMapper;
    private final DashboardProperties dashboardProperties;
    private final NotificationService notificationService;

    /** 每天上午 9:00（台北時間）掃描 SLA 逾期保單並推播通知 */
    @Scheduled(cron = "0 0 9 * * *", zone = "Asia/Taipei")
    public void notifyPolicySlaOverdue() {
        int slaDays = dashboardProperties.getSlaReviewDays();
        List<DashboardSlaOverduePolicyRow> overdueList = dashboardMapper.findPolicySlaOverdueList(slaDays);
        if (overdueList.isEmpty()) {
            log.info("SLA 逾期保單通知：無逾期案件（slaDays={}）", slaDays);
            return;
        }

        int sentCount = 0;
        for (DashboardSlaOverduePolicyRow row : overdueList) {
            String policyNo = row.getPolicyNo();
            String status = row.getPolicyStatus();
            if (policyNo == null || policyNo.isBlank() || status == null) {
                continue;
            }

            if ("SUBMIT".equals(status)) {
                notificationService.pushToRoleIfNotSentToday(
                        "APPLICANT",
                        NOTIF_TYPE_POLICY,
                        TITLE,
                        "保單 " + policyNo + " 業務審核已超過 " + slaDays + " 天，請盡速處理。",
                        policyNo,
                        "SYSTEM"
                );
                sentCount += 1;
            } else if ("PENDING".equals(status)) {
                notificationService.pushToRoleIfNotSentToday(
                        "REVIEWER",
                        NOTIF_TYPE_POLICY,
                        TITLE,
                        "保單 " + policyNo + " 主管審核已超過 " + slaDays + " 天，請盡速處理。",
                        policyNo,
                        "SYSTEM"
                );
                sentCount += 1;
            }
        }

        log.info("SLA 逾期保單通知完成：掃描 {} 件，處理 {} 件（slaDays={}）", overdueList.size(), sentCount, slaDays);
    }
}
