package maventest.dto;

import lombok.Getter;
import lombok.Setter;

/** MyBatis 對應：當月每日申請件數 */
@Getter
@Setter
public class DashboardDailyCountRow {

    private Integer dayOfMonth;
    private Long count;
}
