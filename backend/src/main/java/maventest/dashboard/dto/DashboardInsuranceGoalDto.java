package maventest.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardInsuranceGoalDto {

    /** 月目標件數（設定檔 app.dashboard.monthly-target） */
    private int monthlyTarget;

    /** 本月已核准件數（APPROVED + EFFECT_DATE 在本月） */
    private long currentAchieved;

    /** 達成率（%） */
    private int achievementRate;
}