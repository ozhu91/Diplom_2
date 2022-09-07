package userapi;

import api.client.UserApiPrototype;
import api.model.user.UserAuthorizationData;
import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.hamcrest.Matchers.equalTo;


public class UserApiRegistrationTest extends UserApiPrototype {

    @Rule
    public Timeout globalTimeout =  Timeout.seconds(0);

    String email = "zhumzhumreg@mail.ru";

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
    @DisplayName("Test registration user")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Проверка регистрации пользователя")
    public void TestRegistrationNewUser() {
        Response registrationRequest = UserRegisterRequest(new UserAuthorizationData(email, password, name));
        registrationRequest
                .then()
                .statusCode(200)
                .and()
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Test registration created user")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверка регистрации уже зарегистрированного пользователя")
    public void TestRegistrationCreatedUser(){
        UserRegisterRequest(new UserAuthorizationData(email, password, name));
        Response registrationRequest = UserRegisterRequest(new UserAuthorizationData(email, password, name));
        registrationRequest
                .then()
                .statusCode(403)
                .and()
                .assertThat()
                .body("success", equalTo(false))
                .and().assertThat().body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Test registration user without 1 parameter")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверка регистрации пользователя без указания 1 поля")
    public void TestRegistrationUserWithoutOneParameter() {
        Response registrationRequest = UserRegisterRequest(new UserAuthorizationData(email, password));
        registrationRequest
                .then()
                .statusCode(403)
                .and()
                .assertThat()
                .body("success", equalTo(false))
                .and().assertThat().body("message", equalTo("Email, password and name are required fields"));
    }
}