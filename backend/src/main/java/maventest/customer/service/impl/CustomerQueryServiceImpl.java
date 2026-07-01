package maventest.customer.service.impl;

import java.util.List;
<<<<<<< HEAD
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import maventest.customer.dto.ActiveProjectOptionDto;
import maventest.appointment.dto.AptRecordListRequest;
import maventest.appointment.dto.AptRecordListResponse;
import maventest.appointment.dto.CallAppointmentItemDto;
import maventest.appointment.dto.PolicyAppointmentContextDto;
import maventest.appointment.mapper.AptRecordMapper;
import maventest.appointment.mapper.CallAppointmentMapper;
import maventest.customer.mapper.ProjectMainMapper;
=======
import java.util.Locale;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import maventest.customer.dto.ActiveProjectOptionDto;
import maventest.visit.dto.AptRecordListRequest;
import maventest.visit.dto.AptRecordListResponse;
import maventest.visit.dto.CallAppointmentItemDto;
import maventest.visit.dto.PolicyAppointmentContextDto;
import maventest.visit.mapper.AptRecordMapper;
import maventest.visit.mapper.CallAppointmentMapper;
import maventest.visit.mapper.ProjectMainMapper;
>>>>>>> develop
import maventest.customer.service.CustomerQueryService;

@Service
@RequiredArgsConstructor
public class CustomerQueryServiceImpl implements CustomerQueryService {

<<<<<<< HEAD
    private final AptRecordMapper aptRecordMapper;
    private final CallAppointmentMapper callAppointmentMapper;
    private final ProjectMainMapper projectMainMapper;

=======
    private static final Pattern IDENTITY_CARD_PATTERN = Pattern.compile("^[A-Z][0-9]{9}$");
    
    private final AptRecordMapper aptRecordMapper;
    private final CallAppointmentMapper callAppointmentMapper;
    private final ProjectMainMapper projectMainMapper;
    
>>>>>>> develop
    @Override
    public List<AptRecordListResponse> listAptRecords(AptRecordListRequest request) {
        return aptRecordMapper.selectAptRecordList(request);
    }

    @Override
<<<<<<< HEAD
    public List<AptRecordListResponse> listAptRecordsByCustName(String custName) {
        return aptRecordMapper.selectAptRecordHistoryByCustName(custName.trim());
=======
    public List<AptRecordListResponse> listAptRecordsByIdentityCard(String identityCard) {
        String normalized = identityCard == null ? "" : identityCard.trim().toUpperCase(Locale.ROOT);
        if (!IDENTITY_CARD_PATTERN.matcher(normalized).matches()) {
            throw new IllegalArgumentException("身分證格式需為 1 個英文字母加 9 碼數字");
        }
        return aptRecordMapper.selectAptRecordHistoryByIdentityCard(normalized);
>>>>>>> develop
    }

    @Override
    public PolicyAppointmentContextDto getPolicyAppointmentContext(String policyNo) {
        return callAppointmentMapper.selectAppointmentContextByPolicyNo(policyNo);
    }

    @Override
    public List<ActiveProjectOptionDto> listActiveProjects() {
        return projectMainMapper.selectActiveProjects();
    }

    @Override
    public List<CallAppointmentItemDto> listAppointmentsByListNo(String listNo) {
        return callAppointmentMapper.selectByListNo(listNo);
    }
}
