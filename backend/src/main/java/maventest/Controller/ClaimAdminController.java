package maventest.Controller;

import maventest.entity.ClaimEntity;
import maventest.mapper.ClaimMapper;
import maventest.common.ApiResponse;
import maventest.policyapplication.application.internal.commandservices.NotificationService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/claim")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ClaimAdminController {

    private final ClaimMapper claimMapper;
    private final NotificationService notificationService;
    @Value("${app.upload-dir}")
    private String uploadDir;

    // 1. 查詢理賠申請清單
    @GetMapping("/list")
    public ResponseEntity<?> getClaimList(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String policyNo,
            @RequestParam(required = false) String applyDate) {
        List<ClaimEntity> list = claimMapper.selectClaimList(status, policyNo, applyDate);

        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    // 2. 查看理賠詳情
    @GetMapping("/{claimNo}")
    public ResponseEntity<?> getClaimDetail(@PathVariable String claimNo) {
        ClaimEntity claim = claimMapper.selectByClaimNo(claimNo);
        if (claim == null) {
            return ResponseEntity.status(404).body(ApiResponse.fail(404, "找不到該理賠案件"));
        }
        return ResponseEntity.ok(ApiResponse.ok(claim));
    }

    // 3. 新增理賠紀錄
    @PostMapping
    public ResponseEntity<?> createClaim(@RequestBody ClaimEntity claim) {
        String newClaimNo = "CLM" + System.currentTimeMillis() / 1000;
        claim.setClaimNo(newClaimNo);
        claim.setClaimStatus("SUBMIT");

        int rows = claimMapper.insertClaim(claim);
        if (rows > 0) {
            notificationService.pushToRole("REVIEWER", "CLAIM",
                    "理賠新件待審",
                    "理賠案件 " + newClaimNo + " 已送件，請確認處理。",
                    newClaimNo, "SYSTEM");
            return ResponseEntity.ok(ApiResponse.ok(Map.of("claimNo", newClaimNo)));
        }
        return ResponseEntity.badRequest().body(ApiResponse.fail(400, "建立失敗"));
    }

    // 4. 更新理賠資料
    @PutMapping("/{claimNo}")
    public ResponseEntity<?> updateClaim(@PathVariable String claimNo, @RequestBody ClaimEntity claim) {
        claim.setClaimNo(claimNo);
        int rows = claimMapper.updateClaim(claim);
        if (rows > 0) {
            return ResponseEntity.ok(ApiResponse.ok("理賠案件更新成功"));
        }
        return ResponseEntity.badRequest().body(ApiResponse.fail(400, "更新失敗"));
    }

    // 5. 刪除錯誤建立的申請
    @DeleteMapping("/{claimNo}")
    public ResponseEntity<?> deleteClaim(@PathVariable String claimNo) {
        ClaimEntity currentClaim = claimMapper.selectByClaimNo(claimNo);
        if (currentClaim == null) {
            return ResponseEntity.status(404).body(ApiResponse.fail(404, "案件不存在"));
        }
        if (!"PENDING".equals(currentClaim.getClaimStatus())) {
            return ResponseEntity.badRequest().body(ApiResponse.fail(400, "只有 PENDING 狀態的案件允許刪除"));
        }

        claimMapper.deleteClaim(claimNo);
        return ResponseEntity.ok(ApiResponse.ok("案件已成功刪除"));
    }

    // 補上 1：提供前端「客戶下拉選單」的資料來源
    @GetMapping("/member-options")
    public ResponseEntity<?> getMemberOptions() {
        // 直接叫 mapper 去查出所有客戶的 ID 和姓名
        List<Map<String, Object>> members = claimMapper.selectAllMemberOptions();
        return ResponseEntity.ok(ApiResponse.ok(members));
    }

    // 補上 2：提供前端「保單下拉選單」的資料來源
    @GetMapping("/policy-options")
    public ResponseEntity<?> getPolicyOptions() {
        // 直接叫 mapper 去查出所有保單號碼、名稱以及所屬客戶 ID
        List<Map<String, Object>> policies = claimMapper.selectAllPolicyOptions();
        return ResponseEntity.ok(ApiResponse.ok(policies));
    }

    // 補上 3：提供前端「經辦人員下拉選單」的資料來源
    @GetMapping("/agent-options")
    public ResponseEntity<?> getAgentOptions() {
        List<Map<String, Object>> agents = claimMapper.selectAllAgentOptions();
        return ResponseEntity.ok(ApiResponse.ok(agents));
    }

   @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.fail(400, "請選擇要上傳的檔案"));
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

            return ResponseEntity.ok(ApiResponse.ok(result));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResponse.fail(500, "檔案上傳失敗: " + e.getMessage()));
        }
    }
}