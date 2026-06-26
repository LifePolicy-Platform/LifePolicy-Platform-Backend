package maventest.auth.dto;

import java.sql.Date;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailUpdateResponse {
    
    private Long id;
    private String name;
    private String username;
    private String password;
    private String identityCard;
    private String gender;
    private String email;
    private String mobile;
    private Date birthday;
    private String status;
    private LocalDateTime createTime;
}
