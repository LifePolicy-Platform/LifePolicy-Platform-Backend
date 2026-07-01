<<<<<<<< HEAD:backend/src/main/java/maventest/appointment/dto/CallAppointmentCreateRequest.java
package maventest.appointment.dto;
========
package maventest.visit.dto;
>>>>>>>> develop:backend/src/main/java/maventest/visit/dto/CallAppointmentCreateRequest.java

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CallAppointmentCreateRequest {

    @NotBlank(message = "policyNo is required")
    private String policyNo;

    @NotBlank(message = "campCode is required")
    private String campCode;

    @NotNull(message = "recallTime is required")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime recallTime;
}
