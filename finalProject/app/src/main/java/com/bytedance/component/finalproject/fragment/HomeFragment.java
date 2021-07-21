package com.bytedance.component.finalproject.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bytedance.component.finalproject.net.GetResponse;
import com.bytedance.component.finalproject.net.VideoResult;
import com.bytedance.component.finalproject.utils.Constants;
import com.bytedance.component.finalproject.utils.IApi;
import com.bytedance.component.finalproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private RecyclerView mRecyclerview;
    private VideoAdapter mVideoAdapter;
    private FloatingActionButton mScrollTopButton;
    private View mLoadingLayout;
    private boolean fetchStatus;
    IApi api;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerview = getActivity().findViewById(R.id.recycler_view);

        mLoadingLayout = getActivity().findViewById(R.id.layout_loading);

        initNetwork();
        mVideoAdapter = new VideoAdapter(new ArrayList<>());
        fetchVideoList();

        mScrollTopButton = getActivity().findViewById(R.id.btn_scroll_top);
        mScrollTopButton.setVisibility(View.GONE);
        mScrollTopButton.setOnClickListener((view) -> {
            mScrollTopButton.setVisibility(View.GONE);
            mRecyclerview.smoothScrollToPosition(0);
            Handler handler = new Handler();
            handler.postDelayed(() -> mRecyclerview.scrollToPosition(0), 500);
        });

        mRecyclerview.setAdapter(mVideoAdapter);
        mRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mScrollTopButton.setVisibility(View.GONE);
                    if (!mRecyclerview.canScrollVertically(-1)) {
                        if (!fetchStatus) fetchVideoList();
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < -200) {
                    mScrollTopButton.setVisibility(View.VISIBLE);
                }
            }
        });


    }
    private void initNetwork() {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(IApi.class);
    }

    private void fetchVideoList() {
        fetchStatus = true;
        Call<GetResponse> resp = api.getVideo();
        resp.enqueue(new Callback<GetResponse>() {
            @Override
            public void onResponse(final Call<GetResponse> call, final Response<GetResponse> response) {
                fetchStatus = false;
                getActivity().runOnUiThread(() -> mLoadingLayout.setVisibility(View.GONE));
                if (!response.isSuccessful()) return;
                List<VideoResult> results = response.body().feeds;
                List<VideoItem> videos = results.stream().map((VideoResult result) -> {
                    VideoItem video = new VideoItem(Uri.parse(result.videoUrl), Uri.parse(result.imageUrl),
                            result.imageW, result.imageH,
                            result.userName, result.createdAt);
                    return video;
                }).collect(Collectors.toList());
                mVideoAdapter.notifyItems(videos);
            }


            @Override
            public void onFailure(final Call<GetResponse> call, final Throwable t) {
                fetchStatus = false;
                t.printStackTrace();
                Toast.makeText(getActivity(), R.string.loading_fail, Toast.LENGTH_SHORT).show();
                Log.d(TAG, t.toString());
            }
        });
    }

}