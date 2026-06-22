package maventest.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import maventest.dto.ActiveProjectOptionDto;
import maventest.dto.AptRecordListRequest;
import maventest.dto.AptRecordListResponse;
import maventest.dto.CallAppointmentItemDto;
import maventest.dto.PolicyAppointmentContextDto;
import maventest.mapper.AptRecordMapper;
import maventest.mapper.CallAppointmentMapper;
import maventest.mapper.ProjectMainMapper;
import maventest.service.CustomerQueryService;

@Service
@RequiredArgsConstructor
public class CustomerQueryServiceImpl implements CustomerQueryService {
    
    private final AptRecordMapper aptRecordMapper;
    private final CallAppointmentMapper callAppointmentMapper;
    private final ProjectMainMapper projectMainMapper;
    
    @Override
    public List<AptRecordListResponse> listAptRecords(AptRecordListRequest request) {
        return aptRecordMapper.selectAptRecordList(request);
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
