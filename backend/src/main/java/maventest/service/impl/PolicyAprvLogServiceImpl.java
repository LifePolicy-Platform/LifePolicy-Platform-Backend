package maventest.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import maventest.dto.PolicyAprvLogList;
import maventest.mapper.PolicyAprvLogMapper;
import maventest.service.PolicyAprvLogService;

@Service
@RequiredArgsConstructor
public class PolicyAprvLogServiceImpl implements PolicyAprvLogService {

    private final PolicyAprvLogMapper policyAprvLogMapper;

    @Override
    public List<PolicyAprvLogList> findByPolicyNo(String policyNo) {
        return policyAprvLogMapper.selectByPolicyNo(policyNo);
    }

    
}
