package api.model.user;

public class UserAuthorizationData {

    private User user;

    private String email;

    private String password;

    private String name;

    private boolean success;

    private String accessToken;

    private String refreshToken;

    public UserAuthorizationData(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public UserAuthorizationData(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
    public UserAuthorizationData() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
