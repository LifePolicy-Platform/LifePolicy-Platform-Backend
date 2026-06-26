package maventest.policyapplication.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberProfileRespDto {

    @JsonProperty("MEMBER_ID")
    private Long memberId;

    @JsonProperty("MEMBER_NAME")
    private String memberName;

    @JsonProperty("GENDER")
    private String gender;

    @JsonProperty("BIRTHDAY")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @JsonProperty("CONTACT_PHONE")
    private String contactPhone;
}
