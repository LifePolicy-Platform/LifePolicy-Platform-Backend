package maventest.customer.service;

import java.util.List;

import maventest.customer.dto.ActiveProjectOptionDto;
import maventest.visit.dto.AptRecordListRequest;
import maventest.visit.dto.AptRecordListResponse;
import maventest.visit.dto.CallAppointmentItemDto;
import maventest.visit.dto.PolicyAppointmentContextDto;


public interface CustomerQueryService {
    
    List<AptRecordListResponse> listAptRecords(AptRecordListRequest request);

    List<AptRecordListResponse> listAptRecordsByIdentityCard(String identityCard);

    PolicyAppointmentContextDto getPolicyAppointmentContext(String policyNo);

    List<ActiveProjectOptionDto> listActiveProjects();

    List<CallAppointmentItemDto> listAppointmentsByListNo(String listNo);
}
