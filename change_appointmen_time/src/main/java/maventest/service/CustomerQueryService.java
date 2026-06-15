package maventest.service;

import java.util.List;
import maventest.dto.AptRecordListRequest;
import maventest.dto.AptRecordListResponse;


public interface CustomerQueryService {
    
    List<AptRecordListResponse> listAptRecords(AptRecordListRequest request);
}
