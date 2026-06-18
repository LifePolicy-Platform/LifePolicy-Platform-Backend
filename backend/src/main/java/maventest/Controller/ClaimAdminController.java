package maventest.Controller;

import maventest.entity.ClaimEntity;
import maventest.mapper.ClaimMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/claim")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // 避免跨域阻擋
public class ClaimAdminController {

    private final ClaimMapper claimMapper;

    // 1. 查詢理賠申請清單
    @GetMapping("/list")
    public ResponseEntity<?> getClaimList(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String policyNo,
            @RequestParam(required = false) String applyDate) {
        List<ClaimEntity> list = claimMapper.selectClaimList(status, policyNo, applyDate);
        return ResponseEntity.ok(Map.of("code", 200, "data", list));
    }

    // 2. 查看理賠詳情
    @GetMapping("/{claimNo}")
    public ResponseEntity<?> getClaimDetail(@PathVariable String claimNo) {
        ClaimEntity claim = claimMapper.selectByClaimNo(claimNo);
        if (claim == null) {
            return ResponseEntity.status(404).body(Map.of("code", 404, "message", "找不到該理賠案件"));
        }
        return ResponseEntity.ok(Map.of("code", 200, "data", claim));
    }

    // 3. 新增理賠紀錄（電話受理）
    @PostMapping
    public ResponseEntity<?> createClaim(@RequestBody ClaimEntity claim) {
        // 簡單生成唯一序號（格式範例：CLM + 時間戳記）
        String newClaimNo = "CLM" + System.currentTimeMillis() / 1000;
        claim.setClaimNo(newClaimNo);
        claim.setClaimStatus("PENDING"); // 預設初始狀態
        
        int rows = claimMapper.insertClaim(claim);
        if (rows > 0) {
            return ResponseEntity.ok(Map.of("code", 200, "message", "理賠案件建立成功", "claimNo", newClaimNo));
        }
        return ResponseEntity.badRequest().body(Map.of("code", 400, "message", "建立失敗"));
    }

    // 4. 更新理賠資料
    @PutMapping("/{claimNo}")
    public ResponseEntity<?> updateClaim(@PathVariable String claimNo, @RequestBody ClaimEntity claim) {
        claim.setClaimNo(claimNo);
        int rows = claimMapper.updateClaim(claim);
        if (rows > 0) {
            return ResponseEntity.ok(Map.of("code", 200, "message", "理賠案件更新成功"));
        }
        return ResponseEntity.badRequest().body(Map.of("code", 400, "message", "更新失敗"));
    }

    // 5. 刪除錯誤建立的申請（限 PENDING）
    @DeleteMapping("/{claimNo}")
    public ResponseEntity<?> deleteClaim(@PathVariable String claimNo) {
        ClaimEntity currentClaim = claimMapper.selectByClaimNo(claimNo);
        if (currentClaim == null) {
            return ResponseEntity.status(404).body(Map.of("code", 404, "message", "案件不存在"));
        }
        if (!"PENDING".equals(currentClaim.getClaimStatus())) {
            return ResponseEntity.badRequest().body(Map.of("code", 400, "message", "只有 PENDING 狀態的案件允許刪除"));
        }
        
        claimMapper.deleteClaim(claimNo);
        return ResponseEntity.ok(Map.of("code", 200, "message", "案件已成功刪除"));
    }
}