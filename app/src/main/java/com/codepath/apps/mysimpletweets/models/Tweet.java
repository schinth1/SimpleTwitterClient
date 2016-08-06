package com.codepath.apps.mysimpletweets.models;

import android.text.format.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by sc043016 on 8/4/16.
 */
public class Tweet {
    //list out the attributes
    private String body;
    private long uid; // unique id for the tweet
    private User user;
    private String createdAt;
    private long timestamp;

    //deserialize the JSON and build Tweet objects


    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public User getUser() {
        return user;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public  static Tweet fromJSON(JSONObject jsonObject) {
        Tweet tweet = new Tweet();

        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
            tweet.timestamp = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy").parse(tweet.createdAt).getTime();


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return tweet;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<>();

        for(int i = 0; i<jsonArray.length(); i++)
        {
            try {
                JSONObject tweetJson = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJSON(tweetJson);
                if (tweet != null) {
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }

        }

        return tweets;
    }

    public String getTimeSpan() {
        long elapsed = Math.max(System.currentTimeMillis() - timestamp, 0);
        String timeSpan = "";
        if (elapsed >= DateUtils.YEAR_IN_MILLIS) {
            timeSpan = String.valueOf(elapsed / DateUtils.YEAR_IN_MILLIS) + "y";
        }
        else if (elapsed >= DateUtils.WEEK_IN_MILLIS) {
            timeSpan = String.valueOf(elapsed / DateUtils.WEEK_IN_MILLIS) + "w";
        }
        else if (elapsed >= DateUtils.DAY_IN_MILLIS) {
            timeSpan = String.valueOf(elapsed / DateUtils.DAY_IN_MILLIS) + "d";
        }
        else if (elapsed >= DateUtils.HOUR_IN_MILLIS) {
            timeSpan = String.valueOf(elapsed / DateUtils.HOUR_IN_MILLIS) + "h";
        }
        else if (elapsed >= DateUtils.MINUTE_IN_MILLIS) {
            timeSpan = String.valueOf(elapsed / DateUtils.MINUTE_IN_MILLIS) + "m";
        }
        else {
            timeSpan = String.valueOf(elapsed / DateUtils.SECOND_IN_MILLIS) + "s";
        }
        return timeSpan;
    }

    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
