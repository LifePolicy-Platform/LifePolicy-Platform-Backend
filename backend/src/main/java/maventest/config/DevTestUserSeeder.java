package maventest.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import maventest.entity.AppUserEntity;
import maventest.entity.CustomerInfo;
import maventest.mapper.CustomerInfoMapper;
import maventest.service.impl.AppUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 開發用：啟動時建立/更新測試帳號，並在 console 印出明文密碼。
 */
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.dev.test-user.enabled", havingValue = "true", matchIfMissing = true)
public class DevTestUserSeeder implements CommandLineRunner {

    private final AppUserRepository appUserRepository;
    private final CustomerInfoMapper customerInfoMapper;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.dev.test-user.username:testadmin}")
    private String staffUsername;

    @Value("${app.dev.test-user.password:123456}")
    private String staffPassword;

    @Value("${app.dev.test-user.display-name:測試管理員}")
    private String staffDisplayName;

    @Value("${app.dev.test-user.role-code:ADMIN}")
    private String staffRoleCode;

    @Value("${app.dev.test-user.customer-username:testuser}")
    private String customerUsername;

    @Value("${app.dev.test-user.customer-password:123456}")
    private String customerPassword;

    @Override
    public void run(String... args) {
        seedStaffUser();
        seedCustomerUser();
        printCredentials();
    }

    private void seedStaffUser() {
        String encodedPassword = passwordEncoder.encode(staffPassword);
        appUserRepository.findByUsername(staffUsername)
                .ifPresentOrElse(
                        existing -> appUserRepository.updateByUserName(AppUserEntity.builder()
                                .username(staffUsername)
                                .password(encodedPassword)
                                .displayName(staffDisplayName)
                                .roleCode(staffRoleCode)
                                .status("ACTIVE")
                                .build()),
                        () -> appUserRepository.Save(AppUserEntity.builder()
                                .username(staffUsername)
                                .password(encodedPassword)
                                .displayName(staffDisplayName)
                                .roleCode(staffRoleCode)
                                .status("ACTIVE")
                                .build())
                );
    }

    private void seedCustomerUser() {
        String encodedPassword = passwordEncoder.encode(customerPassword);
        CustomerInfo existing = customerInfoMapper.selectOne(
                new LambdaQueryWrapper<CustomerInfo>()
                        .eq(CustomerInfo::getUsername, customerUsername));

        if (existing == null) {
            CustomerInfo customer = new CustomerInfo();
            customer.setUsername(customerUsername);
            customer.setPassword(encodedPassword);
            customer.setStatus("ACTIVE");
            customerInfoMapper.insert(customer);
            return;
        }

        existing.setPassword(encodedPassword);
        existing.setStatus("ACTIVE");
        customerInfoMapper.updateById(existing);
    }

    private void printCredentials() {
        System.out.println();
        System.out.println("========== DEV TEST LOGIN ==========");
        System.out.println("[後台 tb_user]");
        System.out.println("  USERNAME : " + staffUsername);
        System.out.println("  PASSWORD : " + staffPassword);
        System.out.println("[前台 tb_cust_user]");
        System.out.println("  USERNAME : " + customerUsername);
        System.out.println("  PASSWORD : " + customerPassword);
        System.out.println("====================================");
        System.out.println();
    }
}
