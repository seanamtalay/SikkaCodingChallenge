package com.example.seamon.sikkacodingchallenge

import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.MenuItem
import android.widget.ArrayAdapter

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList
import java.util.Collections
import java.util.Comparator

class YoutubeActivity : AppCompatActivity() {
    private val API_KEY = "AIzaSyB9upIxNC2LjANOzh4r28BezoZMsV1FIWA"
    private val youtubeObjList = ArrayList<YoutubeObject>()
    private lateinit var mQueue: RequestQueue
    private val TAG = "YoutubeActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube)

        mQueue = Volley.newRequestQueue(this)

        getAllVideosId()

        //enable back button
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


    }

    /**
     * This method parse all playlist video Ids from the API call, then combine them all together into this string format: id1,id2,id3,id4
     *
     */
    private fun getAllVideosId() {
        val url = "https://www.googleapis.com/youtube/v3/playlistItems?playlistId=UU_-I_H2iR4MaAghRSDzVBCQ&maxResults=25&part=snippet%2CcontentDetails&key=AIzaSyB9upIxNC2LjANOzh4r28BezoZMsV1FIWA"

        val request = JsonObjectRequest(Request.Method.GET, url, null, Response.Listener { response ->
            try {
                var allIds = ""
                val idList = ArrayList<String>()

                val itemsArray = response.getJSONArray("items")

                for (i in 0 until itemsArray.length()) {
                    val itemObject = itemsArray.getJSONObject(i)
                    val snippetObject = itemObject.getJSONObject("snippet")
                    val resourceIdObject = snippetObject.getJSONObject("resourceId")
                    val videoId = resourceIdObject.getString("videoId")
                    idList.add(videoId)

                    //assign value to the youtube obj
                    val title = snippetObject.getString("title")
                    val description = snippetObject.getString("description")

                    val channelTitle = snippetObject.getString("channelTitle")
                    val thumbnailUrl = snippetObject.getJSONObject("thumbnails").getJSONObject("medium").getString("url")

                    val currentYoutubeOjb = YoutubeObject(title, description, channelTitle, thumbnailUrl, videoId)

                    //add current youtube object to the objects list
                    youtubeObjList.add(currentYoutubeOjb)
                    //now we have everything except views count

                    //concat ids together to build the api url that has views count
                    if (i != itemsArray.length() - 1) {
                        allIds = "$allIds$videoId,"
                    } else {//if it is the last url, then don't put "," at the end
                        allIds = allIds + videoId
                    }
                }

                //build the api link that contain view count information for all videos
                val videoInfoApiLink = "https://www.googleapis.com/youtube/v3/videos?part=contentDetails,statistics&id=$allIds&key=$API_KEY"
                Log.d(TAG, "onResponse: new API link: $videoInfoApiLink")

                getViewCount(videoInfoApiLink)


            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, Response.ErrorListener { error -> error.printStackTrace() })

        mQueue.add(request)

    }


    /**
     * Assign video's views count to the youtubeObj then start the recycler view
     * This function is being called in getAllVideosId();
     * @param url: url of the videoInfoApiLink that contain information of each video
     */
    private fun getViewCount(url: String) {
        val request = JsonObjectRequest(Request.Method.GET, url, null, Response.Listener { response ->
            try {
                val itemsArray = response.getJSONArray("items")

                for (i in 0 until itemsArray.length()) {
                    val itemObject = itemsArray.getJSONObject(i)
                    val statisticsObject = itemObject.getJSONObject("statistics")

                    //assign view count
                    youtubeObjList[i].viewsCount = statisticsObject.getInt("viewCount")

                }

                //sort the arrayList by view
                Collections.sort(youtubeObjList) { o1, o2 -> o2.viewsCount!!.compareTo(o1.viewsCount!!) }

                for (obj in youtubeObjList) {
                    Log.d(TAG, "onResponse: title : " + obj.title + " has view: " + obj.viewsCount)
                }

                //start recycler view
                initRecyclerView()

            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, Response.ErrorListener { error -> error.printStackTrace() })

        mQueue.add(request)
    }

    private fun initRecyclerView() {
        val youtubeRecyclerView = findViewById<RecyclerView>(R.id.youtube_recyclerView)
        val adapter = YoutubeRecyclerViewAdapter(this, youtubeObjList)
        youtubeRecyclerView.adapter = adapter
        youtubeRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    //for back button on action bar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


}
