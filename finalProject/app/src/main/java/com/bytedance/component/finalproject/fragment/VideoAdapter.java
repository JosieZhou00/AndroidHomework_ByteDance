package com.bytedance.component.finalproject.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bytedance.component.finalproject.R;
import com.bytedance.component.finalproject.activity.VideoActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


class VideoItem {
    Uri videoUri, coverUri;
    String author;
    Date created;
    int imageW, imageH;

    VideoItem(Uri videoUri, Uri coverUri, int imageW, int imageH, String author, Date created) {
        this.coverUri = coverUri;
        this.videoUri = videoUri;
        this.author = author;
        this.created = created;
        this.imageW = imageW;
        this.imageH = imageH;
    }
}

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    @NonNull
    private List<VideoItem> mVideoList = new ArrayList<>();

    public void notifyItems(@NonNull List<VideoItem> videos) {
        mVideoList.clear();
        mVideoList.addAll(videos);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mAuthorText, mDateText;
        public final ImageView mImageView;

        public ViewHolder(View view) {
            super(view);
            mAuthorText = view.findViewById(R.id.text_author);
            mDateText = view.findViewById(R.id.upload_date);
            mImageView = view.findViewById(R.id.video_cover);
        }

    }

    public VideoAdapter(List<VideoItem> videos) {
        mVideoList = videos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.video_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        VideoItem video = mVideoList.get(position);
        viewHolder.mAuthorText.setText(video.author);
        viewHolder.mDateText.setText(new SimpleDateFormat("MM-dd").format(video.created));
        int height = (int) ((float) viewHolder.mImageView.getWidth() / video.imageW * video.imageH);
        viewHolder.mImageView.setMinimumHeight(height);


        viewHolder.itemView.setOnClickListener((itemView) -> {
            Intent intent = new Intent(itemView.getContext(), VideoActivity.class);
            intent.putExtra("videoUri", video.videoUri.toString());
            itemView.getContext().startActivity(intent);
        });

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(viewHolder.itemView.getContext());
        circularProgressDrawable.setStrokeWidth(7f);
        circularProgressDrawable.setCenterRadius(40f);
        circularProgressDrawable.start();

        Glide.with(viewHolder.itemView.getContext())
                .load(video.coverUri)
                .placeholder(circularProgressDrawable)
                .into(viewHolder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mVideoList.size();
    }
}
