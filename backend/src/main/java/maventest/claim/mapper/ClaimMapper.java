package maventest.claim.mapper;

import maventest.claim.entity.ClaimAprvLogEntity;
import maventest.claim.entity.ClaimEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface ClaimMapper {
    // 條件查詢（動態 SQL）
    List<ClaimEntity> selectClaimList(
        @Param("status") String status,
        @Param("policyNo") String policyNo,
        @Param("applyDate") String applyDate
    );

    ClaimEntity selectByClaimNo(@Param("claimNo") String claimNo);

    int insertClaim(ClaimEntity claim);

    int updateClaim(ClaimEntity claim);

    int deleteClaim(@Param("claimNo") String claimNo);

    List<Map<String, Object>> selectAllMemberOptions();
    List<Map<String, Object>> selectAllPolicyOptions();

    // 查詢專供審核的清單
    List<ClaimEntity> selectClaimAuditList(@Param("status") String status, @Param("policyNo") String policyNo);

    // 寫入審核歷史紀錄 Log
    int insertAprvLog(ClaimAprvLogEntity logEntity);

    // 查詢某個案件的歷史審核歷程
    List<ClaimAprvLogEntity> selectAprvLogHistory(@Param("claimNo") String claimNo);

    List<Map<String, Object>> selectAllAgentOptions();
<<<<<<< HEAD
}
=======

    // 查出最近一次把該案件設為 PENDING 的業務 username（用於 RETURN/REJECTED 通知）
    String findPendingAprvUser(@Param("claimNo") String claimNo);
}
>>>>>>> develop
