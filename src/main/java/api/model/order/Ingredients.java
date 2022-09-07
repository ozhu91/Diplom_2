package api.model.order;

public class Ingredients {

    private boolean success;
    private Ingredient[] data;

    public Ingredients() {

    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Ingredient[] getData() {
        return data;
    }

    public void setData(Ingredient[] data) {
        this.data = data;
    }
}
