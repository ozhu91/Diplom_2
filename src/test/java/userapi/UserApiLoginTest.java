package userapi;

import api.client.UserApiPrototype;
import api.model.user.UserAuthorizationData;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.hamcrest.Matchers.equalTo;

public class UserApiLoginTest extends UserApiPrototype {

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
    @DisplayName("Test login user")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Проверка авторизации пользователя")
    public void TestLoginUser() throws InterruptedException {
        UserRegisterRequest(new UserAuthorizationData("zhumzhum@mail.ru", "12345", "oleg"));
        Response authRequest = UserLoginRequest(new UserAuthorizationData("zhumzhum@mail.ru", "12345"));
        authRequest
                .then()
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true));
        String accessToken = authRequest.body().as(UserAuthorizationData.class).getAccessToken();
        DeleteUserRequest(accessToken);
    }

    @Test
    @DisplayName("Test login user uncorrect")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверка авторизации несуществующего пользователя")
    public void TestLoginUserUncorrect() throws InterruptedException{
        UserRegisterRequest(new UserAuthorizationData("zhumzhum@mail.ru", "12345", "oleg"));
        Response authRequest = UserLoginRequest(new UserAuthorizationData("zhumzhum@mail.ru", "12345"));
        String accessToken = authRequest.body().as(UserAuthorizationData.class).getAccessToken();
        DeleteUserRequest(accessToken);
        Response uncorrectAuthRequest = UserLoginRequest(new UserAuthorizationData("zhumzhum@mail.ru", "12345"));
        uncorrectAuthRequest
                .then()
                .statusCode(401)
                .assertThat()
                .body("success", equalTo(false))
                .and()
                .assertThat()
                .body("message", equalTo("email or password are incorrect"));
    }
}
