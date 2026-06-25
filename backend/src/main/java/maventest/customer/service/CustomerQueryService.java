package maventest.customer.service;

import java.util.List;

import maventest.customer.dto.ActiveProjectOptionDto;
import maventest.appointment.dto.AptRecordListRequest;
import maventest.appointment.dto.AptRecordListResponse;
import maventest.appointment.dto.CallAppointmentItemDto;
import maventest.appointment.dto.PolicyAppointmentContextDto;


public interface CustomerQueryService {

    List<AptRecordListResponse> listAptRecords(AptRecordListRequest request);

    List<AptRecordListResponse> listAptRecordsByCustName(String custName);

    PolicyAppointmentContextDto getPolicyAppointmentContext(String policyNo);

    List<ActiveProjectOptionDto> listActiveProjects();

    List<CallAppointmentItemDto> listAppointmentsByListNo(String listNo);
}
