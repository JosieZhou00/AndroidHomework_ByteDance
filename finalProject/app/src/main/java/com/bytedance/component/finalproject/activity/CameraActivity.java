package com.bytedance.component.finalproject.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.view.CameraController;
import androidx.camera.view.LifecycleCameraController;
import androidx.camera.view.PreviewView;
import androidx.camera.view.video.OnVideoSavedCallback;
import androidx.camera.view.video.OutputFileOptions;
import androidx.camera.view.video.OutputFileResults;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bytedance.component.finalproject.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.concurrent.Executors;

public class CameraActivity extends AppCompatActivity {
    final static String TAG = "CameraActivity";
    final static int UPLOAD_VIDEO_REQUEST_CODE = 1001;
    final static long TOTAL_TIME = 10000;
    private long startTime;
    private PreviewView mPreviewView;
    private MaterialButton mRecordButton;
    private LinearProgressIndicator mProgress;
    private CameraController mController;
    private OutputFileOptions mOptions;
    private Handler mHandler;
    private Runnable mRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        Log.d(TAG,"on create");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        ImageButton closeButton = findViewById(R.id.btn_close);
        closeButton.setOnClickListener(view -> finish());

        mPreviewView = findViewById(R.id.preview_view);
        mRecordButton = findViewById(R.id.btn_record);
        mProgress = findViewById(R.id.progress);
        mHandler = new Handler();
        setupController();
        setupOutput();
        setupHandler();
        setButtonStartRecord();
    }

    private void setupHandler() {
        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                long duration = System.currentTimeMillis() - startTime;
                if (duration >= TOTAL_TIME) {
                    runOnUiThread(() -> mProgress.setProgress(100));
                    stopRecord();
                    return;
                }
                runOnUiThread(() -> mProgress.setProgress((int) (duration * 100 / TOTAL_TIME), true));
                mHandler.postDelayed(this, 100);
            }
        };
    }


    private void setButtonStartRecord() {
        mRecordButton.setOnClickListener(view -> startRecord());
        mRecordButton.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_video_camera_));
    }

    private void setButtonStopRecord() {
        mRecordButton.setOnClickListener(view -> stopRecord());
        mRecordButton.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_stop));
    }

    @SuppressLint("UnsafeOptInUsageError")
    private void setupController() {
        mController = new LifecycleCameraController(this);
        ((LifecycleCameraController) mController).bindToLifecycle(this);
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();
        mController.setCameraSelector(cameraSelector);
        mPreviewView.setController(mController);
        mController.setEnabledUseCases(CameraController.VIDEO_CAPTURE);
    }

    @SuppressLint("UnsafeOptInUsageError")
    private void setupOutput() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "video_" + System.currentTimeMillis());
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4");

        mOptions = OutputFileOptions.builder(
                getContentResolver(),
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                contentValues).build();
    }

    @SuppressLint("UnsafeOptInUsageError")
    private void startRecord() {

        mController.startRecording(mOptions,
                Executors.newSingleThreadExecutor(),
                new OnVideoSavedCallback() {
                    @Override
                    public void onVideoSaved(@NonNull OutputFileResults outputFileResults) {
                        Log.d(TAG, "onSaved");
                        Intent intent = new Intent(CameraActivity.this, UploadPreviewActivity.class);
                        intent.putExtra("videoUri", outputFileResults.getSavedUri().toString());
                        startActivityForResult(intent, UPLOAD_VIDEO_REQUEST_CODE);
                        runOnUiThread(() -> getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE));
                    }

                    @Override
                    public void onError(int videoCaptureError, @NonNull String message, @Nullable Throwable cause) {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(CameraActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }
        );
        setButtonStopRecord();
        startTime = System.currentTimeMillis();
        mHandler.post(mRunnable);
    }

    @SuppressLint("UnsafeOptInUsageError")
    private void stopRecord() {
        long duration = System.currentTimeMillis() - startTime;
        if (duration < 1000) {
            Toast.makeText(this, R.string.at_least_1s, Toast.LENGTH_SHORT).show();
            return;
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        mHandler.removeCallbacks(mRunnable);
        runOnUiThread(() -> mProgress.setProgress(0));
        setButtonStartRecord();
        mController.stopRecording();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == UPLOAD_VIDEO_REQUEST_CODE) {
                finish();
            }
        }
    }
}