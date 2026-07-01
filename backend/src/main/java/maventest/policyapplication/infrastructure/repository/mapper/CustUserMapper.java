package maventest.policyapplication.infrastructure.repository.mapper;

import maventest.policyapplication.interfaces.dto.MemberProfileRespDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CustUserMapper {

    Long findMemberIdByIdentityCard(String identityCard);

    MemberProfileRespDto findActiveProfileByIdentityCard(@Param("identityCard") String identityCard);

    String findNameByMemberId(Long memberId);

    String findUsernameByMemberId(Long memberId);
}