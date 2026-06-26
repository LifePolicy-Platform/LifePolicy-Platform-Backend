package maventest.visit.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "約訪時間查詢")
public class AptRecordListResponse {

    @Schema(description = "流水號")
    private Long sno;

    @Schema(description = "名單序號")
    private String listNo;

    @Schema(description = "約訪紀錄序號")
    private String recNo;

    @Schema(description = "客戶姓名")
    private String custName;

    @Schema(description = "專案名稱")
    private String campName;

    /** 預定約訪時間 */
    @Schema(description = "預定約訪時間", example = "2026-06-09 14:30:00", type = "string")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime recallTime;

    /** 實際約訪時間（未完成為 null） */
    @Schema(description = "實際約訪時間", example = "2026-06-09 14:30:00", type = "string")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime recTime;

    @Schema(description = "撥出電話")
    private String listLastphone;

    @Schema(description = "名單回收日")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate campServiceDt;

    /** 約訪結果：0=尚未完成，1=約訪成功，2=約訪失敗 */
    @Schema(description = "約訪結果")
    private Integer recallResult;
}
