package api.client;

import api.model.order.IngredientsList;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderApiPrototype {


    /*
     *  Get ingredients
     * */
    public Response GetIngredientsRequest() {
        return given()
                .headers("Content-type", "application/json")
                .when()
                .get("/api/ingredients");
    }

    /*
     * Create order
     * */
    public Response CreateOrderRequest(String accessToken, IngredientsList ingredients) {
        return given()
                .headers("Content-type", "application/json", "Authorization", accessToken)
                .and()
                .body(ingredients)
                .when()
                .post("/api/orders");
    }

    /*
     * Create order without auth
     * */
    public Response CreateOrderRequest (IngredientsList ingredients) {
        return given()
                .headers("Content-type", "application/json")
                .and()
                .body(ingredients)
                .when()
                .post("/api/orders");
    }

    /*
     * Create order without auth
     * */
    public Response CreateOrderRequest() {
        return given()
                .headers("Content-type", "application/json")
                .when()
                .post("/api/orders");
    }

    /*
     * Get order
     * */
    public Response GetOrderRequest(String accessToken) {
        return given()
                .headers("Content-type", "application/json", "Authorization", accessToken)
                .when()
                .get("/api/orders");
    }

    /*
     * Get order without auth
     * */
    public Response GetOrderRequest() {
        return given()
                .headers("Content-type", "application/json")
                .when()
                .get("/api/orders");
    }


}


