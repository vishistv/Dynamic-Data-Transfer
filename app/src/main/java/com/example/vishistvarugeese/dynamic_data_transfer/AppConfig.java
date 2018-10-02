package com.example.vishistvarugeese.dynamic_data_transfer;

/**
 * Created by Vishist Varugeese on 27-12-2017.
 */

public class AppConfig {
    public static String ip = "192.168.43.110";
    // Server user login url
    public static String URL_LOGIN = "http://" + ip + "/dynamic_data/login.php";

    // Server user register url
    public static String URL_REGISTER = "http://" + ip + "/dynamic_data/register.php";

    //Server get user from log url
    public static String URL_GET_LOG = "http://" + ip + "/dynamic_data/log.php";

    //Server store user in log url
    public static String URL_STORE_LOG = "http://" + ip + "/dynamic_data/storeLog.php";

    //Server get all user accounts
    public static String URL_USER_ACCOUNTS = "http://" + ip + "/dynamic_data/userAccounts.php";

    //Server request reset password
    public static String URL_REQ_RESET_PASS = "http://" + ip + "/dynamic_data/resetPassword.php";
}
