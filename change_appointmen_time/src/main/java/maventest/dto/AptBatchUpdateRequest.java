package maventest.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "批次更新約訪時間請求")
public class AptBatchUpdateRequest {

    @NotNull(message = "【安排方式】不得為空")
    @Schema(description = "安排方式：TODAY=安排至今日；WORKDAYS=今日+N工作天；SPECIFIC=指定時間")
    private ScheduleMode mode;

    @Schema(description = "工作天數（mode=WORKDAYS 時必填）", example = "3")
    private Integer workDays;

    @Schema(description = "指定約訪時間（mode=SPECIFIC 時必填）", example = "2026-07-01 14:30:00", type = "string")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime specificDateTime;

    @Valid
    @NotEmpty(message = "【勾選資料】不得為空")
    @Schema(description = "勾選的資料清單（mode=SPECIFIC 時僅含第一筆）")
    private List<AptUpdateItem> records;

    /** 當 mode = SPECIFIC 時，specificDateTime 必填 */
    @AssertTrue(message = "【約訪時間設定值】不得為空")
    public boolean isSpecificDateTimeValid() {
        if (ScheduleMode.SPECIFIC.equals(mode)) {
            return specificDateTime != null;
        }
        return true;
    }
}
