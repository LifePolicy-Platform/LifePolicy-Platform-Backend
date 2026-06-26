package maventest.dashboard.mapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import maventest.dashboard.dto.DashboardDailyCountRow;
import maventest.dashboard.dto.DashboardProductTypeCountRow;
import maventest.dashboard.dto.DashboardSlaOverduePolicyRow;
import maventest.dashboard.dto.DashboardStatusCountRow;

@Mapper
public interface DashboardMapper {

    BigDecimal sumApprovedPremiumByEffectDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    long countApprovedByEffectDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    List<DashboardDailyCountRow> countMonthApplicationsByDay(
            @Param("monthStart") LocalDateTime monthStart,
            @Param("monthEnd") LocalDateTime monthEnd);

    List<DashboardStatusCountRow> countByPolicyStatusInApplyTimeRange(
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd);

    List<DashboardProductTypeCountRow> countApprovedSalesByProductType(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    /** 待審保單：SUBMIT + PENDING */
    long countPendingPolicies();

    /** 今日進入待審佇列（今日申請或今日審核歷程進入 SUBMIT/PENDING） */
    long countPoliciesEnteredReviewToday(@Param("todayStart") LocalDateTime todayStart);

    /** 今日離開待審佇列（審核歷程 APPROVED/REJECTED/RETURN） */
    long countPoliciesExitedReviewToday(@Param("todayStart") LocalDateTime todayStart);

    /** 理賠申請件數（依 APPLY_TIME 區間） */
    long countClaimsByApplyDate(@Param("dayStart") LocalDateTime dayStart,
                                @Param("dayEnd") LocalDateTime dayEnd);

    /** 保單 SLA 逾期（審核中超過 slaDays） */
    long countPolicySlaOverdue(@Param("slaDays") int slaDays);

    /** 保單 SLA 逾期清單 */
    List<DashboardSlaOverduePolicyRow> findPolicySlaOverdueList(@Param("slaDays") int slaDays);

    /** 理賠 SLA 逾期（審核中超過 slaDays） */
    long countClaimSlaOverdue(@Param("slaDays") int slaDays);
}
