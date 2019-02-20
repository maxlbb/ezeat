package one.marcomass.ezeat.models;

public class User {

    private String _id;
    private String username;
    private String password;
    private String email;
    private boolean confirmed;
    private boolean blocked;
    private String provider;

    public User(String _id, String username, String password, boolean confirmed, boolean blocked, String provider, String email) {
        this._id = _id;
        this.username = username;
        this.password = password;
        this.confirmed = confirmed;
        this.blocked = blocked;
        this.provider = provider;
        this.email = email;
    }

    public String get_id() {
        return _id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public String getProvider() {
        return provider;
    }

    public String getEmail() {
        return email;
    }
}
