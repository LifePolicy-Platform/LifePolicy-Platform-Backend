<<<<<<<< HEAD:backend/src/main/java/maventest/appointment/service/impl/CallAppointmentCommandServiceImpl.java
package maventest.appointment.service.impl;
========
package maventest.visit.service.impl;
>>>>>>>> develop:backend/src/main/java/maventest/visit/service/impl/CallAppointmentCommandServiceImpl.java

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
<<<<<<<< HEAD:backend/src/main/java/maventest/appointment/service/impl/CallAppointmentCommandServiceImpl.java
import maventest.appointment.dto.CallAppointmentConfirmRequest;
import maventest.appointment.dto.CallAppointmentConfirmResponse;
import maventest.appointment.dto.CallAppointmentCreateRequest;
import maventest.appointment.dto.CallAppointmentCreateResponse;
import maventest.appointment.dto.PolicyAppointmentContextDto;
import maventest.appointment.entity.CallAppointmentEntity;
import maventest.appointment.mapper.CallAppointmentMapper;
import maventest.appointment.service.CallAppointmentCommandService;
import maventest.customer.dto.ActiveProjectOptionDto;
import maventest.customer.mapper.ProjectMainMapper;
import maventest.policyapplication.infrastructure.repository.mapper.CallListMapper;
========
import maventest.customer.dto.ActiveProjectOptionDto;
import maventest.visit.dto.CallAppointmentConfirmRequest;
import maventest.visit.dto.CallAppointmentConfirmResponse;
import maventest.visit.dto.CallAppointmentCreateRequest;
import maventest.visit.dto.CallAppointmentCreateResponse;
import maventest.visit.dto.PolicyAppointmentContextDto;
import maventest.visit.entity.CallAppointmentEntity;
import maventest.visit.mapper.CallAppointmentMapper;
import maventest.visit.mapper.ProjectMainMapper;
import maventest.policyapplication.infrastructure.repository.mapper.CallListMapper;
import maventest.visit.service.CallAppointmentCommandService;
>>>>>>>> develop:backend/src/main/java/maventest/visit/service/impl/CallAppointmentCommandServiceImpl.java

@Service
@RequiredArgsConstructor
public class CallAppointmentCommandServiceImpl implements CallAppointmentCommandService {

    /** 名單狀態：0=未處理 */
    private static final int CALL_LIST_STATUS_PENDING = 0;
    /** 名單狀態：1=已約訪 */
    private static final int CALL_LIST_STATUS_APPOINTED = 1;
    /** 名單狀態：2=已結案 */
    private static final int CALL_LIST_STATUS_CLOSED = 2;

    /** 約訪結果：1=成功 */
    private static final int RECALL_RESULT_SUCCESS = 1;
    /** 約訪結果：2=失敗 */
    private static final int RECALL_RESULT_FAILED = 2;

    private static final String REC_NO_PREFIX = "REC";
    private static final DateTimeFormatter REC_NO_PERIOD = DateTimeFormatter.ofPattern("yyyyMM");
    private static final int REC_NO_SEQUENCE_LENGTH = 4;

    private final CallAppointmentMapper callAppointmentMapper;
    private final ProjectMainMapper projectMainMapper;
    private final CallListMapper callListMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CallAppointmentCreateResponse createAppointment(CallAppointmentCreateRequest request, String updateUser) {
        PolicyAppointmentContextDto context = callAppointmentMapper
                .selectAppointmentContextByPolicyNo(request.getPolicyNo());
        if (context == null || context.getListNo() == null || context.getListNo().isBlank()) {
            throw new IllegalArgumentException("查無保單對應之名單資料");
        }

        Integer listStatus = context.getListStatus();
        if (listStatus == null || (listStatus != CALL_LIST_STATUS_PENDING && listStatus != CALL_LIST_STATUS_CLOSED)) {
            throw new IllegalArgumentException("此名單尚有未完成約訪，無法新增約訪");
        }

        ActiveProjectOptionDto project = findActiveProject(request.getCampCode());
        if (project == null) {
            throw new IllegalArgumentException("專案不存在或已超過服務期限");
        }

        LocalDateTime recallTime = request.getRecallTime();
        if (!recallTime.isAfter(LocalDateTime.now().plusMinutes(5))) {
            throw new IllegalArgumentException("約訪時間需大於現在時間 5 分鐘後");
        }

        LocalDateTime now = LocalDateTime.now();
        String recNo = generateRecNo(now);
        CallAppointmentEntity entity = CallAppointmentEntity.builder()
                .recNo(recNo)
                .listNo(context.getListNo())
                .campCode(request.getCampCode())
                .recallTime(recallTime)
                .createTime(now)
                .updateUser(updateUser)
                .build();

        callAppointmentMapper.insertCallAppointment(entity);
        callListMapper.updateListStatus(context.getListNo(), CALL_LIST_STATUS_APPOINTED);

        return CallAppointmentCreateResponse.builder()
                .sno(entity.getSno())
                .recNo(recNo)
                .listNo(context.getListNo())
                .campCode(request.getCampCode())
                .recallTime(recallTime)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CallAppointmentConfirmResponse confirmAppointmentResult(
            CallAppointmentConfirmRequest request, String updateUser) {
        PolicyAppointmentContextDto context = callAppointmentMapper
                .selectAppointmentContextByPolicyNo(request.getPolicyNo());
        if (context == null || context.getListNo() == null || context.getListNo().isBlank()) {
            throw new IllegalArgumentException("查無保單對應之名單資料");
        }

        if (context.getListStatus() == null || context.getListStatus() != CALL_LIST_STATUS_APPOINTED) {
            throw new IllegalArgumentException("名單狀態非已約訪，無法確認約訪結果");
        }

        CallAppointmentEntity pending = callAppointmentMapper.selectPendingByListNo(context.getListNo());
        if (pending == null) {
            throw new IllegalArgumentException("查無進行中的約訪記錄");
        }

        if (!Objects.equals(pending.getUpdateUser(), updateUser)) {
            throw new IllegalArgumentException("僅約訪承辦人可確認約訪結果");
        }

        int recallResult = request.getRecallResult();
        if (recallResult != RECALL_RESULT_SUCCESS && recallResult != RECALL_RESULT_FAILED) {
            throw new IllegalArgumentException("約訪結果僅能選擇成功或失敗");
        }

        LocalDateTime recTime;
        if (recallResult == RECALL_RESULT_SUCCESS) {
            recTime = request.getRecTime();
            if (recTime == null) {
                throw new IllegalArgumentException("請選擇實際約訪時間");
            }
            if (pending.getRecallTime() != null && !recTime.isAfter(pending.getRecallTime())) {
                throw new IllegalArgumentException("實際約訪時間需大於約訪時間");
            }
        } else {
            recTime = request.getRecTime() != null ? request.getRecTime() : LocalDateTime.now();
        }
        int updated = callAppointmentMapper.confirmAppointmentResult(
                pending.getSno(), recallResult, recTime, updateUser);
        if (updated == 0) {
            throw new IllegalArgumentException("約訪結果已確認或資料不存在");
        }

        callListMapper.updateListStatus(context.getListNo(), CALL_LIST_STATUS_CLOSED);

        return CallAppointmentConfirmResponse.builder()
                .sno(pending.getSno())
                .listNo(context.getListNo())
                .recallResult(recallResult)
                .listStatus(CALL_LIST_STATUS_CLOSED)
                .recTime(recTime)
                .build();
    }

    private ActiveProjectOptionDto findActiveProject(String campCode) {
        List<ActiveProjectOptionDto> activeProjects = projectMainMapper.selectActiveProjects();
        return activeProjects.stream()
                .filter(project -> campCode.equals(project.getCampCode()))
                .findFirst()
                .orElse(null);
    }

    private String generateRecNo(LocalDateTime baseTime) {
        String period = baseTime.format(REC_NO_PERIOD);
        String fullPrefix = REC_NO_PREFIX + period;
        String maxNo = callAppointmentMapper.findMaxRecNoByPrefix(fullPrefix);
        int nextSeq = resolveNextSequence(maxNo, fullPrefix);
        return fullPrefix + String.format("%0" + REC_NO_SEQUENCE_LENGTH + "d", nextSeq);
    }

    private int resolveNextSequence(String maxNo, String prefix) {
        if (maxNo == null || maxNo.length() <= prefix.length()) {
            return 1;
        }
        String seqPart = maxNo.substring(prefix.length());
        return Integer.parseInt(seqPart) + 1;
    }
}
