package maventest.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSideStatDto {

    private String title;
    private long count;
    private String subtitle;
    /** up / down / neutral */
    private String trend;
}