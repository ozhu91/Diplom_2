package orderapi;

import api.client.OrderApiPrototype;
import api.client.UserApiPrototype;
import api.model.order.Ingredients;
import api.model.order.IngredientsList;
import api.model.user.UserAuthorizationData;
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

import java.util.ArrayList;

import static org.hamcrest.Matchers.equalTo;

public class OrderApiCreateOrderTest {

    @Rule
    public Timeout globalTimeout =  Timeout.seconds(0);

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
    @DisplayName("Test create order")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Создание заказа")
    public void TestCreateOrder() {
        Response registrationRequest = userApi.UserRegisterRequest(new UserAuthorizationData(email, password, name));
        String accessToken = registrationRequest.thenReturn().body().as(UserAuthorizationData.class).getAccessToken();
        Ingredients ingredientsResponse = orderApi.GetIngredientsRequest().thenReturn().body().as(Ingredients.class);
        ArrayList<String> ingredients = new ArrayList<>();
        String firstIngredient = ingredientsResponse.getData()[1].get_id();
        String SecondIngredient = ingredientsResponse.getData()[2].get_id();
        ingredients.add(firstIngredient);
        ingredients.add(SecondIngredient);

        Response createOrderResponse = orderApi.CreateOrderRequest(accessToken, new IngredientsList(ingredients));
        createOrderResponse
                .then()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Test create order without auth")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Создание заказа без входа в систему")
    public void TestCreateOrderWithoutAuth(){
        Ingredients ingredientsResponse = orderApi.GetIngredientsRequest().thenReturn().body().as(Ingredients.class);
        ArrayList<String> ingredients = new ArrayList<>();
        String firstIngredient = ingredientsResponse.getData()[1].get_id();
        String SecondIngredient = ingredientsResponse.getData()[2].get_id();
        ingredients.add(firstIngredient);
        ingredients.add(SecondIngredient);

        Response createOrderResponse = orderApi.CreateOrderRequest(new IngredientsList(ingredients));
        createOrderResponse
                .then()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Test create order with incorrect ingredient")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Создание заказа с некорректным ингредиентом")
    public void TestCreateOrderWithIncorrectIngredient(){
        Ingredients ingredientsResponse = orderApi.GetIngredientsRequest().thenReturn().body().as(Ingredients.class);
        ArrayList<String> ingredients = new ArrayList<>();
        String firstIngredient = ingredientsResponse.getData()[1].get_id();
        String SecondIngredient = ingredientsResponse.getData()[2].get_id();
        ingredients.add(firstIngredient + "abs");
        ingredients.add(SecondIngredient);

        Response createOrderResponse = orderApi.CreateOrderRequest(new IngredientsList(ingredients));
        createOrderResponse
                .then()
                .statusCode(500);
    }

    @Test
    @DisplayName("Test create order without ingredient")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Создание заказа без ингредиентов в систему")
    public void TestCreateOrderWithoutIngredient(){
        Response createOrderResponse = orderApi.CreateOrderRequest();
        createOrderResponse
                .then()
                .statusCode(400)
                .and()
                .body("success", equalTo(false));
    }


}