package maventest.dto;

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
