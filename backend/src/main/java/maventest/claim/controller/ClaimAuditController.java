package maventest.claim.controller;

import lombok.RequiredArgsConstructor;
import maventest.claim.dto.AiAnalyzeRequest;
import maventest.claim.entity.ClaimEntity;
import maventest.claim.entity.ClaimAprvLogEntity;
import maventest.claim.service.ClaimAuditService;
import maventest.common.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/claim-audit")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ClaimAuditController {

    private final ClaimAuditService claimAuditService;

    @GetMapping("/list")
    public ResponseEntity<?> getAuditList(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String policyNo) {
        List<ClaimEntity> list = claimAuditService.getAuditList(status, policyNo);
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @PutMapping("/decision")
    public ResponseEntity<?> makeAuditDecision(@RequestBody Map<String, Object> payload) {
        try {
            claimAuditService.makeAuditDecision(payload);
            return ResponseEntity.ok(ApiResponse.ok("審核核決處理完成"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.fail(400, "核決處理失敗: " + e.getMessage()));
        }
    }

    @GetMapping("/logs/{claimNo}")
    public ResponseEntity<?> getClaimLogs(@PathVariable String claimNo) {
        List<ClaimAprvLogEntity> logs = claimAuditService.getClaimLogs(claimNo);
        return ResponseEntity.ok(ApiResponse.ok(logs));
    }

    // 🌟 一鍵 AI 智慧醫理分析端點
    @PostMapping("/ai-analyze")
    public ResponseEntity<?> analyzeClaimWithAi(@RequestBody AiAnalyzeRequest request) {
        try {
            String analysisResult = claimAuditService.analyzeClaimWithAi(request);
            return ResponseEntity.ok(ApiResponse.ok(analysisResult));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.fail(400, "AI 分析失敗: " + e.getMessage()));
        }
    }
}