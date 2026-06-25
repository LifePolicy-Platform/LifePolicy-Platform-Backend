package maventest.appointment.dto;

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
