package api.model.user;

public class UserUpdateData {
    private String email;

    private String password;

    public UserUpdateData(String email, String password){
        this.password = password;
        this.email = email;
    }

    public UserUpdateData() {}
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
}
