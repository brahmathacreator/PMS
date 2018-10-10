package controllers.constants;

public class Validation {

    public final static String app_generic_alpha_numeric_special_pattern = "^[A-Za-z0-9 _,\\.\\/\\-]{2,200}$";
    public final static String app_generic_numeric_pattern = "^[0-9]{1,10}$";
    public final static String app_generic_alpha_pattern = "^[A-Za-z_]{2,200}$";
    public final static String app_generic_alpha_numeric_pattern = "^[A-Za-z0-9_\\ ]{2,100}$";
    public final static String app_password_pattern = "((?=.*\\\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#%]).{8,16})";
    public final static String app_phone_pattern = "^[0-9]{8}$|^[0-9]{10}$";

    public final static long max_user_logo_size_mb = 1;
    public final static long max_user_attachment_size_mb = 30;
    public final static int DEFAULT_PAGE_SIZE = 15;
}
