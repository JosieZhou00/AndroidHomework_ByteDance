package com.bytedance.component.finalproject.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;

import com.bytedance.component.finalproject.R;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

public class VideoActivity extends AppCompatActivity {

    private final static String TAG = "VideoActivity";
    ImageButton mPrevButton;
    SimpleExoPlayer mPlayer;
    Uri mVideoUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        mPrevButton = findViewById(R.id.btn_prev);
        mPrevButton.setOnClickListener(view -> finish());

        PlayerView playerView = findViewById(R.id.video_view);
        playerView.setControllerAutoShow(false);
        mPlayer = new SimpleExoPlayer.Builder(this).build();
        playerView.setPlayer(mPlayer);


        if (getIntent().getStringExtra("videoUri") != null) {
            mVideoUri = Uri.parse(getIntent().getStringExtra("videoUri"));
            playVideo(mVideoUri);
        }


    }

    private void playVideo(Uri uri) {
        MediaItem mediaItem = MediaItem.fromUri(uri);
        mPlayer.setMediaItem(mediaItem);
        mPlayer.prepare();
        mPlayer.play();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayer.release();
    }
}