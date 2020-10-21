package sb.learn.authjwt.payload.request;

import javax.validation.constraints.NotBlank;

/**
 * @author Symplice BONI
 * project auth-jwt
 * date 21/10/2020
 */
public class LoginRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
