package maventest.appointment.service;

import maventest.appointment.dto.CallAppointmentConfirmRequest;
import maventest.appointment.dto.CallAppointmentConfirmResponse;
import maventest.appointment.dto.CallAppointmentCreateRequest;
import maventest.appointment.dto.CallAppointmentCreateResponse;

public interface CallAppointmentCommandService {

    CallAppointmentCreateResponse createAppointment(CallAppointmentCreateRequest request, String updateUser);

    CallAppointmentConfirmResponse confirmAppointmentResult(
            CallAppointmentConfirmRequest request, String updateUser);
}