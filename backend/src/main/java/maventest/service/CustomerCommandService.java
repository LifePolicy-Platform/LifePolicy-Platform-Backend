package maventest.service;

import java.util.List;

import maventest.dto.AptBatchUpdateRequest;
import maventest.dto.AptBatchUpdateResponse;
import maventest.dto.CallAppointmentConfirmRequest;
import maventest.dto.CallAppointmentConfirmResponse;
import maventest.dto.CallAppointmentCreateRequest;
import maventest.dto.CallAppointmentCreateResponse;
import maventest.dto.UserDetailUpdateResponse;
import maventest.dto.UserDetailUpdateRequest;

public interface CustomerCommandService {

    List<AptBatchUpdateResponse> updateAptRecords(AptBatchUpdateRequest request);

    CallAppointmentCreateResponse createAppointment(CallAppointmentCreateRequest request);

    CallAppointmentConfirmResponse confirmAppointmentResult(CallAppointmentConfirmRequest request);

    // 更新個人資料
    // UserDetailUpdateResponse updatePersonalInfo(UserDetailUpdateRequest request);
}
