package supratim.com.searcher.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import supratim.com.searcher.twitter.TwitterSearchRequester;
import supratim.com.searcher.twitter.TwitterWrapper;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by borax12 on 20/08/15.
 */
public class TwitterSearchFetch extends AsyncTask<String,Void,List<Status>> {

    private Context context;
    private TwitterSearchRequester requester;

    public TwitterSearchFetch(Context context,TwitterSearchRequester requester) {
        this.context= context;
        this.requester = requester;
    }

    @Override
    protected List<twitter4j.Status> doInBackground(String... params) {
        Twitter twitter = TwitterWrapper.getTwitterInstance(context.getApplicationContext());
        List<twitter4j.Status> tweets = null;
        try {
            Query query = new Query(params[0]);
            QueryResult result;
            result = twitter.search(query);
            tweets = result.getTweets();

        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
        }

        return tweets;
    }

    @Override
    protected void onPostExecute(List<twitter4j.Status> statuses) {
        super.onPostExecute(statuses);
        requester.finishSuccess(statuses);
    }
}
