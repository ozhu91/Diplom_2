package api.client;

import api.model.order.IngredientsList;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderApiPrototype {


    @Step("Запрос на получение всех ингредиентов пользователя")
    public Response GetIngredientsRequest() {
        return given()
                .headers("Content-type", "application/json")
                .when()
                .get("/api/ingredients");
    }

    @Step("Запрос на создание заказа для пользователя")
    public Response CreateOrderRequest(String accessToken, IngredientsList ingredients) {
        return given()
                .headers("Content-type", "application/json", "Authorization", accessToken)
                .and()
                .body(ingredients)
                .when()
                .post("/api/orders");
    }

    @Step("Запрос на создание заказа без авторизации")
    public Response CreateOrderRequest (IngredientsList ingredients) {
        return given()
                .headers("Content-type", "application/json")
                .and()
                .body(ingredients)
                .when()
                .post("/api/orders");
    }

    @Step("Запрос на создание заказа для пользователя без авторизации")
    public Response CreateOrderRequest() {
        return given()
                .headers("Content-type", "application/json")
                .when()
                .post("/api/orders");
    }

    /*
     * Get order
     * */
    @Step("Запрос на заказы для пользователя")
    public Response GetOrderRequest(String accessToken) {
        return given()
                .headers("Content-type", "application/json", "Authorization", accessToken)
                .when()
                .get("/api/orders");
    }

    @Step("Запрос на заказы для пользователя без авторизации")
    public Response GetOrderRequest() {
        return given()
                .headers("Content-type", "application/json")
                .when()
                .get("/api/orders");
    }
}


