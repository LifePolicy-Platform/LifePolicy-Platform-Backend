package maventest.Controller;

import lombok.RequiredArgsConstructor;
import maventest.common.ApiResponse;
import maventest.entity.ClaimEntity;
import maventest.entity.ClaimAprvLogEntity;
import maventest.mapper.ClaimMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/claim-audit")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ClaimAuditController {

    private final ClaimMapper claimMapper;

    // 1. 取得待審核理賠清單
    @GetMapping("/list")
    public ResponseEntity<?> getAuditList(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String policyNo) {
        List<ClaimEntity> list = claimMapper.selectClaimAuditList(status, policyNo);
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    // 2. 理賠案件審核核決 (同意 APPROVED / 駁回 REJECTED / 撤回 RETURN)
    // @PutMapping("/decision")
    // @Transactional(rollbackFor = Exception.class)
    // public ResponseEntity<?> makeAuditDecision(@RequestBody Map<String, Object> payload) {
    //     String claimNo = (String) payload.get("claimNo");
    //     String action = (String) payload.get("action"); // APPROVED, REJECTED, RETURN
    //     String remark = (String) payload.get("remark");
    //     String user = (String) payload.get("aprvUser") != null ? (String) payload.get("aprvUser") : "SYSTEM_AUDITOR";
        
    //     BigDecimal aprvAmount = null;
    //     if (payload.get("approveAmount") != null) {
    //         aprvAmount = new BigDecimal(payload.get("approveAmount").toString());
    //     }

    //     // A. 查出原案件資訊
    //     ClaimEntity claim = claimMapper.selectByClaimNo(claimNo);
    //     if (claim == null) {
    //         return ResponseEntity.badRequest().body(ApiResponse.fail(400, "找不到該案件"));
    //     }

    //     // B. 變更主表狀態
    //     claim.setClaimStatus(action);
    //     claim.setApproveAmount(aprvAmount);
    //     claim.setRemark(remark);
    //     claim.setUpdateUser(user);
    //     claimMapper.updateClaim(claim);

    //     // C. 同步寫入歷程 Log 表
    //     ClaimAprvLogEntity log = new ClaimAprvLogEntity();
    //     log.setClaimNo(claimNo);
    //     log.setPolicyNo(claim.getPolicyNo());
    //     log.setClaimStatus(action);
    //     log.setApproveAmount(aprvAmount);
    //     log.setAprvRemark(remark);
    //     log.setAprvUser(user);
    //     claimMapper.insertAprvLog(log);

    //     return ResponseEntity.ok(ApiResponse.ok("審核核決處理完成"));
    // }

    @PutMapping("/decision")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> makeAuditDecision(@RequestBody Map<String, Object> payload) {
        String claimNo = (String) payload.get("claimNo");
        String action = (String) payload.get("action"); // APPROVED, REJECTED, RETURN, PENDING
        String remark = (String) payload.get("remark");
        String user = (String) payload.get("aprvUser") != null ? (String) payload.get("aprvUser") : "SYSTEM_AUDITOR";
        
        BigDecimal aprvAmount = null;
        if (payload.get("approveAmount") != null) {
            aprvAmount = new BigDecimal(payload.get("approveAmount").toString());
        }

        // A. 查出原案件資訊
        ClaimEntity claim = claimMapper.selectByClaimNo(claimNo);
        if (claim == null) {
            return ResponseEntity.badRequest().body(ApiResponse.fail(400, "找不到該案件"));
        }

        // B. 變更主表狀態
        // 這裡不需要額外判斷，直接把 action (PENDING/APPROVED/...) 傳入即可
        claim.setClaimStatus(action); 
        claim.setApproveAmount(aprvAmount);
        claim.setRemark(remark);
        claim.setUpdateUser(user);
        
        // 這裡利用你原本的 updateClaim 方法
        claimMapper.updateClaim(claim);

        // C. 同步寫入歷程 Log 表
        ClaimAprvLogEntity log = new ClaimAprvLogEntity();
        log.setClaimNo(claimNo);
        log.setPolicyNo(claim.getPolicyNo());
        log.setClaimStatus(action);
        log.setApproveAmount(aprvAmount);
        log.setAprvRemark(remark);
        log.setAprvUser(user);
        claimMapper.insertAprvLog(log);

        return ResponseEntity.ok(ApiResponse.ok("審核核決處理完成，狀態更新為: " + action));
    }

    // 3. 獲取單一理賠案件的歷史軌跡履歷
    @GetMapping("/logs/{claimNo}")
    public ResponseEntity<?> getClaimLogs(@PathVariable String claimNo) {
        List<ClaimAprvLogEntity> logs = claimMapper.selectAprvLogHistory(claimNo);
        return ResponseEntity.ok(ApiResponse.ok(logs));
    }
}