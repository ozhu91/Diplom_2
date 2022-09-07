package orderapi;

import api.client.OrderApiPrototype;
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
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.hamcrest.Matchers.equalTo;

public class OrderApiGetOrderTest {

    @Rule
    public Timeout globalTimeout =  Timeout.seconds(120);

    OrderApiPrototype orderApi = new OrderApiPrototype();
    UserApiPrototype userApi = new UserApiPrototype();

    String email = "zhumzhumorder@mail.ru";
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
        Response login = userApi.UserLoginRequest(new UserAuthorizationData( email, password));
        String accessToken;
        if(login.thenReturn().body().as(UserAuthorizationData.class).getSuccess() == false) {
            Response registrationRequest = userApi.UserRegisterRequest(new UserAuthorizationData(email, password, name));
            accessToken = registrationRequest.thenReturn().body().as(UserAuthorizationData.class).getAccessToken();
        } else {
            accessToken = login.thenReturn().body().as(UserAuthorizationData.class).getAccessToken();
        }
        userApi.DeleteUserRequest(accessToken);
    }

    @Test
    @DisplayName("Test get user order")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Получение заказа пользователя")
    public void TestGetUserOrder() {
        Response registrationRequest = userApi.UserRegisterRequest(new UserAuthorizationData(email, password, name));
        String accessToken = registrationRequest.thenReturn().body().as(UserAuthorizationData.class).getAccessToken();
        Response createOrderResponse = orderApi.GetOrderRequest(accessToken);
        createOrderResponse
                .then()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
        userApi.DeleteUserRequest(accessToken);
    }

    @Test
    @DisplayName("Test get user order without auth")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Получение заказа пользователя")
    public void TestGetUserOrderWithputAuth() {
        Response createOrderResponse = orderApi.GetOrderRequest();
        createOrderResponse
                .then()
                .statusCode(401)
                .and()
                .body("success", equalTo(false));
    }


}
