<<<<<<<< HEAD:backend/src/main/java/maventest/appointment/dto/CallAppointmentConfirmRequest.java
package maventest.appointment.dto;
========
package maventest.visit.dto;
>>>>>>>> develop:backend/src/main/java/maventest/visit/dto/CallAppointmentConfirmRequest.java

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CallAppointmentConfirmRequest {

    @NotBlank(message = "policyNo is required")
    private String policyNo;

    @NotNull(message = "recallResult is required")
    @Min(value = 1, message = "recallResult must be 1 or 2")
    @Max(value = 2, message = "recallResult must be 1 or 2")
    private Integer recallResult;

    /** 實際約訪時間（選填，未填則以系統時間記錄） */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime recTime;
}
