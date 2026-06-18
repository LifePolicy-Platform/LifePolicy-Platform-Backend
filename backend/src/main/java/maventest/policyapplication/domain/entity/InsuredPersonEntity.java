package maventest.policyapplication.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import maventest.policyapplication.domain.enums.Gender;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsuredPersonEntity {

    private String insuredId;
    private String applicationId;
    private String insuredIdNo;
    private String insuredName;
    private Gender insuredGender;
    private LocalDate insuredBirthdate;
    private String contactPhone;
}
