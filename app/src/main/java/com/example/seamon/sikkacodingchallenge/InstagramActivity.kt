package com.example.seamon.sikkacodingchallenge

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.widget.GridLayoutManager
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

class InstagramActivity : AppCompatActivity() {

    private var mQueue: RequestQueue? = null
    private val instagramObjList = ArrayList<InstagramObject>()
    private val linkUrlList = ArrayList<String>() //for checking duplicates

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instagram)

        mQueue = Volley.newRequestQueue(this)

        getInstagramImages()

        //enable back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    /**
     * Deals with Instagram API call by getting image url, link url, and like count of each photo.
     * Then, updates the recycler view with images from ‘sikkalife’, ‘practicemobilizer’ &
     * ‘selfdrivingpractice’ and ‘sikkasoftware’ tags.
     */
    private fun getInstagramImages() {
        //init recycler view
        val instagramRecyclerView = findViewById<RecyclerView>(R.id.instagram_recyclerView)
        val adapter = InstagramRecyclerViewAdaper(this, instagramObjList)
        instagramRecyclerView.adapter = adapter
        instagramRecyclerView.layoutManager = GridLayoutManager(this, 3)

        val apiUrlList = ArrayList<String>()
        apiUrlList.add("https://api.instagram.com/v1/tags/sikkasoftware/media/recent?access_token=8802462623.ba4c844.0faf4614002847648b4e724a3e4ffe94")
        apiUrlList.add("https://api.instagram.com/v1/tags/sikkalife/media/recent?access_token=8802462623.ba4c844.0faf4614002847648b4e724a3e4ffe94")
        apiUrlList.add("https://api.instagram.com/v1/tags/practicemobilizer/media/recent?access_token=8802462623.ba4c844.0faf4614002847648b4e724a3e4ffe94")
        apiUrlList.add("https://api.instagram.com/v1/tags/selfdrivingpractice/media/recent?access_token=8802462623.ba4c844.0faf4614002847648b4e724a3e4ffe94")

        for (url in apiUrlList) {
            val request = JsonObjectRequest(Request.Method.GET, url, null, Response.Listener { response ->
                try {

                    val dataArray = response.getJSONArray("data")

                    for (i in 0 until dataArray.length()) {
                        val dataObject = dataArray.getJSONObject(i)
                        val imagesObject = dataObject.getJSONObject("images")
                        val currentImgUrl = imagesObject.getJSONObject("thumbnail").getString("url")
                        val currentLinkUrl = dataObject.getString("link")
                        val currentLikeCount = dataObject.getJSONObject("likes").getInt("count")

                        //check if the link exists or not. If the link is already existed, then skip
                        if (!linkUrlList.contains(currentLinkUrl)) {
                            linkUrlList.add(currentLinkUrl)

                            //assign value to the instagram obj
                            val currentInstagramObject = InstagramObject(currentImgUrl, currentLinkUrl, currentLikeCount as Integer)
                            //currentInstagramObject.setImgUrl(currentImgUrl);
                            //currentInstagramObject.setLinkUrl(currentLinkUrl);
                            //currentInstagramObject.setLikeCount(currentLikeCount);


                            instagramObjList.add(currentInstagramObject)
                        }

                    }

                    //sort the arrayList by likes
                    Collections.sort(instagramObjList) { o1, o2 -> o2.likeCount.compareTo(o1.likeCount as Int) }

                    for (obj in instagramObjList) {
                        Log.d(TAG, "onResponse: currentLikeCount: " + obj.likeCount + ", currentLinkUrl: " + obj.linkUrl)
                    }

                    //update the recycler view
                    adapter.notifyDataSetChanged()

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener { error -> error.printStackTrace() })

            mQueue!!.add(request)
        }
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

    companion object {
        private val TAG = "InstagramActivity"
    }
}
