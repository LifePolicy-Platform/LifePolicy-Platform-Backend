<<<<<<<< HEAD:backend/src/main/java/maventest/appointment/dto/AptRecordListRequest.java
package maventest.appointment.dto;
========
package maventest.visit.dto;
>>>>>>>> develop:backend/src/main/java/maventest/visit/dto/AptRecordListRequest.java


import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "搜尋請求")
public class AptRecordListRequest {

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "起始日期", example = "2026-06-01")
    private LocalDate startDate;

    @NotNull
    @DateTimeFormat(pattern = "HH:mm:ss")
    @Schema(
        description = "起始時間",
        example = "09:00:00",
        type = "string",
        format = "time"
    )
    private LocalTime startTime;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "結束日期", example = "2026-06-30")
    private LocalDate endDate;

    @NotNull
    @DateTimeFormat(pattern = "HH:mm:ss")
    @Schema(
        description = "結束時間",
        example = "18:00:00",
        type = "string",
        format = "time"
    )
    private LocalTime endTime;
}
