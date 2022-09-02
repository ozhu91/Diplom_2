package api.client;

import api.model.user.UserAuthorizationData;
import api.model.user.UserUpdateData;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserApiPrototype {

    /*
     * User register
     * */
    public Response UserRegisterRequest(UserAuthorizationData data) {
        return given()
                .headers("Content-type", "application/json")
                .and()
                .body(data)
                .when()
                .post("/api/auth/register");
    }

    /*
     * User login
     * */
    public Response UserLoginRequest(UserAuthorizationData data) {
        return given()
                .headers("Content-type", "application/json")
                .and()
                .body(data)
                .when()
                .post("/api/auth/login");
    }


    /*
     * User delete
     * */
    public Response DeleteUserRequest(String accessToken) {
        return given()
                .headers(
                        "Authorization", accessToken, "Content-type", "application/json", "Accept",
                        "/" )
                .when()
                .delete("/api/auth/user");
    }

    /*
     * User update
     * */
    public Response UpdateUserRequest(String accessToken, UserUpdateData data) {
        return given()
                .headers(
                        "Authorization", accessToken, "Content-type", "application/json", "Accept",
                        "/" )
                .and()
                .body(data)
                .when()
                .patch("/api/auth/user");
    }

    /*
     * User update without auth
     * */
    public Response UpdateUserRequest(UserUpdateData data) {
        return given()
                .headers(
                        "Content-type", "application/json", "Accept",
                        "/" )
                .and()
                .body(data)
                .when()
                .patch("/api/auth/user");
    }


    /*
     * User getting data
     * */
    public Response GetUserRequest(String accessToken) {
        return given()
                .headers(
                        "Authorization", accessToken, "Content-type", "application/json", "Accept",
                        "/" )
                .when()
                .get("/api/auth/user");
    }

}
