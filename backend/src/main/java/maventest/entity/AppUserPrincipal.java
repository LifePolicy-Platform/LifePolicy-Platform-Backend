package maventest.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.security.Principal;

@Getter
@AllArgsConstructor
public class AppUserPrincipal implements Principal {

    private final String username;
    private final String displayName;
    private final String roleCode;

    @Override
    public String getName() {
        return username;
    }
}
