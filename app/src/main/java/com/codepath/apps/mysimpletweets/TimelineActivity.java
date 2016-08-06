package com.codepath.apps.mysimpletweets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    private static final String TAG = TimelineActivity.class.getSimpleName();

    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter aTweets;
    private ListView lvTweets;

    private final int TWEET_REQUEST_CODE = 1;
    private final int TWEET_RESPONSE_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        //find the listView
        lvTweets = (ListView)findViewById(R.id.lvTweets);
        //create teh arraylist(data source)
        tweets = new ArrayList<>();
        //construct the adapter the data source
        aTweets = new TweetsArrayAdapter(this, tweets);
        //connect adapter to list view
        lvTweets.setAdapter(aTweets);

        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                populateTimeline(null, tweets.get(totalItemsCount - 1).getUid() - 1);
                return true;
            }
        });
        //Get the client
        client = TwitterApplication.getRestClient();
        populateTimeline(null, null);
        //getNextTweets(null, null);
    }

    public void populateTimeline(Long since, Long max_id) {
        //long max_id = tweets.get(tweets.size()-1).getUid();
        client.getHomeTimeLine(since, max_id, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.i(TAG, response.toString());
                tweets.addAll(Tweet.fromJSONArray(response));
                aTweets.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i(TAG, responseString.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.i(TAG, errorResponse.toString());
            }
        });

    }


    //Send an API request to get the timeline json
    //Fill the listview by creating the tweet objects from json
//    private void populateTimeline(Long since, Long max_id) {
//        //client.getHomeTimeLine(new JsonHttpResponseHandler( ){
//        client.getHomeTimeLine(since, max_id, new JsonHttpResponseHandler(){
//            //SUCCESS
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
//                //deserialize the JSON
//                //CREATE MODELS AND ADD THEM TO THE ADAPTER
//                //LOAD THE MODEL DATA INTO LISTVIEW
//                aTweets.addAll(Tweet.fromJSONArray(json));
//            }
//
//            //FAILURE
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                Log.d("DEBUG", errorResponse.toString());
//            }
//        });
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.timeline, menu);

        MenuItem tweetItem = (MenuItem) menu.findItem(R.id.mnuTweet);
        tweetItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
                startActivityForResult(i, TWEET_REQUEST_CODE);
                return true;
            }
        });
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == TWEET_RESPONSE_CODE && requestCode == TWEET_REQUEST_CODE) {
            //Post and Get Updated Timeline
            String tweet = data.getExtras().getString("tweet");
            client.postUpdate(tweet, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.i("TWEET", response.toString());
                    tweets.add(0, Tweet.fromJSON(response));
                    aTweets.notifyDataSetChanged();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.i(TAG, responseString);
                }
            });
        }
    }
}
