//package com.example.uberclone;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//
//import java.net.URLDecoder;
//import java.util.HashMap;
//
//public class SessionManager {
//
//    SharedPreferences sharedPreferences;
//    public SharedPreferences.Editor editor;
//    public Context context;
//    int PRIVATE_MODE = 0;
//
//    private static final String PREF_NAME = "LOGIN";
//    private static final String LOGIN = "IS_LOGIN";
//    private static final String USER_ID = "USER_ID";
//    private static final String USER_TYPE = "USER_TYPE";
//    private static final String USER_PHONE = "USER_PHONE";
//    private static final String COUNTRY_CODE = "COUNTRY_CODE";
//
//    public SessionManager(Context context)
//    {
//        this.context = context;
//        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
//        editor = sharedPreferences.edit();
//    }
//
//    public boolean isLogin()
//    {
//        return sharedPreferences.getBoolean(LOGIN, false);
//    }
//    public void createSessions(String user_id, String user_type, String user_phone, String country_code)
//    {
//        editor.putBoolean(LOGIN, true);
//        editor.putString(USER_ID, user_id);
//        editor.putString(USER_TYPE, user_type);
//        editor.putString(USER_PHONE, user_phone);
//        editor.putString(COUNTRY_CODE, country_code);
//        editor.apply();
//    }
//
//    public HashMap<String, String> getUserDetails()
//    {
//        HashMap<String,String> user = new HashMap<>();
//        user.put(USER_ID, sharedPreferences.getString(USER_ID,null));
//        user.put(USER_TYPE, sharedPreferences.getString(USER_TYPE,null));
//        user.put(USER_PHONE, sharedPreferences.getString(USER_PHONE,null));
//        user.put(COUNTRY_CODE, sharedPreferences.getString(COUNTRY_CODE,null));
//
//        return user;
//    }
//}
