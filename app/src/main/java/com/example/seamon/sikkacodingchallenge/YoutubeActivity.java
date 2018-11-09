package com.example.seamon.sikkacodingchallenge;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class YoutubeActivity extends AppCompatActivity {
    private static final String TAG = "YoutubeActivity";
    private String API_KEY = "AIzaSyB9upIxNC2LjANOzh4r28BezoZMsV1FIWA";
    private ArrayList<YoutubeObject> youtubeObjList = new ArrayList<>();
    private RequestQueue mQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);

        mQueue = Volley.newRequestQueue(this);

        getAllVideosId();

        //enable back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    /**
     * This method parse all playlist video Ids from the API call, then combine them all together into this string format: id1,id2,id3,id4
     *
     */
    private void getAllVideosId(){
        String url = "https://www.googleapis.com/youtube/v3/playlistItems?playlistId=UU_-I_H2iR4MaAghRSDzVBCQ&maxResults=25&part=snippet%2CcontentDetails&key=AIzaSyB9upIxNC2LjANOzh4r28BezoZMsV1FIWA";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String allIds = "";
                    ArrayList<String> idList = new ArrayList<>();

                    JSONArray itemsArray = response.getJSONArray("items");

                    for(int i = 0; i < itemsArray.length(); i++){
                        JSONObject itemObject = itemsArray.getJSONObject(i);
                        JSONObject snippetObject = itemObject.getJSONObject("snippet");
                        JSONObject resourceIdObject = snippetObject.getJSONObject("resourceId");
                        String videoId = resourceIdObject.getString("videoId");
                        idList.add(videoId);

                        //assign value to the youtube obj
                        YoutubeObject currentYoutubeOjb = new YoutubeObject();
                        currentYoutubeOjb.setChannel(snippetObject.getString("channelTitle"));
                        currentYoutubeOjb.setTitle(snippetObject.getString("title"));
                        currentYoutubeOjb.setDescription(snippetObject.getString("description"));
                        currentYoutubeOjb.setThumbnailUrl(snippetObject.getJSONObject("thumbnails").getJSONObject("medium").getString("url"));
                        currentYoutubeOjb.setId(videoId);

                        //add current youtube object to the objects list
                        youtubeObjList.add(currentYoutubeOjb);
                        //now we have everything except views count

                        //concat ids together to build the api url that has views count
                        if(i !=  itemsArray.length()-1) {
                            allIds = allIds + videoId + ",";
                        }
                        else{//if it is the last url, then don't put "," at the end
                            allIds = allIds + videoId;
                        }
                    }

                    //build the api link that contain view count information for all videos
                    String videoInfoApiLink = "https://www.googleapis.com/youtube/v3/videos?part=contentDetails,statistics&id=" + allIds + "&key=" + API_KEY;
                    Log.d(TAG, "onResponse: new API link: " + videoInfoApiLink);

                    getViewCount(videoInfoApiLink);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);

    }


    /**
     * Assign video's views count to the youtubeObj then start the recycler view
     * This function is being called in getAllVideosId();
     * @param url: url of the videoInfoApiLink that contain information of each video
     */
    private void  getViewCount(String url){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray itemsArray = response.getJSONArray("items");

                    for(int i = 0; i < itemsArray.length(); i++){
                        JSONObject itemObject = itemsArray.getJSONObject(i);
                        JSONObject statisticsObject = itemObject.getJSONObject("statistics");

                        //assign view count
                        youtubeObjList.get(i).setViewsCount(statisticsObject.getInt("viewCount"));

                    }

                    //sort the arrayList by view
                    Collections.sort(youtubeObjList, new Comparator<YoutubeObject>() {
                        @Override
                        public int compare(YoutubeObject o1, YoutubeObject o2) {
                            return o2.getViewsCount().compareTo(o1.getViewsCount());
                        }
                    });

                    for(YoutubeObject obj : youtubeObjList){
                        Log.d(TAG, "onResponse: title : " + obj.getTitle() + " has view: " + obj.getViewsCount() );
                    }

                    //start recycler view
                    initRecyclerView();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
    }

    private void initRecyclerView(){
        RecyclerView youtubeRecyclerView = findViewById(R.id.youtube_recyclerView);
        YoutubeRecyclerViewAdapter adapter = new YoutubeRecyclerViewAdapter(this, youtubeObjList);
        youtubeRecyclerView.setAdapter(adapter);
        youtubeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    //for back button on action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
