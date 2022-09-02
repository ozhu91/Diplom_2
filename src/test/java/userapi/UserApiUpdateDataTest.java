package userapi;

import api.client.UserApiPrototype;
import api.model.user.UserAuthorizationData;
import api.model.user.UserUpdateData;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.hamcrest.Matchers.equalTo;

public class UserApiUpdateDataTest extends UserApiPrototype {

    @Rule
    public Timeout globalTimeout =  Timeout.seconds(0);

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .build()
                .filter(new AllureRestAssured());
    }

    @Test
    @DisplayName("Test user email update ")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Проверка изменения email пользователя")
    public void TestUpdateUserEmail() {
        UserRegisterRequest(new UserAuthorizationData("zhumzhum@mail.ru", "12345", "oleg"));
        Response authRequestBefore = UserLoginRequest(new UserAuthorizationData("zhumzhum@mail.ru", "12345"));
        String accessTokenBefore = authRequestBefore.body().as(UserAuthorizationData.class).getAccessToken();
        Response UpdateRequest = UpdateUserRequest(accessTokenBefore, new UserUpdateData("zhumzhumafter@mail.ru", "12345"));
        // Проверка на корректность ответа запроса на изменение email пользователя
        UpdateRequest
                .then()
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true));
        Response authRequestAfter = UserLoginRequest(new UserAuthorizationData("zhumzhumafter@mail.ru", "12345"));
        // Проверка на корректность ответа запроса авторизации пользователя под новым email
        authRequestAfter
                .then()
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true));
        Assert.assertEquals("zhumzhumafter@mail.ru", authRequestAfter.body().as(UserAuthorizationData.class).getUser().getEmail());
        Response authRequestOld = UserLoginRequest(new UserAuthorizationData("zhumzhum@mail.ru", "12345"));
        // Проверка на отсутствие пользователя под старым email
        authRequestOld
                .then()
                .statusCode(401)
                .assertThat()
                .body("success", equalTo(false));
        String accessTokenAfter = authRequestAfter.body().as(UserAuthorizationData.class).getAccessToken();
        DeleteUserRequest(accessTokenAfter);
    }

    @Test
    @DisplayName("Test user password update ")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Проверка изменения пароля пользователя")
    public void TestUpdateUserPassword() {
        UserRegisterRequest(new UserAuthorizationData("zhumzhum@mail.ru", "12345", "oleg"));
        Response authRequestBefore = UserLoginRequest(new UserAuthorizationData("zhumzhum@mail.ru", "12345"));
        String accessTokenBefore = authRequestBefore.body().as(UserAuthorizationData.class).getAccessToken();
        // Проверка на корректность ответа запроса на изменение пароля  пользователя
        Response UpdateRequest = UpdateUserRequest(accessTokenBefore, new UserUpdateData("zhumzhum@mail.ru", "55555"));
        UpdateRequest
                .then()
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true));
        // Проверка на корректность ответа запроса авторизации пользователя под новым паролем
        Response authRequestAfter = UserLoginRequest(new UserAuthorizationData("zhumzhum@mail.ru", "55555"));
        authRequestAfter
                .then()
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true));
        // Проверка на отсутствие пользователя под старым паролем
        Response authRequestOld = UserLoginRequest(new UserAuthorizationData("zhumzhum@mail.ru", "12345"));
        authRequestOld
                .then()
                .statusCode(401)
                .assertThat()
                .body("success", equalTo(false));
        String accessTokenAfter = authRequestAfter.body().as(UserAuthorizationData.class).getAccessToken();
        DeleteUserRequest(accessTokenAfter);
    }

    @Test
    @DisplayName("Test user data update without auth")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Проверка изменения данных пользователя без авторизации")
    public void TestUpdateUserDataWithoutAuth() {
        UserRegisterRequest(new UserAuthorizationData("zhumzhum@mail.ru", "12345", "oleg"));
        Response authRequest = UserLoginRequest(new UserAuthorizationData("zhumzhum@mail.ru", "12345"));
        // Проверка на корректность ответа запроса на изменение пароля  пользователя
        Response UpdateRequest = UpdateUserRequest(new UserUpdateData("zhumzhum@mail.ru", "55555"));
        UpdateRequest
                .then()
                .statusCode(401)
                .assertThat()
                .body("success", equalTo(false));

        String accessToken = authRequest.body().as(UserAuthorizationData.class).getAccessToken();
        DeleteUserRequest(accessToken);
    }
}
