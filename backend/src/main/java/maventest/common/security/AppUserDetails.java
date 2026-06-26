package maventest.common.security;

import maventest.customer.entity.CustomerInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class AppUserDetails implements UserDetails {

    private final CustomerInfo customer;

    public AppUserDetails(CustomerInfo customer) {
        this.customer = customer;
    }

    public CustomerInfo getCustomer() {
        return customer;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return customer.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return customer.getUsername();
    }

    @Override
    public boolean isEnabled() {
        return Integer.valueOf(1).equals(customer.getStatus());
    }
}
