package maventest.policyapplication.infrastructure.repository.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustUserMapper {

    Long findMemberIdByIdentityCard(String identityCard);

    String findNameByMemberId(Long memberId);

    String findUsernameByMemberId(Long memberId);
}