package com.example.seamon.sikkacodingchallenge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
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

public class InstagramActivity extends AppCompatActivity {
    private static final String TAG = "InstagramActivity";

    private RequestQueue mQueue;
    private ArrayList<InstagramObject> instagramObjList = new ArrayList<>();
    private ArrayList<String> linkUrlList = new ArrayList<>(); //for checking duplicates

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instagram);

        mQueue = Volley.newRequestQueue(this);

        getInstagramImages();

        //enable back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Deals with Instagram API call by getting image url, link url, and like count of each photo.
     * Then, updates the recycler view with images from ‘sikkalife’, ‘practicemobilizer’ &
     * ‘selfdrivingpractice’ and ‘sikkasoftware’ tags.
     */
    private void getInstagramImages(){
        //init recycler view
        RecyclerView instagramRecyclerView = findViewById(R.id.instagram_recyclerView);
        final InstagramRecyclerViewAdaper adapter = new InstagramRecyclerViewAdaper(this, instagramObjList);
        instagramRecyclerView.setAdapter(adapter);
        instagramRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        ArrayList<String> apiUrlList = new ArrayList<>();
        apiUrlList.add("https://api.instagram.com/v1/tags/sikkasoftware/media/recent?access_token=8802462623.ba4c844.0faf4614002847648b4e724a3e4ffe94");
        apiUrlList.add("https://api.instagram.com/v1/tags/sikkalife/media/recent?access_token=8802462623.ba4c844.0faf4614002847648b4e724a3e4ffe94");
        apiUrlList.add("https://api.instagram.com/v1/tags/practicemobilizer/media/recent?access_token=8802462623.ba4c844.0faf4614002847648b4e724a3e4ffe94");
        apiUrlList.add("https://api.instagram.com/v1/tags/selfdrivingpractice/media/recent?access_token=8802462623.ba4c844.0faf4614002847648b4e724a3e4ffe94");

        for(String url: apiUrlList) {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        JSONArray dataArray = response.getJSONArray("data");

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataObject = dataArray.getJSONObject(i);
                            JSONObject imagesObject = dataObject.getJSONObject("images");
                            String currentImgUrl = imagesObject.getJSONObject("thumbnail").getString("url");
                            String currentLinkUrl = dataObject.getString("link");
                            Integer currentLikeCount = dataObject.getJSONObject("likes").getInt("count");

                            //check if the link exists or not. If the link is already existed, then skip
                            if (!linkUrlList.contains(currentLinkUrl)) {
                                linkUrlList.add(currentLinkUrl);

                                //assign value to the instagram obj
                                InstagramObject currentInstagramObject = new InstagramObject();
                                currentInstagramObject.setImgUrl(currentImgUrl);
                                currentInstagramObject.setLinkUrl(currentLinkUrl);
                                currentInstagramObject.setLikeCount(currentLikeCount);

                                instagramObjList.add(currentInstagramObject);
                            }

                        }

                        //sort the arrayList by likes
                        Collections.sort(instagramObjList, new Comparator<InstagramObject>() {
                            @Override
                            public int compare(InstagramObject o1, InstagramObject o2) {
                                return o2.getLikeCount().compareTo(o1.getLikeCount());
                            }
                        });

                        for (InstagramObject obj : instagramObjList) {
                            Log.d(TAG, "onResponse: currentLikeCount: " + obj.getLikeCount() + ", currentLinkUrl: " + obj.getLinkUrl());
                        }

                        //update the recycler view
                        adapter.notifyDataSetChanged();

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
