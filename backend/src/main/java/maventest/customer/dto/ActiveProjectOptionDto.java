package maventest.customer.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActiveProjectOptionDto {

    private String campCode;
    private String campName;
    private LocalDate campServiceDt;
}
