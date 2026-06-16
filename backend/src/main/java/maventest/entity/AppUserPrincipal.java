package maventest.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.security.Principal;
import java.util.List;

@Getter
@AllArgsConstructor
public class AppUserPrincipal implements Principal {

    private final String username;
    private final String displayName;
    private final List<String> roles;

    @Override
    public String getName() {
        return username;
    }
}