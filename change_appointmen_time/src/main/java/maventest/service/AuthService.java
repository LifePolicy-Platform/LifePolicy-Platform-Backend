package maventest.service;

import maventest.dto.auth.CurrentUserResponse;
import maventest.dto.auth.LoginRequest;
import maventest.dto.auth.LoginResponseData;

public interface AuthService {

    /** 驗證帳密，成功則回傳 JWT 與使用者資訊 */
    LoginResponseData login(LoginRequest request);

    /** 根據已認證的 username 回傳目前使用者資訊 */
    CurrentUserResponse currentUser(String username);
}
