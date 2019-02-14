package one.marcomass.ezeat.models;

public class Restaurant {

    private final String name;
    private final String address;
    private final float minOrder;
    private final String imageURL;
    private final int ID;

    public Restaurant(int ID, String name, String address, float minOrder, String imageURL) {
        this.name = name;
        this.address = address;
        this.minOrder = minOrder;
        this.imageURL = imageURL;
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public float getMinOrder() {
        return minOrder;
    }

    public int getID() {
        return ID;
    }

    public String getImageURL() {
        return imageURL;
    }
}
