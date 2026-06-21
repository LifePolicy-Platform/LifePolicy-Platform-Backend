package maventest.service;

import maventest.dto.CallAppointmentConfirmRequest;
import maventest.dto.CallAppointmentConfirmResponse;
import maventest.dto.CallAppointmentCreateRequest;
import maventest.dto.CallAppointmentCreateResponse;

public interface CallAppointmentCommandService {

    CallAppointmentCreateResponse createAppointment(CallAppointmentCreateRequest request, String updateUser);

    CallAppointmentConfirmResponse confirmAppointmentResult(
            CallAppointmentConfirmRequest request, String updateUser);
}
