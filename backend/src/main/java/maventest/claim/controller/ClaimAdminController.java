package maventest.claim.controller;

import maventest.claim.dto.ClaimCreateRequest;
import maventest.claim.dto.ClaimUpdateRequest;
import maventest.claim.dto.OptionResponse;
import maventest.claim.entity.ClaimEntity;
import maventest.claim.service.ClaimService;
import maventest.common.ApiResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/claim")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ClaimAdminController {

    private final ClaimService claimService;

    @GetMapping("/list")
    public ResponseEntity<?> getClaimList(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String policyNo,
            @RequestParam(required = false) String applyDate) {
        List<ClaimEntity> list = claimService.getClaimList(status, policyNo, applyDate);
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @GetMapping("/{claimNo}")
    public ResponseEntity<?> getClaimDetail(@PathVariable String claimNo) {
        ClaimEntity claim = claimService.getClaimDetail(claimNo);
        if (claim == null) {
            return ResponseEntity.status(404).body(ApiResponse.fail(404, "找不到該理賠案件"));
        }
        return ResponseEntity.ok(ApiResponse.ok(claim));
    }

    @PostMapping
    public ResponseEntity<?> createClaim(@RequestBody ClaimCreateRequest request) {
        try {
            String claimNo = claimService.createClaim(request);
            return ResponseEntity.ok(ApiResponse.ok(Map.of("claimNo", claimNo)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.fail(400, "建立失敗: " + e.getMessage()));
        }
    }

    @PutMapping("/{claimNo}")
    public ResponseEntity<?> updateClaim(
            @PathVariable String claimNo, 
            @RequestBody ClaimUpdateRequest request) {
        try {
            claimService.updateClaim(claimNo, request);
            return ResponseEntity.ok(ApiResponse.ok("理賠案件更新成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.fail(400, "更新失敗: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{claimNo}")
    public ResponseEntity<?> deleteClaim(@PathVariable String claimNo) {
        try {
            claimService.deleteClaim(claimNo);
            return ResponseEntity.ok(ApiResponse.ok("案件已成功刪除"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.fail(400, e.getMessage()));
        }
    }

    @GetMapping("/member-options")
    public ResponseEntity<?> getMemberOptions() {
        List<OptionResponse> options = claimService.getMemberOptions();
        return ResponseEntity.ok(ApiResponse.ok(options));
    }

    @GetMapping("/policy-options")
    public ResponseEntity<?> getPolicyOptions() {
        List<OptionResponse> options = claimService.getPolicyOptions();
        return ResponseEntity.ok(ApiResponse.ok(options));
    }

    @GetMapping("/agent-options")
    public ResponseEntity<?> getAgentOptions() {
        List<OptionResponse> options = claimService.getAgentOptions();
        return ResponseEntity.ok(ApiResponse.ok(options));
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            Map<String, String> response = claimService.uploadFile(file);
            return ResponseEntity.ok(ApiResponse.ok(response));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail(500, e.getMessage()));
        }
    }
}