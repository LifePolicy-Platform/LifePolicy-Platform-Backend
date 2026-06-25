package maventest.policy.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import maventest.policy.dto.PolicyAprvLogList;
import maventest.policy.mapper.PolicyAprvLogMapper;
import maventest.policy.service.PolicyAprvLogService;

@Service
@RequiredArgsConstructor
public class PolicyAprvLogServiceImpl implements PolicyAprvLogService {

    private final PolicyAprvLogMapper policyAprvLogMapper;

    @Override
    public List<PolicyAprvLogList> findByPolicyNo(String policyNo) {
        return policyAprvLogMapper.selectByPolicyNo(policyNo);
    }


}
