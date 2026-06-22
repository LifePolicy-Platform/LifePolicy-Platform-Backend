package maventest.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import maventest.entity.PolicyAprvLogEntity;
import maventest.mapper.PolicyAprvLogMapper;

@Component
@RequiredArgsConstructor
public class PolicyAprvLogAppender {

    private final PolicyAprvLogMapper policyAprvLogMapper;

    public void append(String policyNo, String aprvStatus, String aprvUser, String aprvRemark) {
        policyAprvLogMapper.insert(PolicyAprvLogEntity.builder()
                .policyNo(policyNo)
                .aprvStatus(aprvStatus)
                .aprvUser(aprvUser)
                .aprvTime(LocalDateTime.now())
                .aprvRemark(aprvRemark)
                .build());
    }
}
