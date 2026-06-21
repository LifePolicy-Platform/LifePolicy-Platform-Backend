package maventest.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import maventest.config.DashboardProperties;
import maventest.dto.DashboardChartItemDto;
import maventest.dto.DashboardDailyCountRow;
import maventest.dto.DashboardInsuranceGoalDto;
import maventest.dto.DashboardProductTypeCountRow;
import maventest.dto.DashboardSideStatDto;
import maventest.dto.DashboardStatusCountRow;
import maventest.dto.DashboardSummaryRespDto;
import maventest.dto.DashboardTimeSeriesPointDto;
import maventest.dto.DashboardYearlyPerformanceDto;
import maventest.mapper.DashboardMapper;
import maventest.service.DashboardQueryService;

@Service
@RequiredArgsConstructor
public class DashboardQueryServiceImpl implements DashboardQueryService {

    private static final Map<String, String> STATUS_LABELS = Map.of(
            "SUBMIT", "待業務審核",
            "PENDING", "待主管審核",
            "RETURN", "退回補件",
            "APPROVED", "已核准",
            "REJECTED", "已拒絕"
    );

    private static final Map<String, String> STATUS_COLORS = Map.of(
            "SUBMIT", "#4299e1",
            "PENDING", "#ed8936",
            "RETURN", "#ecc94b",
            "APPROVED", "#48bb78",
            "REJECTED", "#f56565"
    );

    private static final Map<String, String> PRODUCT_TYPE_LABELS = Map.of(
            "LIFE", "壽險",
            "HEALTH", "醫療險",
            "ACCIDENT", "意外險"
    );

    private static final Map<String, String> PRODUCT_TYPE_COLORS = Map.of(
            "LIFE", "#48bb78",
            "HEALTH", "#4299e1",
            "ACCIDENT", "#9f7aea"
    );

    private static final List<String> PRODUCT_TYPE_ORDER = List.of("LIFE", "HEALTH", "ACCIDENT");

    private final DashboardMapper dashboardMapper;
    private final DashboardProperties dashboardProperties;

    @Override
    public DashboardSummaryRespDto getSummary() {
        LocalDate today = LocalDate.now();
        int currentYear = today.getYear();

        LocalDate yearStart = LocalDate.of(currentYear, 1, 1);
        LocalDate yearEnd = LocalDate.of(currentYear, 12, 31);
        LocalDate lastYearStart = LocalDate.of(currentYear - 1, 1, 1);
        LocalDate lastYearSameDay = today.minusYears(1);

        YearMonth currentMonth = YearMonth.from(today);
        LocalDate monthStart = currentMonth.atDay(1);
        LocalDate monthEnd = currentMonth.atEndOfMonth();

        LocalDateTime monthApplyStart = monthStart.atStartOfDay();
        LocalDateTime monthApplyEnd = monthEnd.atTime(LocalTime.MAX);
        LocalDateTime yearApplyStart = yearStart.atStartOfDay();
        LocalDateTime yearApplyEnd = yearEnd.atTime(LocalTime.MAX);

        BigDecimal thisYearPremium = nullToZero(
                dashboardMapper.sumApprovedPremiumByEffectDateRange(yearStart, today));
        BigDecimal lastYearPremium = nullToZero(
                dashboardMapper.sumApprovedPremiumByEffectDateRange(lastYearStart, lastYearSameDay));

        long currentAchieved = dashboardMapper.countApprovedByEffectDateRange(monthStart, monthEnd);
        int monthlyTarget = dashboardProperties.getMonthlyTarget();
        int achievementRate = monthlyTarget <= 0
                ? 0
                : (int) Math.round(currentAchieved * 100.0 / monthlyTarget);

        LocalDateTime todayStart = today.atStartOfDay();

        return DashboardSummaryRespDto.builder()
                .yearlyPerformance(buildYearlyPerformance(thisYearPremium, lastYearPremium))
                .insuranceGoal(DashboardInsuranceGoalDto.builder()
                        .monthlyTarget(monthlyTarget)
                        .currentAchieved(currentAchieved)
                        .achievementRate(achievementRate)
                        .build())
                .monthApplications(buildMonthApplications(monthApplyStart, monthApplyEnd, monthEnd.getDayOfMonth()))
                .policyStatusDistribution(buildPolicyStatusDistribution(yearApplyStart, yearApplyEnd))
                .productCategorySales(buildProductCategorySales(yearStart, yearEnd))
                .sideStats(buildSideStats(
                        todayStart,
                        monthApplyStart,
                        monthApplyEnd,
                        currentMonth.minusMonths(1).atDay(1).atStartOfDay(),
                        currentMonth.minusMonths(1).atEndOfMonth().atTime(LocalTime.MAX)))
                .build();
    }

    private List<DashboardSideStatDto> buildSideStats(
            LocalDateTime todayStart,
            LocalDateTime monthStart,
            LocalDateTime monthEnd,
            LocalDateTime lastMonthStart,
            LocalDateTime lastMonthEnd) {
        int slaDays = dashboardProperties.getSlaReviewDays();

        long pendingNow = dashboardMapper.countPendingPolicies();
        long enteredToday = dashboardMapper.countPoliciesEnteredReviewToday(todayStart);
        long exitedToday = dashboardMapper.countPoliciesExitedReviewToday(todayStart);
        long pendingYesterday = pendingNow - enteredToday + exitedToday;
        long pendingDelta = pendingNow - pendingYesterday;

        long claimsThisMonth = dashboardMapper.countClaimsByApplyDate(monthStart, monthEnd);
        long claimsLastMonth = dashboardMapper.countClaimsByApplyDate(lastMonthStart, lastMonthEnd);
        long claimsDelta = claimsThisMonth - claimsLastMonth;

        long slaOverdue = dashboardMapper.countPolicySlaOverdue(slaDays)
                + dashboardMapper.countClaimSlaOverdue(slaDays);

        return List.of(
                buildSideStat("待審保單", pendingNow, pendingDelta),
                buildSideStat("當月理賠申請", claimsThisMonth, claimsDelta, true),
                DashboardSideStatDto.builder()
                        .title("SLA 逾期案件")
                        .count(slaOverdue)
                        .subtitle("審核超過 " + slaDays + " 天，需優先處理")
                        .trend("neutral")
                        .build()
        );
    }

    private DashboardSideStatDto buildSideStat(String title, long count, long delta) {
        return buildSideStat(title, count, delta, false);
    }

    private DashboardSideStatDto buildSideStat(String title, long count, long delta, boolean monthOverMonth) {
        return DashboardSideStatDto.builder()
                .title(title)
                .count(count)
                .subtitle(monthOverMonth
                        ? formatMonthOverMonthSubtitle(delta)
                        : formatDayOverDaySubtitle(delta))
                .trend(resolveTrend(delta))
                .build();
    }

    private String formatDayOverDaySubtitle(long delta) {
        if (delta > 0) {
            return "較昨日 +" + delta + " 件";
        }
        if (delta < 0) {
            return "較昨日 " + delta + " 件";
        }
        return "與昨日相同";
    }

    private String formatMonthOverMonthSubtitle(long delta) {
        if (delta > 0) {
            return "較上月 +" + delta + " 件";
        }
        if (delta < 0) {
            return "較上月 " + delta + " 件";
        }
        return "與上月相同";
    }

    private String resolveTrend(long delta) {
        if (delta > 0) {
            return "up";
        }
        if (delta < 0) {
            return "down";
        }
        return "neutral";
    }

    private DashboardYearlyPerformanceDto buildYearlyPerformance(
            BigDecimal thisYearPremium,
            BigDecimal lastYearPremium) {
        Trend trend = calculateTrend(thisYearPremium, lastYearPremium);
        return DashboardYearlyPerformanceDto.builder()
                .amount(thisYearPremium)
                .subtitle("今年核准保費合計")
                .trend(trend.direction())
                .trendLabel(trend.label())
                .build();
    }

    private List<DashboardTimeSeriesPointDto> buildMonthApplications(
            LocalDateTime monthStart,
            LocalDateTime monthEnd,
            int daysInMonth) {
        List<DashboardDailyCountRow> rows =
                dashboardMapper.countMonthApplicationsByDay(monthStart, monthEnd);

        Map<Integer, Long> dayCountMap = new LinkedHashMap<>();
        for (int day = 1; day <= daysInMonth; day++) {
            dayCountMap.put(day, 0L);
        }
        for (DashboardDailyCountRow row : rows) {
            if (row.getDayOfMonth() != null) {
                dayCountMap.put(row.getDayOfMonth(), row.getCount() == null ? 0L : row.getCount());
            }
        }

        List<DashboardTimeSeriesPointDto> points = new ArrayList<>(daysInMonth);
        dayCountMap.forEach((day, count) -> points.add(
                DashboardTimeSeriesPointDto.builder()
                        .label(String.format("%02d", day))
                        .value(count)
                        .build()));
        return points;
    }

    private List<DashboardChartItemDto> buildPolicyStatusDistribution(
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd) {
        List<DashboardStatusCountRow> rows =
                dashboardMapper.countByPolicyStatusInApplyTimeRange(rangeStart, rangeEnd);
        List<DashboardChartItemDto> items = new ArrayList<>();
        for (DashboardStatusCountRow row : rows) {
            String status = row.getPolicyStatus() == null ? "UNKNOWN" : row.getPolicyStatus();
            items.add(DashboardChartItemDto.builder()
                    .label(STATUS_LABELS.getOrDefault(status, status))
                    .value(row.getCount() == null ? 0L : row.getCount())
                    .color(STATUS_COLORS.getOrDefault(status, "#718096"))
                    .build());
        }
        return items;
    }

    private List<DashboardChartItemDto> buildProductCategorySales(LocalDate rangeStart, LocalDate rangeEnd) {
        List<DashboardProductTypeCountRow> rows =
                dashboardMapper.countApprovedSalesByProductType(rangeStart, rangeEnd);

        Map<String, Long> countByType = new LinkedHashMap<>();
        for (String type : PRODUCT_TYPE_ORDER) {
            countByType.put(type, 0L);
        }
        for (DashboardProductTypeCountRow row : rows) {
            if (row.getProductType() == null) {
                continue;
            }
            String type = row.getProductType().trim().toUpperCase();
            if (countByType.containsKey(type)) {
                countByType.put(type, row.getCount() == null ? 0L : row.getCount());
            }
        }

        List<DashboardChartItemDto> items = new ArrayList<>();
        for (String type : PRODUCT_TYPE_ORDER) {
            items.add(DashboardChartItemDto.builder()
                    .label(PRODUCT_TYPE_LABELS.get(type))
                    .value(countByType.get(type))
                    .color(PRODUCT_TYPE_COLORS.get(type))
                    .build());
        }
        return items;
    }

    private BigDecimal nullToZero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private Trend calculateTrend(BigDecimal current, BigDecimal previous) {
        if (previous.compareTo(BigDecimal.ZERO) <= 0) {
            if (current.compareTo(BigDecimal.ZERO) > 0) {
                return new Trend("up", "較去年同期成長");
            }
            return new Trend("neutral", "—");
        }

        BigDecimal diff = current.subtract(previous);
        BigDecimal rate = diff.multiply(BigDecimal.valueOf(100))
                .divide(previous, 1, RoundingMode.HALF_UP);

        if (diff.compareTo(BigDecimal.ZERO) > 0) {
            return new Trend("up", "+" + rate + "%");
        }
        if (diff.compareTo(BigDecimal.ZERO) < 0) {
            return new Trend("down", rate + "%");
        }
        return new Trend("neutral", "0%");
    }

    private record Trend(String direction, String label) {
    }
}
