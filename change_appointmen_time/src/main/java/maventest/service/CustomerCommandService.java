package maventest.service;

import java.util.List;

import maventest.dto.AptBatchUpdateRequest;
import maventest.dto.AptBatchUpdateResponse;

public interface CustomerCommandService {

    List<AptBatchUpdateResponse> updateAptRecords(AptBatchUpdateRequest request);
}
