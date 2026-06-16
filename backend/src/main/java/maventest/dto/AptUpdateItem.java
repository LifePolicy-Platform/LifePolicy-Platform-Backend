package maventest.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "單筆勾選的約訪資料")
public class AptUpdateItem {

    @NotNull(message = "【流水號】不得為空")
    @Schema(description = "流水號（識別用，回傳結果時對應前端列）")
    private Long sno;

    @NotBlank(message = "【名單序號】不得為空")
    @Schema(description = "名單序號（更新條件）")
    private String listNo;

    @NotNull(message = "【約訪時間】不得為空")
    @Schema(description = "原始約訪時間（TODAY/WORKDAYS 模式用於保留時分）", example = "2026-07-01 14:30:00", type = "string")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime recallTime;
}
