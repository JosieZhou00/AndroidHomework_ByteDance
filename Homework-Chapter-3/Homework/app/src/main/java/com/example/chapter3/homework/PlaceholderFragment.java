package com.example.chapter3.homework;


import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.chapter3.homework.recyclerView.FriendListAdapter;
import com.example.chapter3.homework.recyclerView.FriendsDataSet;

public class PlaceholderFragment extends Fragment {

    private static String TAG = "PlaceholderFragment";
    private LottieAnimationView animation;
    private RecyclerView friendsList;
    FriendListAdapter adapter;
    LinearLayoutManager linear;
    private boolean isVisible = false;
    private boolean isFirst = false;
    private View rootView = null;
    ObjectAnimator animatorLoad = null;
    ObjectAnimator animatorList = null;
    private int T = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG,"Calling onCreateView");
        View view = inflater.inflate(R.layout.fragment_placeholder, container, false);
        friendsList = view.findViewById(R.id.friendList);
        animation = view.findViewById(R.id.animation);

        int ID = getResources().getIdentifier("circle","mipmap","com.example.chapter3.homework");
        Log.d(TAG,"Found ID : " + Integer.toString(ID));
        adapter = new FriendListAdapter(FriendsDataSet.getData(ID));
        linear = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        friendsList.setHasFixedSize(true);
        friendsList.setLayoutManager(linear);
        friendsList.setAdapter(adapter);

        animatorLoad = ObjectAnimator.ofFloat(animation,"alpha",1f,0f);
        animatorList = ObjectAnimator.ofFloat(friendsList,"alpha",0f,1f);
        animatorLoad.setDuration(500);
        animatorList.setDuration(500);

        rootView = view;

        T = 0;

        if(isVisible && !isFirst){
            setAnimationBegin();
            isFirst = true;
        }
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG,"Call setUserVisibleHint " + Boolean.toString(isVisibleToUser));
        if (isVisibleToUser) {
            isVisible = true;
        }
        if (rootView == null) {
            return;
        }

        if (!isFirst&&isVisible) {
            setAnimationBegin();
            isFirst = true;
            return;
        }
        if (isVisible) {
            Log.d(TAG,"reset Animation");
            isVisible = false;
            isFirst = false;
            resetAnimation();
        }
    }

    public void resetAnimation(){
        ++T;
        friendsList.setVisibility(View.INVISIBLE);
        friendsList.setAlpha(1f);
        animation.pauseAnimation();
        animation.setVisibility(View.VISIBLE);
        animation.setAlpha(1f);
        animation.setProgress(0);
    }

    public void setAnimationBegin(){
        // TODO ex3-3: 修改 fragment_placeholder，添加 loading 控件和列表视图控件
        final int nowT = ++T;
        Log.d(TAG,"Begin animation");
        animation.playAnimation();
        if(rootView != null && isVisible) rootView.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 这里会在 5s 后执行
                // TODO ex3-4：实现动画，将 lottie 控件淡出，列表数据淡入
                if(T != nowT) return;
                friendsList.setAlpha(0f);
                friendsList.setVisibility(View.VISIBLE);

                animatorLoad.start();
                animatorList.start();
                if(T != nowT) return;

                if(rootView != null && isVisible) rootView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(T != nowT) return;
                        animation.pauseAnimation();
                        animation.setVisibility(View.INVISIBLE);
                    }
                },500);
            }
        }, 5000);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}