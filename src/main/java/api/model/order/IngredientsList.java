package api.model.order;

import java.util.ArrayList;
public class IngredientsList {

    private ArrayList<String> ingredients = new ArrayList<>();

    public IngredientsList(ArrayList<String> ingredients) {
        for(String ingredient : ingredients) {
            this.ingredients.add(ingredient);
        }
    }
    public ArrayList<String> getIngredientsList() {
        return ingredients;
    }

    public void setIngredientsList(ArrayList<String> ingredientsList) {
        this.ingredients = ingredientsList;
    }

}
