package maventest.policyapplication.application.internal;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import maventest.policyapplication.infrastructure.repository.InsuranceApplicationRepository;

@Service
@RequiredArgsConstructor
public class PolicyNumberGenerator {

    private static final String POLICY_PREFIX = "POL";
    private static final String LIST_PREFIX = "L";
    private static final DateTimeFormatter PERIOD_FORMAT = DateTimeFormatter.ofPattern("yyyyMM");
    private static final int SEQUENCE_LENGTH = 4;

    private final InsuranceApplicationRepository insuranceApplicationRepository;

    public String generatePolicyNo(LocalDateTime baseTime) {
        return generateNumber(POLICY_PREFIX, baseTime, insuranceApplicationRepository::findMaxPolicyNoByPrefix);
    }

    public String generateListNo(LocalDateTime baseTime) {
        return generateNumber(LIST_PREFIX, baseTime, insuranceApplicationRepository::findMaxListNoByPrefix);
    }

    private String generateNumber(
            String prefix,
            LocalDateTime baseTime,
            Function<String, String> maxNumberFinder
    ) {
        String period = baseTime.format(PERIOD_FORMAT);
        String fullPrefix = prefix + period;
        String maxNo = maxNumberFinder.apply(fullPrefix);
        int nextSeq = resolveNextSequence(maxNo, fullPrefix);
        return fullPrefix + String.format("%0" + SEQUENCE_LENGTH + "d", nextSeq);
    }

    private int resolveNextSequence(String maxNo, String prefix) {
        if (maxNo == null || maxNo.length() <= prefix.length()) {
            return 1;
        }
        String seqPart = maxNo.substring(prefix.length());
        return Integer.parseInt(seqPart) + 1;
    }
}
