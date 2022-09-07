package api.model.user;

import java.util.List;

public class UserCreateRequest {

    private List<String> ingredients;

    public UserCreateRequest(List<String> ingredients) {
        this.ingredients = ingredients;
    }
    public UserCreateRequest() {}

    public List<String> getIngredients() {
        return this.ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}
