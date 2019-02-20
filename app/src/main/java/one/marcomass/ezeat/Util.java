package one.marcomass.ezeat;

import android.util.Patterns;

public class Util {

    public final static String RESTAURANT_ID = "one.marcomass.ezeat.restaurant_id";
    public final static int REQUEST_LOGIN = 7001;
    public final static int REQUEST_LOGOUT = 7002;
    public final static int LOGIN_SUCCESSFULL = 2001;
    public final static int LOGIN_ERROR = 5001;

    public final static String TOKEN = "one.marcomass.ezeat.token";
    public final static String PREFERENCES = "one.marcomass.ezeat.preferences";
    public final static String USERNAME = "one.marcomass.ezeat.username";
    public final static String EMAIL = "one.marcomass.ezeat.email";

    public static boolean verifyEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean verifyPassword(String password) {
        return (password.length() > 5);
    }

    public static boolean verifyNumber(String number) {
        return Patterns.PHONE.matcher(number).matches() && number.length() > 6 && number.length() <= 13 ;
    }

    public static boolean verifyUsername(String username) {
        return (username.length() > 4);
    }
}
