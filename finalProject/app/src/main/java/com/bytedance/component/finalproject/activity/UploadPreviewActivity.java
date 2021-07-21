package com.bytedance.component.finalproject.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import com.bytedance.component.finalproject.R;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

public class UploadPreviewActivity extends AppCompatActivity {

    final static int REQUEST_COVER_CHOOSE = 1001;
    private final static String TAG = "UploadPreviewActivity";
    ImageButton mPrevButton, mNextButton;
    SimpleExoPlayer mPlayer;
    Uri mVideoUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_preview);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        mPrevButton = findViewById(R.id.btn_prev);
        mNextButton = findViewById(R.id.btn_next);

        mPrevButton.setOnClickListener(view -> finish());
        mNextButton.setOnClickListener((view) -> nextStep());

        PlayerView playerView = findViewById(R.id.video_view);
        mPlayer = new SimpleExoPlayer.Builder(this).build();
        playerView.setPlayer(mPlayer);

        if (getIntent().getStringExtra("videoUri") != null) {
            mVideoUri = Uri.parse(getIntent().getStringExtra("videoUri"));
            playVideo(mVideoUri);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_COVER_CHOOSE) {
                Log.d(TAG, "UploadFinish");
                setResult(Activity.RESULT_OK);
                finish();
            }
        }
    }

    private void playVideo(Uri uri) {
        MediaItem mediaItem = MediaItem.fromUri(uri);
        mPlayer.setMediaItem(mediaItem);
        mPlayer.prepare();
        mPlayer.play();
    }

    private void nextStep() {
        Intent intent = new Intent(UploadPreviewActivity.this, ChooseCoverActivity.class);
        intent.putExtra("videoUri", mVideoUri.toString());
        startActivityForResult(intent, REQUEST_COVER_CHOOSE);
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayer.release();
    }
}