package maventest.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CallAppointmentItemDto {

    private Long sno;
    private String recNo;
    private String listNo;
    private String projectCode;
    private String projectName;
    private LocalDateTime recallTime;
    private LocalDateTime recTime;
    /** 約訪結果：0=尚未完成，1=成功，2=失敗 */
    private Integer recallResult;
}
