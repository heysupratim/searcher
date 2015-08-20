package supratim.com.searcher.twitter;

import android.content.Context;

import supratim.com.searcher.R;
import supratim.com.searcher.utilites.UserPreferences;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by borax12 on 20/08/15.
 */
public class TwitterWrapper {


    public static Twitter getTwitterInstance (Context context){
        ConfigurationBuilder cb = new ConfigurationBuilder();

        String consumerKey = context.getResources().getString(R.string.twitter_consumer_key);
        String consumerSecret = context.getResources().getString(R.string.twitter_consumer_secret);

        String OAuthToken = UserPreferences.getInstance(context).getPrefKeyOauthToken();
        String OAuthSecret = UserPreferences.getInstance(context).getPrefKeyOauthSecret();

        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerSecret)
                .setOAuthAccessToken(OAuthToken)
                .setOAuthAccessTokenSecret(OAuthSecret);
        TwitterFactory tf = new TwitterFactory(cb.build());
        return tf.getInstance();
    }

}
