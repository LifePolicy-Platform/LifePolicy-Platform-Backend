package maventest.policyapplication.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CallListEntity {

    private String listNo;
    private Long memberId;
    private String listLastPhone;
    private Integer listStatus;
}
