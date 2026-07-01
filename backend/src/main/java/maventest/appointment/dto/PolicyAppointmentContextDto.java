package maventest.appointment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyAppointmentContextDto {

    private String policyNo;
    private String listNo;
    private Long memberId;
    private String custName;
    private String listLastPhone;
    /** 名單狀態：0=未處理，1=已約訪，2=已結案 */
    private Integer listStatus;
    /** 進行中約訪流水號（RECALL_RESULT=0 且最新一筆） */
    private Long pendingAppointmentSno;
    /** 進行中約訪的 UPDATE_USER */
    private String pendingAppointmentUser;
}
