package maventest.mapper;

import maventest.entity.ClaimEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ClaimMapper {
    // 條件查詢（動態 SQL）
    List<ClaimEntity> selectClaimList(
        @Param("status") String status, 
        @Param("policyNo") String policyNo, 
        @Param("applyDate") String applyDate
    );
    
    ClaimEntity selectByClaimNo(String claimNo);
    
    int insertClaim(ClaimEntity claim);
    
    int updateClaim(ClaimEntity claim);
    
    int deleteClaim(String claimNo);
}