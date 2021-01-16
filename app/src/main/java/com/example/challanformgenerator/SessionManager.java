package com.example.challanformgenerator;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    private static final String IS_LOGIN="IsLoggedIn";
    public static final String USER_ID="user_id";
    public static final String USER_ROLE="user_role";
    public static final String TOKEN="SESSION_TOKEN";
    public SessionManager(Context context)
    {
        sharedPreferences = context.getSharedPreferences("userLoginSession",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    /**
     * Setting up Session in Local Device
     * */
    public void createLoginSession(User user)
    {
        editor.putBoolean(IS_LOGIN,true);
        editor.putString(USER_ID,user.userID);
        editor.putString(TOKEN,user.token);
        editor.putString(USER_ROLE,user.role);
        editor.commit();
    }
    public HashMap<String,String> getUserDetails()
    {
        HashMap<String,String> userData = new HashMap<String,String>();
        userData.put(USER_ID,sharedPreferences.getString(USER_ID,null));
        userData.put(USER_ROLE,sharedPreferences.getString(USER_ROLE,null));
        userData.put(TOKEN,sharedPreferences.getString(TOKEN,null));
        return userData;
    }
    /**
     * Setting API TOKEN
     * */
    public void setToken(String api)
    {
        editor.putBoolean(IS_LOGIN,true);
        editor.putString(TOKEN,api);
        editor.commit();
    }
    /**
     * Get API TOKEN
     * */
    public String getToken()
    {
        return sharedPreferences.getString(TOKEN,null);
    }
    public String getUserrole()
    {
        return sharedPreferences.getString(USER_ROLE,null);
    }
    public Boolean isLoggedIn()
    {
        if(sharedPreferences.getBoolean(IS_LOGIN,false))
        {
            return true;
        }
        return false;
    }
    public void logout()
    {
        editor.remove(IS_LOGIN);
        editor.remove(USER_ID);
        editor.remove(USER_ROLE);
        editor.commit();
    }
}
