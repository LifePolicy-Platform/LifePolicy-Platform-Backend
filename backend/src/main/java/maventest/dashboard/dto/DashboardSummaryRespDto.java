package maventest.dashboard.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryRespDto {

    private DashboardYearlyPerformanceDto yearlyPerformance;
    private DashboardInsuranceGoalDto insuranceGoal;
    private List<DashboardTimeSeriesPointDto> monthApplications;
    private List<DashboardChartItemDto> policyStatusDistribution;
    private List<DashboardChartItemDto> productCategorySales;
    private List<DashboardSideStatDto> sideStats;
}
