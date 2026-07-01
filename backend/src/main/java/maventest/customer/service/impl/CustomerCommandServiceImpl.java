package maventest.customer.service.impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
<<<<<<< HEAD
import maventest.appointment.dto.AptBatchUpdateRequest;
import maventest.appointment.dto.AptBatchUpdateResponse;
import maventest.appointment.dto.AptUpdateItem;
import maventest.appointment.dto.CallAppointmentConfirmRequest;
import maventest.appointment.dto.CallAppointmentConfirmResponse;
import maventest.appointment.dto.CallAppointmentCreateRequest;
import maventest.appointment.dto.CallAppointmentCreateResponse;
import maventest.appointment.service.AptRecordSingleService;
import maventest.appointment.service.CallAppointmentCommandService;
=======
import maventest.visit.dto.AptBatchUpdateRequest;
import maventest.visit.dto.AptBatchUpdateResponse;
import maventest.visit.dto.AptUpdateItem;
import maventest.visit.dto.CallAppointmentConfirmRequest;
import maventest.visit.dto.CallAppointmentConfirmResponse;
import maventest.visit.dto.CallAppointmentCreateRequest;
import maventest.visit.dto.CallAppointmentCreateResponse;
import maventest.visit.service.AptRecordSingleService;
import maventest.visit.service.CallAppointmentCommandService;
>>>>>>> develop
import maventest.customer.service.CustomerCommandService;

@Service
@RequiredArgsConstructor
public class CustomerCommandServiceImpl implements CustomerCommandService {

    private final AptRecordSingleService singleService;
    private final CallAppointmentCommandService callAppointmentCommandService;

    @Override
    public List<AptBatchUpdateResponse> updateAptRecords(AptBatchUpdateRequest request) {

        LocalDate targetDate;
        LocalDateTime specificDateTime = null;

        switch (request.getMode()) {
            case TODAY:
                targetDate = LocalDate.now();
                break;
            case WORKDAYS:
                if (request.getWorkDays() == null) {
                    throw new IllegalArgumentException("【工作天數】不得為空");
                }
                targetDate = plusWorkDays(LocalDate.now(), request.getWorkDays());
                break;
            case SPECIFIC:
                targetDate = request.getSpecificDateTime().toLocalDate();
                specificDateTime = request.getSpecificDateTime();
                break;
            default:
                throw new IllegalArgumentException("未知的安排方式");
        }

        String updateUser = resolveUpdateUser();

        // ===  for 迴圈，逐筆處理 → 驗證（無交易）→ 更新（REQUIRES_NEW） ===
        List<AptBatchUpdateResponse> results = new ArrayList<>();
        for (AptUpdateItem item : request.getRecords()) {
            AptBatchUpdateResponse resp = new AptBatchUpdateResponse();
            resp.setSno(item.getSno());
            try {
                LocalDateTime newRecallTime = singleService.calcNewRecallTime(
                        targetDate, item.getRecallTime(), specificDateTime);

                if (!newRecallTime.isAfter(LocalDateTime.now().plusMinutes(5))) {
                    throw new RuntimeException("約訪時間需大於現在時間 5 分鐘後");
                }

                singleService.executeUpdate(item.getListNo(), newRecallTime, updateUser);
                resp.setResult("success");
                resp.setRecallTime(newRecallTime);
            } catch (Exception e) {
                resp.setResult("fail");
                resp.setErrorMsg(e.getMessage());
            }
            results.add(resp);
        }
        return results;
    }

    @Override
    public CallAppointmentCreateResponse createAppointment(CallAppointmentCreateRequest request) {
        return callAppointmentCommandService.createAppointment(request, resolveUpdateUser());
    }

    @Override
    public CallAppointmentConfirmResponse confirmAppointmentResult(CallAppointmentConfirmRequest request) {
        return callAppointmentCommandService.confirmAppointmentResult(request, resolveUpdateUser());
    }

    private String resolveUpdateUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "system";
        }
        String name = authentication.getName();
        if (name == null || name.isBlank() || "anonymousUser".equals(name)) {
            return "system";
        }
        return name;
    }

    /** 今日 + N 個工作天（排除週六、週日） */
    private LocalDate plusWorkDays(LocalDate start, int workDays) {
        LocalDate date = start;
        int added = 0;
        while (added < workDays) {
            date = date.plusDays(1);
            DayOfWeek dow = date.getDayOfWeek();
            if (dow != DayOfWeek.SATURDAY && dow != DayOfWeek.SUNDAY) {
                added++;
            }
        }
        return date;
    }
}
