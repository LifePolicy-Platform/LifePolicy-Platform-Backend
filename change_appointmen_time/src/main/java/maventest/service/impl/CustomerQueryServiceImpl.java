package maventest.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import maventest.dto.AptRecordListRequest;
import maventest.dto.AptRecordListResponse;
import maventest.mapper.AptRecordMapper;
import maventest.service.CustomerQueryService;

@Service
@RequiredArgsConstructor
public class CustomerQueryServiceImpl implements CustomerQueryService {
    
    private final AptRecordMapper aptRecordMapper;
    
    @Override
    public List<AptRecordListResponse> listAptRecords(AptRecordListRequest request) {
        return aptRecordMapper.selectAptRecordList(request);
    }
}
