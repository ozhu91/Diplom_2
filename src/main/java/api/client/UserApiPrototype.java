package api.client;

import api.model.user.UserAuthorizationData;
import api.model.user.UserUpdateData;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserApiPrototype {

    @Step("Запрос на регистрацию пользователя")
    public Response UserRegisterRequest(UserAuthorizationData data) {
        return given()
                .headers("Content-type", "application/json")
                .and()
                .body(data)
                .when()
                .post("/api/auth/register");
    }

    @Step("Запрос на авторизация пользователя пользователя")
    public Response UserLoginRequest(UserAuthorizationData data) {
        return given()
                .headers("Content-type", "application/json")
                .and()
                .body(data)
                .when()
                .post("/api/auth/login");
    }


    @Step("Запрос на удаление пользователя")
    public Response DeleteUserRequest(String accessToken) {
        return given()
                .headers(
                        "Authorization", accessToken, "Content-type", "application/json", "Accept",
                        "/" )
                .when()
                .delete("/api/auth/user");
    }

    @Step("Запрос на обновление данных пользователя")
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

    @Step("Запрос на обновление данных пользователя без access token")
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
}
