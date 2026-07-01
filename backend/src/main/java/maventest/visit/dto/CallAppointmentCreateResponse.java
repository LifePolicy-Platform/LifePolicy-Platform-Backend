<<<<<<<< HEAD:backend/src/main/java/maventest/appointment/dto/CallAppointmentCreateResponse.java
package maventest.appointment.dto;
========
package maventest.visit.dto;
>>>>>>>> develop:backend/src/main/java/maventest/visit/dto/CallAppointmentCreateResponse.java

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CallAppointmentCreateResponse {

    private Long sno;
    private String recNo;
    private String listNo;
    private String campCode;
    private LocalDateTime recallTime;
}
