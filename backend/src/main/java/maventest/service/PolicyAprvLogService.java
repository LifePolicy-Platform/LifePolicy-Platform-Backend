package maventest.service;

import java.util.List;

import maventest.dto.PolicyAprvLogList;

public interface PolicyAprvLogService {

    /**
     * 依保單號碼查詢審核歷程（依審核時間由舊到新）。
     */
    List<PolicyAprvLogList> findByPolicyNo(String policyNo);
}
