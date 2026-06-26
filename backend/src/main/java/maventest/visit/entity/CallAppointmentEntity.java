package maventest.visit.entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CallAppointmentEntity {

    private Long sno;
    private String recNo;
    private String listNo;
    private String campCode;
    private LocalDateTime recallTime;
    private LocalDateTime recTime;
    private Integer recallResult;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String updateUser;
}
