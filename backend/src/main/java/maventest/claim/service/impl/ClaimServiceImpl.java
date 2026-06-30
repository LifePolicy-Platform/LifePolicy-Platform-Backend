package maventest.claim.service.impl;

import maventest.claim.dto.ClaimCreateRequest;
import maventest.claim.dto.ClaimUpdateRequest;
import maventest.claim.dto.OptionResponse;
import maventest.claim.entity.ClaimEntity;
import maventest.claim.mapper.ClaimMapper;
import maventest.claim.service.ClaimService;
import maventest.notification.service.NotificationService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClaimServiceImpl implements ClaimService {

    private final ClaimMapper claimMapper;
    private final NotificationService notificationService;

    @Value("${app.upload-dir}")
    private String uploadDir;

    @Override
    @Transactional(readOnly = true)
    public List<ClaimEntity> getClaimList(String status, String policyNo, String applyDate) {
        return claimMapper.selectClaimList(status, policyNo, applyDate);
    }

    @Override
    @Transactional(readOnly = true)
    public ClaimEntity getClaimDetail(String claimNo) {
        return claimMapper.selectByClaimNo(claimNo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createClaim(ClaimCreateRequest request) {
        String newClaimNo = "CLM" + (System.currentTimeMillis() / 1000);
        
        // 核心安全優化：由後端伺服器生成標準時間，防止前端時間造假，並解決資料庫 null 衝突
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        java.time.LocalDateTime expiry = now.plusDays(15); // 預設 15 天過期
        
        ClaimEntity claim = ClaimEntity.builder()
                .claimNo(newClaimNo)
                .memberId(request.getMemberId())
                .policyNo(request.getPolicyNo())
                .claimAmount(request.getClaimAmount())
                .claimStatus("SUBMIT")
                .agentId(request.getAgentId())
                .remark(request.getRemark())
                .applyTime(now)
                .expiryTime(expiry)
                .file01Name(request.getFile01Name())
                .file01Path(request.getFile01Path())
                .file02Name(request.getFile02Name())
                .file02Path(request.getFile02Path())
                .build();

        int rows = claimMapper.insertClaim(claim);
        if (rows <= 0) {
            throw new RuntimeException("理賠案件寫入資料庫失敗");
        }

        // 推送通知
        notificationService.pushToRole("REVIEWER", "CLAIM",
                "理賠新件待審",
                "理賠案件 " + newClaimNo + " 已送件，請確認處理。",
                newClaimNo, "SYSTEM");

        return newClaimNo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateClaim(String claimNo, ClaimUpdateRequest request) {
        ClaimEntity claim = claimMapper.selectByClaimNo(claimNo);
        if (claim == null) {
            throw new IllegalArgumentException("找不到欲修改的理賠案件");
        }

        // 只允許經辦將狀態變更為 'SUBMIT' (用於補件重新送審)
        // 嚴格禁止非審核人員惡意傳入 APPROVED 或 REJECTED
        if (request.getClaimStatus() != null) {
            String targetStatus = request.getClaimStatus().toUpperCase().trim();
            if (!"SUBMIT".equals(targetStatus) && !claim.getClaimStatus().equals(targetStatus)) {
                throw new IllegalStateException("經辦人員無權限變更案件狀態為: " + request.getClaimStatus());
            }
            claim.setClaimStatus(targetStatus);
        }

        claim.setClaimAmount(request.getClaimAmount());
        claim.setRemark(request.getRemark());
        claim.setFile01Name(request.getFile01Name());
        claim.setFile01Path(request.getFile01Path());
        claim.setFile02Name(request.getFile02Name());
        claim.setFile02Path(request.getFile02Path());

        claimMapper.updateClaim(claim);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteClaim(String claimNo) {
        ClaimEntity currentClaim = claimMapper.selectByClaimNo(claimNo);
        if (currentClaim == null) {
            throw new IllegalArgumentException("案件不存在");
        }
        if (!"PENDING".equals(currentClaim.getClaimStatus())) {
            throw new IllegalStateException("只有 PENDING 狀態的案件允許刪除");
        }
        claimMapper.deleteClaim(claimNo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OptionResponse> getMemberOptions() {
        List<Map<String, Object>> rawList = claimMapper.selectAllMemberOptions();
        List<OptionResponse> options = new ArrayList<>();
        for (Map<String, Object> map : rawList) {
            options.add(new OptionResponse(map.get("memberId"), String.valueOf(map.get("name")), null));
        }
        return options;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OptionResponse> getPolicyOptions() {
        List<Map<String, Object>> rawList = claimMapper.selectAllPolicyOptions();
        List<OptionResponse> options = new ArrayList<>();
        for (Map<String, Object> map : rawList) {
            options.add(new OptionResponse(map.get("policyNo"), String.valueOf(map.get("productName")), map.get("memberId")));
        }
        return options;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OptionResponse> getAgentOptions() {
        List<Map<String, Object>> rawList = claimMapper.selectAllAgentOptions();
        List<OptionResponse> options = new ArrayList<>();
        for (Map<String, Object> map : rawList) {
            options.add(new OptionResponse(map.get("agentId"), String.valueOf(map.get("agentName")), null));
        }
        return options;
    }

    @Override
    public Map<String, String> uploadFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("請選擇要上傳的檔案");
        }
        try {
            File dir = new File(uploadDir, "claim");
            if (!dir.exists()) dir.mkdirs();

            String originalName = file.getOriginalFilename();
            if (originalName == null || originalName.isBlank()) {
                originalName = "file_" + System.currentTimeMillis();
            }
            String savedName = UUID.randomUUID() + "_" + originalName;
            File dest = new File(dir, savedName);
            file.transferTo(dest);

            Map<String, String> result = new HashMap<>();
            result.put("fileName", originalName);
            result.put("filePath", "/uploads/claim/" + savedName);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("檔案儲存失敗: " + e.getMessage(), e);
        }
    }
}