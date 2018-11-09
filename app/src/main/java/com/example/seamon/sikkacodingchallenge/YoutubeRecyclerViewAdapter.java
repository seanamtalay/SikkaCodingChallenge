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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class YoutubeRecyclerViewAdapter extends RecyclerView.Adapter<YoutubeRecyclerViewAdapter.ViewHolder>{

    private ArrayList<YoutubeObject> youtubeObjList = new ArrayList<>();
    private Context mContext;


    public YoutubeRecyclerViewAdapter(Context mContext, ArrayList<YoutubeObject> youtubeObjList) {
        this.youtubeObjList = youtubeObjList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_youtube_list, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        //set everything up
        viewHolder.titleText.setText(youtubeObjList.get(i).getTitle());
        viewHolder.channelText.setText(youtubeObjList.get(i).getChannel());
        viewHolder.descriptionText.setText(youtubeObjList.get(i).getDescription());
        String shownViewText = NumberFormat.getNumberInstance(Locale.US).format(youtubeObjList.get(i).getViewsCount()) + " views";
        viewHolder.viewsText.setText(shownViewText);

        String thumbnailUrl = youtubeObjList.get(i).getThumbnailUrl();
        if(!(thumbnailUrl==null) && !thumbnailUrl.isEmpty()) {
            //GlideApp.with(mContext).load(Uri.parse(thumbnailUrl)).into(viewHolder.thumbnailImage);
            Glide.with(mContext).load(Uri.parse(thumbnailUrl)).into(viewHolder.thumbnailImage);

        }

        //onclick
        viewHolder.youtubeListParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent to Youtube
                String videoId = youtubeObjList.get(i).getId();
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + videoId));
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
        return youtubeObjList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnailImage;
        TextView titleText, descriptionText, viewsText, channelText;
        ConstraintLayout youtubeListParentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailImage = itemView.findViewById(R.id.layout_youtube_thumbnail);
            titleText = itemView.findViewById(R.id.layout_youtube_title_text);
            descriptionText = itemView.findViewById(R.id.layout_youtube_des_text);
            viewsText = itemView.findViewById(R.id.layout_youtube_view_text);
            channelText = itemView.findViewById(R.id.layout_youtube_channel_text);
            youtubeListParentLayout = itemView.findViewById(R.id.layout_youtube_contraintLayout);
        }
    }


}
