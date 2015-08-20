package supratim.com.searcher.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import supratim.com.searcher.R;
import twitter4j.Status;

/**
 * Created by borax12 on 20/08/15.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {


    private List<Status> tweets;

    public void setData(List<Status> tweets) {
        this.tweets = tweets;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;
        public TextView handle;
        public TextView tweet;
        public TextView timeStamp;

        public ViewHolder(View v) {
            super(v);
            view= v;
        }
    }


    public SearchAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_row, parent, false);

        ViewHolder vh = new ViewHolder(v);
        vh.handle=(TextView) v.findViewById(R.id.user_handle);
        vh.tweet=(TextView) v.findViewById(R.id.tweet);
        vh.timeStamp=(TextView) v.findViewById(R.id.time_stamp);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if(tweets!=null&&!tweets.isEmpty()){

            Status tweet = tweets.get(position);
            holder.handle.setText("@"+tweet.getUser().getScreenName());
            holder.tweet.setText(tweet.getText());
            holder.timeStamp.setText(tweet.getCreatedAt().toString());

        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(tweets!=null&&!tweets.isEmpty()){
            return tweets.size();
        }
        else{
            return 0;
        }
    }
}