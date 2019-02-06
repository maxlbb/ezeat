package one.marcomass.ezeat.models;

public class Restaurant {

    private final String name;
    private final String description;
    private final int ID;

    public Restaurant(int ID, String name, String description) {
        this.name = name;
        this.description = description;
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getID() {
        return ID;
    }
}
