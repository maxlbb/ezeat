package one.marcomass.ezeat.models;

public class SignResponse {

    private String jwt;
    private User user;

    public SignResponse(String jwt, User user) {
        this.jwt = jwt;
        this.user = user;
    }

    public String getJwt() {
        return jwt;
    }

    public User getUser() {
        return user;
    }
}
