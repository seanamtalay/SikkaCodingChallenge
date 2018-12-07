package com.example.seamon.sikkacodingchallenge

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide

import java.text.NumberFormat
import java.util.ArrayList
import java.util.Locale

class YoutubeRecyclerViewAdapter(private val mContext: Context, private val youtubeObjList: ArrayList<YoutubeObject>) : RecyclerView.Adapter<YoutubeRecyclerViewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.layout_youtube_list, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        //set everything up
        viewHolder.titleText.text = youtubeObjList[i].title
        viewHolder.channelText.text = youtubeObjList[i].channel
        viewHolder.descriptionText.text = youtubeObjList[i].description
        val shownViewText = NumberFormat.getNumberInstance(Locale.US).format(youtubeObjList[i].viewsCount) + " views"
        viewHolder.viewsText.text = shownViewText

        val thumbnailUrl: String? = youtubeObjList[i].thumbnailUrl
        if (thumbnailUrl != null && !thumbnailUrl.isEmpty()) {
            //GlideApp.with(mContext).load(Uri.parse(thumbnailUrl)).into(viewHolder.thumbnailImage);
            Glide.with(mContext).load(Uri.parse(thumbnailUrl)).into(viewHolder.thumbnailImage)

        }

        //onclick
        viewHolder.youtubeListParentLayout.setOnClickListener {
            //intent to Youtube
            val videoId = youtubeObjList[i].id
            val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$videoId"))
            val webIntent = Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=$videoId"))
            try {
                mContext.startActivity(appIntent)
            } catch (ex: ActivityNotFoundException) {
                mContext.startActivity(webIntent)
            }
        }
    }

    override fun getItemCount(): Int {
        return youtubeObjList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val thumbnailImage = itemView.findViewById(R.id.layout_youtube_thumbnail) as ImageView
        val titleText = itemView.findViewById(R.id.layout_youtube_title_text) as TextView
        val descriptionText = itemView.findViewById(R.id.layout_youtube_des_text) as TextView
        val viewsText = itemView.findViewById(R.id.layout_youtube_view_text) as TextView
        val channelText = itemView.findViewById(R.id.layout_youtube_channel_text) as TextView
        val youtubeListParentLayout = itemView.findViewById(R.id.layout_youtube_contraintLayout) as ConstraintLayout

    }


}
