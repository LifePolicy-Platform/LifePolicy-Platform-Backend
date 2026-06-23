package maventest.service;

import java.util.List;

import maventest.dto.ActiveProjectOptionDto;
import maventest.dto.AptRecordListRequest;
import maventest.dto.AptRecordListResponse;
import maventest.dto.CallAppointmentItemDto;
import maventest.dto.PolicyAppointmentContextDto;


public interface CustomerQueryService {
    
    List<AptRecordListResponse> listAptRecords(AptRecordListRequest request);

    List<AptRecordListResponse> listAptRecordsByCustName(String custName);

    PolicyAppointmentContextDto getPolicyAppointmentContext(String policyNo);

    List<ActiveProjectOptionDto> listActiveProjects();

    List<CallAppointmentItemDto> listAppointmentsByListNo(String listNo);
}
