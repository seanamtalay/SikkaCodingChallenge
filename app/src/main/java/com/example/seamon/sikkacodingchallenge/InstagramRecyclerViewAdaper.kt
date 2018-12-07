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

import java.util.ArrayList

class InstagramRecyclerViewAdaper(private val mContext: Context, private val instagramObjList: ArrayList<InstagramObject>) : RecyclerView.Adapter<InstagramRecyclerViewAdaper.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.layout_instagram_grid, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        //set up
        val imgUrl: String? = instagramObjList[i].imgUrl
        if (imgUrl != null && !imgUrl.isEmpty()) {
            Glide.with(mContext).load(Uri.parse(imgUrl)).into(viewHolder.instagramImage)
        }

        val likesCount = "Likes : " + instagramObjList[i].likeCount.toString()
        viewHolder.likeCountText.text = likesCount

        //on click
        viewHolder.instagramListParentLayout.setOnClickListener {
            //intent to instagram
            val link = instagramObjList[i].linkUrl
            val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            appIntent.setPackage("com.instagram.android")
            val webIntent = Intent(Intent.ACTION_VIEW,
                    Uri.parse(link))
            try {
                mContext.startActivity(appIntent)
            } catch (ex: ActivityNotFoundException) {
                mContext.startActivity(webIntent)
            }
        }
    }

    override fun getItemCount(): Int {
        return instagramObjList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val instagramImage = itemView.findViewById(R.id.layout_instagram_image) as ImageView
        val likeCountText = itemView.findViewById(R.id.layout_instagram_like_text) as TextView
        val instagramListParentLayout = itemView.findViewById(R.id.layout_instagram_constraintLayout) as ConstraintLayout


    }
}
