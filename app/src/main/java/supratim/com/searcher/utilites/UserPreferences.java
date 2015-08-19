package supratim.com.searcher.utilites;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by borax12 on 20/08/15.
 */
public class UserPreferences {

    private static final String DATA_STORE = "data";
    private static final String PREF_KEY_TWITTER_LOGIN = "is_twitter_loggedin";
    private static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
    private static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
    private static final String PREF_USER_NAME = "twitter_user_name";

    private static SharedPreferences preferences = null;
    private Context mContext;

    private static UserPreferences userPreferencesStore = null;

    private UserPreferences(Context context) {
        preferences = context.getSharedPreferences(DATA_STORE, 0);
        mContext = context;
    }

    public static UserPreferences getInstance(Context context) {
        if (userPreferencesStore == null)
            userPreferencesStore = new UserPreferences(context);

        return userPreferencesStore;
    }

    public void storeUserTwitterLoggedIn(boolean isLoggedIn){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PREF_KEY_TWITTER_LOGIN,isLoggedIn);
        editor.apply();
    }

    public boolean isLoggedIn(){
        return preferences.getBoolean(PREF_KEY_TWITTER_LOGIN,false);
    }


    public void storeOauthToken(String oAuthToken){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_KEY_OAUTH_TOKEN,oAuthToken);
        editor.apply();
    }

    public void storeOauthSecret(String oAuthSecretKey){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_KEY_OAUTH_SECRET, oAuthSecretKey);
        editor.apply();
    }

    public String getPrefKeyOauthToken(){
        return preferences.getString(PREF_KEY_OAUTH_TOKEN,"");
    }

    public String getPrefKeyOauthSecret(){
        return preferences.getString(PREF_KEY_OAUTH_SECRET, "");
    }

}
