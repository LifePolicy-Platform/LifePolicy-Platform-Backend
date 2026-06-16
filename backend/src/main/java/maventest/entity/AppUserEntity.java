package maventest.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUserEntity {

    private Long id;
    private String username;
    private String passwordHash;
    private String displayName;
    private boolean enabled;
    private List<String> roles;
}