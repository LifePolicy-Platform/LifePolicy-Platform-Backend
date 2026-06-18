package maventest.common.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import maventest.entity.CustomerInfo;
import maventest.mapper.CustomerInfoMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final CustomerInfoMapper customerInfoMapper;

    public AppUserDetailsService(CustomerInfoMapper customerInfoMapper) {
        this.customerInfoMapper = customerInfoMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomerInfo customer = customerInfoMapper.selectOne(
                new LambdaQueryWrapper<CustomerInfo>()
                        .eq(CustomerInfo::getUsername, username)
        );
        if (customer == null || !customer.isEnabled()) {
            throw new UsernameNotFoundException("帳號不存在或已停用：" + username);
        }
        return new AppUserDetails(customer);
    }
}
