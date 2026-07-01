package maventest.visit.service;

import maventest.visit.dto.CallAppointmentConfirmRequest;
import maventest.visit.dto.CallAppointmentConfirmResponse;
import maventest.visit.dto.CallAppointmentCreateRequest;
import maventest.visit.dto.CallAppointmentCreateResponse;

public interface CallAppointmentCommandService {

    CallAppointmentCreateResponse createAppointment(CallAppointmentCreateRequest request, String updateUser);

    CallAppointmentConfirmResponse confirmAppointmentResult(
            CallAppointmentConfirmRequest request, String updateUser);
}
