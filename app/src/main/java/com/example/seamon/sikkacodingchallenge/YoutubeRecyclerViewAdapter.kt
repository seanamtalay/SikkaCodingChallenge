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

class YoutubeRecyclerViewAdapter(private val mContext: Context, val youtubeObjList: ArrayList<YoutubeObject>) : RecyclerView.Adapter<YoutubeRecyclerViewAdapter.ViewHolder>() {


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

        val thumbnailUrl = youtubeObjList[i].thumbnailUrl
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

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var thumbnailImage: ImageView
        internal var titleText: TextView
        internal var descriptionText: TextView
        internal var viewsText: TextView
        internal var channelText: TextView
        internal var youtubeListParentLayout: ConstraintLayout

        init {
            thumbnailImage = itemView.findViewById(R.id.layout_youtube_thumbnail)
            titleText = itemView.findViewById(R.id.layout_youtube_title_text)
            descriptionText = itemView.findViewById(R.id.layout_youtube_des_text)
            viewsText = itemView.findViewById(R.id.layout_youtube_view_text)
            channelText = itemView.findViewById(R.id.layout_youtube_channel_text)
            youtubeListParentLayout = itemView.findViewById(R.id.layout_youtube_contraintLayout)
        }
    }


}
