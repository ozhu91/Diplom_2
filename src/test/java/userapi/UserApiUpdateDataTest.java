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
import org.junit.*;
import org.junit.rules.Timeout;

import static org.hamcrest.Matchers.equalTo;

public class UserApiUpdateDataTest extends UserApiPrototype {

    @Rule
    public Timeout globalTimeout =  Timeout.seconds(0);

    String email = "zhumzhumupdate@mail.ru";

    String password = "12345";

    String name = "Oleg";

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .build()
                .filter(new AllureRestAssured());
    }

    @After
    public void methodAfter() {
        Response login = UserLoginRequest(new UserAuthorizationData( email, password));
        String accessToken;
        if(login.thenReturn().body().as(UserAuthorizationData.class).getSuccess() == false) {
            Response registrationRequest = UserRegisterRequest(new UserAuthorizationData(email, password, name));
            accessToken = registrationRequest.thenReturn().body().as(UserAuthorizationData.class).getAccessToken();
        } else {
            accessToken = login.thenReturn().body().as(UserAuthorizationData.class).getAccessToken();
        }
        DeleteUserRequest(accessToken);
    }

    @Test
    @DisplayName("Test user email update ")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Проверка изменения email пользователя")
    public void TestUpdateUserEmail() {
        UserRegisterRequest(new UserAuthorizationData(email, password, name));
        Response authRequestBefore = UserLoginRequest(new UserAuthorizationData(email, password));
        String accessTokenBefore = authRequestBefore.thenReturn().body().as(UserAuthorizationData.class).getAccessToken();
        UpdateUserRequest(accessTokenBefore, new UserUpdateData("zhumzhumafter@mail.ru", "12345"));
        Response authRequestAfter = UserLoginRequest(new UserAuthorizationData("zhumzhumafter@mail.ru", "12345"));
        authRequestAfter
                .then()
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true));
        Assert.assertEquals("zhumzhumafter@mail.ru", authRequestAfter.thenReturn().body().as(UserAuthorizationData.class).getUser().getEmail());
        String accessTokenAfter = authRequestAfter.body().as(UserAuthorizationData.class).getAccessToken();
        DeleteUserRequest(accessTokenAfter);
    }

    @Test
    @DisplayName("Test user password update ")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Проверка изменения пароля пользователя")
    public void TestUpdateUserPassword() {
        UserRegisterRequest(new UserAuthorizationData(email, password, name));
        Response authRequestBefore = UserLoginRequest(new UserAuthorizationData(email, password));
        String accessTokenBefore = authRequestBefore.thenReturn().body().as(UserAuthorizationData.class).getAccessToken();
        UpdateUserRequest(accessTokenBefore, new UserUpdateData(email, "55555"));
        Response authRequestAfter = UserLoginRequest(new UserAuthorizationData(email, "55555"));
        Response authRequestOld = UserLoginRequest(new UserAuthorizationData(email, password));
        authRequestOld
                .then()
                .statusCode(401)
                .assertThat()
                .body("success", equalTo(false));
        String accessTokenAfter = authRequestAfter.thenReturn().body().as(UserAuthorizationData.class).getAccessToken();
        DeleteUserRequest(accessTokenAfter);
    }

    @Test
    @DisplayName("Test user data update without auth")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Проверка изменения данных пользователя без авторизации")
    public void TestUpdateUserDataWithoutAuth() {
        UserRegisterRequest(new UserAuthorizationData(email, password, name));
        Response authRequest = UserLoginRequest(new UserAuthorizationData(email, password));
        Response UpdateRequest = UpdateUserRequest(new UserUpdateData(email, "55555"));
        UpdateRequest
                .then()
                .statusCode(401)
                .assertThat()
                .body("success", equalTo(false));
        String accessToken = authRequest.thenReturn().body().as(UserAuthorizationData.class).getAccessToken();
        DeleteUserRequest(accessToken);
    }
}