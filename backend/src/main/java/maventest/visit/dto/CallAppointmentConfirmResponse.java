<<<<<<<< HEAD:backend/src/main/java/maventest/appointment/dto/CallAppointmentConfirmResponse.java
package maventest.appointment.dto;
========
package maventest.visit.dto;
>>>>>>>> develop:backend/src/main/java/maventest/visit/dto/CallAppointmentConfirmResponse.java

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CallAppointmentConfirmResponse {

    private Long sno;
    private String listNo;
    private Integer recallResult;
    private Integer listStatus;
    private LocalDateTime recTime;
}
