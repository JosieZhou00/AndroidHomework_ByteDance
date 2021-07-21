package com.bytedance.component.finalproject.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bytedance.component.finalproject.R;
import com.bytedance.component.finalproject.fragment.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private final static int RECORD_PERMISSION_REQUEST_CODE = 1001;
    private final static int REQUEST_TAKE_GALLERY_VIDEO = 1002;
    private final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_green);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        BottomNavigationItemView navRecord = findViewById(R.id.nav_record);
        navRecord.setOnClickListener(this::requestRecordPermission);

        BottomNavigationItemView navUpload = findViewById(R.id.nav_upload);
        navUpload.setOnClickListener((view) -> videoSelect());

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
        navigation.setOnNavigationItemReselectedListener((view) -> {
        });
        navigation.setSelectedItemId(R.id.nav_home);
        loadFragment(new HomeFragment());
    }

    public void requestRecordPermission(View view) {
        boolean hasCameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean hasAudioPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
        if (hasCameraPermission && hasAudioPermission) recordVideo();
        else {
            List<String> permission = new ArrayList<String>();
            permission.add(Manifest.permission.CAMERA);
            permission.add(Manifest.permission.RECORD_AUDIO);
            ActivityCompat.requestPermissions(this, permission.toArray(new String[permission.size()]), RECORD_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasPermission = true;
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                hasPermission = false;
                break;
            }
        }

        if (!hasPermission) {
            Toast.makeText(this, "权限获取失败", Toast.LENGTH_SHORT).show();
            return;
        }

        if (requestCode == RECORD_PERMISSION_REQUEST_CODE) {
            recordVideo();
        }
    }

    private void recordVideo() {
        Intent intent = new Intent(MainActivity.this, CameraActivity.class);
        startActivity(intent);
    }

    @SuppressLint("NonConstantResourceId")
    private boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.nav_home:
                fragment = new HomeFragment();
                break;

        }
        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private void videoSelect() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        startActivityForResult(Intent.createChooser(intent, "Select Video"), REQUEST_TAKE_GALLERY_VIDEO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
                Uri uri = data.getData();
                Intent intent = new Intent(MainActivity.this, UploadPreviewActivity.class);
                intent.putExtra("videoUri", uri.toString());
                startActivity(intent);
            }
        }
    }
}