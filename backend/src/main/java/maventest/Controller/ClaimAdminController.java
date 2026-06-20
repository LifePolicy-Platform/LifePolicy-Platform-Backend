package maventest.Controller;

import maventest.entity.ClaimEntity;
import maventest.mapper.ClaimMapper;
import maventest.common.ApiResponse; // 確定匯入你們的 ApiResponse

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/claim")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") 
public class ClaimAdminController {

    private final ClaimMapper claimMapper;

    // 1. 查詢理賠申請清單
    @GetMapping("/list")
    public ResponseEntity<?> getClaimList(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String policyNo,
            @RequestParam(required = false) String applyDate) {
        List<ClaimEntity> list = claimMapper.selectClaimList(status, policyNo, applyDate);
        
        // ⭕ 使用標準的 ok()
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    // 2. 查看理賠詳情
    @GetMapping("/{claimNo}")
    public ResponseEntity<?> getClaimDetail(@PathVariable String claimNo) {
        ClaimEntity claim = claimMapper.selectByClaimNo(claimNo);
        if (claim == null) {
            // ⭕ 改用 fail(code, message)
            return ResponseEntity.status(404).body(ApiResponse.fail(404, "找不到該理賠案件"));
        }
        return ResponseEntity.ok(ApiResponse.ok(claim));
    }

    // 3. 新增理賠紀錄
    @PostMapping
    public ResponseEntity<?> createClaim(@RequestBody ClaimEntity claim) {
        String newClaimNo = "CLM" + System.currentTimeMillis() / 1000;
        claim.setClaimNo(newClaimNo);
        claim.setClaimStatus("PENDING"); 
        
        int rows = claimMapper.insertClaim(claim);
        if (rows > 0) {
            return ResponseEntity.ok(ApiResponse.ok(Map.of("claimNo", newClaimNo)));
        }
        // ⭕ 改用 fail(code, message)
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
        // ⭕ 改用 fail(code, message)
        return ResponseEntity.badRequest().body(ApiResponse.fail(400, "更新失敗"));
    }

    // 5. 刪除錯誤建立的申請
    @DeleteMapping("/{claimNo}")
    public ResponseEntity<?> deleteClaim(@PathVariable String claimNo) {
        ClaimEntity currentClaim = claimMapper.selectByClaimNo(claimNo);
        if (currentClaim == null) {
            // ⭕ 改用 fail(code, message)
            return ResponseEntity.status(404).body(ApiResponse.fail(404, "案件不存在"));
        }
        if (!"PENDING".equals(currentClaim.getClaimStatus())) {
            // ⭕ 改用 fail(code, message)
            return ResponseEntity.badRequest().body(ApiResponse.fail(400, "只有 PENDING 狀態的案件允許刪除"));
        }
        
        claimMapper.deleteClaim(claimNo);
        return ResponseEntity.ok(ApiResponse.ok("案件已成功刪除"));
    }

    // 🌟 補上 1：提供前端「客戶下拉選單」的資料來源
    @GetMapping("/member-options")
    public ResponseEntity<?> getMemberOptions() {
        // 直接叫 mapper 去查出所有客戶的 ID 和姓名
        List<Map<String, Object>> members = claimMapper.selectAllMemberOptions();
        return ResponseEntity.ok(ApiResponse.ok(members));
    }

    // 🌟 補上 2：提供前端「保單下拉選單」的資料來源
    @GetMapping("/policy-options")
    public ResponseEntity<?> getPolicyOptions() {
        // 直接叫 mapper 去查出所有保單號碼、名稱以及所屬客戶 ID
        List<Map<String, Object>> policies = claimMapper.selectAllPolicyOptions();
        return ResponseEntity.ok(ApiResponse.ok(policies));
    }
}