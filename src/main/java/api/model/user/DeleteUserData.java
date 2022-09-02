package api.model.user;

public class DeleteUserData {


    private String accessToken;

    public DeleteUserData(String accessToken) {
        this.accessToken = accessToken;
    }

    public DeleteUserData() {
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}
