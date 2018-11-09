package com.example.seamon.sikkacodingchallenge;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class InstagramRecyclerViewAdaper extends RecyclerView.Adapter<InstagramRecyclerViewAdaper.ViewHolder> {
    private ArrayList<InstagramObject> instagramObjList = new ArrayList<>();
    private Context mContext;

    public InstagramRecyclerViewAdaper(Context mContext, ArrayList<InstagramObject> instagramObjList) {
        this.instagramObjList = instagramObjList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_instagram_grid, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        //set up
        String imgUrl = instagramObjList.get(i).getImgUrl();
        if(!(imgUrl==null) && !imgUrl.isEmpty()) {
            Glide.with(mContext).load(Uri.parse(imgUrl)).into(viewHolder.instagramImage);
        }

        String likesCount = "Likes : " + instagramObjList.get(i).getLikeCount().toString();
        viewHolder.likeCountText.setText(likesCount);

        //on click
        viewHolder.instagramListParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent to instagram
                String link = instagramObjList.get(i).getLinkUrl();
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                appIntent.setPackage("com.instagram.android");
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(link));
                try {
                    mContext.startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    mContext.startActivity(webIntent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return instagramObjList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView instagramImage;
        TextView likeCountText;
        ConstraintLayout instagramListParentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            instagramImage = itemView.findViewById(R.id.layout_instagram_image);
            likeCountText = itemView.findViewById(R.id.layout_instagram_like_text);
            instagramListParentLayout = itemView.findViewById(R.id.layout_instagram_constraintLayout);
        }
    }
}
