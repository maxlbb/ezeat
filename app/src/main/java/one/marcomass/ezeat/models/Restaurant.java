package one.marcomass.ezeat.models;

public class Restaurant {

    private final String name;
    private final String description;

    public Restaurant(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
