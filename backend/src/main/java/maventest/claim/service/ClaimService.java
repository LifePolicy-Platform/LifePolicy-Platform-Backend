package maventest.claim.service;

import maventest.claim.dto.ClaimCreateRequest;
import maventest.claim.dto.ClaimUpdateRequest;
import maventest.claim.dto.OptionResponse;
import maventest.claim.entity.ClaimEntity;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

public interface ClaimService {
    List<ClaimEntity> getClaimList(String status, String policyNo, String applyDate);
    ClaimEntity getClaimDetail(String claimNo);
    String createClaim(ClaimCreateRequest request);
    void updateClaim(String claimNo, ClaimUpdateRequest request);
    void deleteClaim(String claimNo);
    List<OptionResponse> getMemberOptions();
    List<OptionResponse> getPolicyOptions();
    List<OptionResponse> getAgentOptions();
    Map<String, String> uploadFile(MultipartFile file);
}