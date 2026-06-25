package maventest.customer.service;

import java.util.List;

import maventest.appointment.dto.AptBatchUpdateRequest;
import maventest.appointment.dto.AptBatchUpdateResponse;
import maventest.appointment.dto.CallAppointmentConfirmRequest;
import maventest.appointment.dto.CallAppointmentConfirmResponse;
import maventest.appointment.dto.CallAppointmentCreateRequest;
import maventest.appointment.dto.CallAppointmentCreateResponse;
import maventest.auth.dto.UserDetailUpdateResponse;
import maventest.auth.dto.UserDetailUpdateRequest;

public interface CustomerCommandService {

    List<AptBatchUpdateResponse> updateAptRecords(AptBatchUpdateRequest request);

    CallAppointmentCreateResponse createAppointment(CallAppointmentCreateRequest request);

    CallAppointmentConfirmResponse confirmAppointmentResult(CallAppointmentConfirmRequest request);

    // 更新個人資料
    // UserDetailUpdateResponse updatePersonalInfo(UserDetailUpdateRequest request);
}
