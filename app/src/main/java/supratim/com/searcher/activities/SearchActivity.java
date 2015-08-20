package supratim.com.searcher.activities;

import android.animation.Animator;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.util.List;

import supratim.com.searcher.R;
import supratim.com.searcher.adapters.SearchAdapter;
import supratim.com.searcher.asyncTasks.TwitterSearchFetch;
import supratim.com.searcher.twitter.TwitterSearchRequester;
import twitter4j.Status;

public class SearchActivity extends AppCompatActivity implements TwitterSearchRequester {

    private LinearLayoutManager mLayoutManager;
    private SearchView searchView;
    private RecyclerView mRecyclerView;
    private SearchAdapter searchAdapter;
    private SearchListener searchListener;
    private TwitterSearchFetch searchFetchTask;
    private Context context;
    private CircularProgressView progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_up, R.anim.abc_fade_out);
        setContentView(R.layout.search_activity);

        final View view = findViewById(R.id.main_search_view);

        context = this;

        //find views
        mRecyclerView = (RecyclerView) findViewById(R.id.result_recycler);
        //mRecyclerView.setVisibility(View.INVISIBLE);
        searchView = (SearchView) findViewById(R.id.twitter_search);
        progressView = (CircularProgressView) findViewById(R.id.progress);

        searchListener = new SearchListener();
        searchView.setOnQueryTextListener(searchListener);

        searchAdapter = new SearchAdapter();
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(searchAdapter);


    }


    void enterReveal(View myView) {

        // get the center for the clipping circle
        int cx = myView.getMeasuredWidth() / 2;
        int cy = myView.getMeasuredHeight() / 2;

        // get the final radius for the clipping circle
        int finalRadius = Math.max(myView.getWidth(), myView.getHeight()) / 2;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // create the animator for this view (the start radius is zero)
            Animator anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);

            // make the view visible and start the animation
            myView.setVisibility(View.VISIBLE);
            anim.start();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_activity, menu);
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

    @Override
    public void finishSuccess(List<Status> statuses) {
        searchAdapter.setData(statuses);
        searchAdapter.notifyDataSetChanged();
        enterReveal(mRecyclerView);
        searchFetchTask.cancel(true);
        stopProgress();
    }

    private class SearchListener implements SearchView.OnQueryTextListener {

        @Override
        public boolean onQueryTextSubmit(String query) {

            if (!query.isEmpty()) {
                hideKeyboard();
                startProgress();
                String[] params = new String[]{query};
                searchFetchTask = new TwitterSearchFetch(context, SearchActivity.this);
                searchFetchTask.execute(params);
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            if (newText.isEmpty()) {
                searchAdapter.setData(null);
                searchAdapter.notifyDataSetChanged();
            }
            return false;
        }
    }

    private void startProgress() {
        progressView.setVisibility(View.VISIBLE);
        progressView.startAnimation();
    }

    private void stopProgress() {
        progressView.setVisibility(View.GONE);
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = SearchActivity.this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
