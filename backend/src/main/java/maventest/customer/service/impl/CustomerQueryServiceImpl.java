package maventest.customer.service.impl;

import java.util.List;
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
import maventest.customer.service.CustomerQueryService;

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
    public List<AptRecordListResponse> listAptRecordsByCustName(String custName) {
        return aptRecordMapper.selectAptRecordHistoryByCustName(custName.trim());
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
