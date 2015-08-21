package supratim.com.searcher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import supratim.com.searcher.activities.SearchActivity;
import supratim.com.searcher.activities.WebViewActivity;
import supratim.com.searcher.utilites.UserPreferences;
import supratim.com.searcher.utilites.Utilities;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class MainActivity extends AppCompatActivity {

    private ImageButton button;
    private Context context;

    public static final int REQUEST_CODE = 100;


    //Twitter API Objects
    private static Twitter twitter;
    private static RequestToken requestToken;


    //Twitter API Specific keys
    private static String consumerKey;
    private static String consumerSecret;
    private static String callbackUrl=null;
    private String oAuthVerifier = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_down, R.anim.abc_fade_out);
        setContentView(R.layout.activity_main);
        context =this;

        /* Enabling strict mode - Just for reducing the effort to use the AsyncTwitterFactory*/
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setupKeys();

        button = (ImageButton)findViewById(R.id.login_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Utilities.isNetworkAvailable(context)){
                    Utilities.showToast(context.getResources().getString(R.string.network_unavailable),context);
                }
                else{
                    performInitTasks();
                }
            }
        });

    }

    private void performInitTasks() {

        //if the user Logged in , take to the search UI
        if(UserPreferences.getInstance(getApplicationContext()).isLoggedIn()){
            Intent searchViewIntent = new Intent(this, SearchActivity.class);
            startActivity(searchViewIntent);
        }
        else{
            login();
        }
    }

    private void login() {
        final ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(consumerKey);
        builder.setOAuthConsumerSecret(consumerSecret);

        final Configuration configuration = builder.build();
        final TwitterFactory factory = new TwitterFactory(configuration);
        twitter = factory.getInstance();


        try {
            requestToken = twitter.getOAuthRequestToken(callbackUrl);

            /**
             *  webview for authorization
             *  Once authorized, results are received at onActivityResult
             *  */
            final Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra(WebViewActivity.EXTRA_URL, requestToken.getAuthenticationURL());
            startActivityForResult(intent, REQUEST_CODE);

        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    private void setupKeys() {

        try{
            consumerKey = Utilities.loadPropties(context).getProperty("twitter_consumer_key");
            consumerSecret = Utilities.loadPropties(context).getProperty("twitter_consumer_secret");
            callbackUrl = Utilities.loadPropties(context).getProperty("twitter_callback");
        }
        catch(Exception e){
            Log.d("KeysException",e.getLocalizedMessage());
        }
        oAuthVerifier = getString(R.string.twitter_oauth_verifier);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            String verifier = data.getExtras().getString(oAuthVerifier);
            try {
                AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);

                saveDetails(accessToken);

            } catch (Exception e) {
                Log.e("Twitter Login Failed", e.getMessage());
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void saveDetails(AccessToken accessToken) {

        if(accessToken!=null){
            UserPreferences.getInstance(getApplicationContext()).storeOauthToken(accessToken.getToken());
            UserPreferences.getInstance(getApplicationContext()).storeOauthSecret(accessToken.getTokenSecret());
            UserPreferences.getInstance(getApplicationContext()).storeUserTwitterLoggedIn(true);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
