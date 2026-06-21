package maventest.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardYearlyPerformanceDto {

    /** 今年成交保費合計（APPROVED + EFFECT_DATE 在今年） */
    private BigDecimal amount;

    private String subtitle;

    /** up | down | neutral */
    private String trend;

    private String trendLabel;
}
