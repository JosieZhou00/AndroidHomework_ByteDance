package com.bytedance.component.finalproject.net;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetResponse {
    @SerializedName("feeds")
    public List<VideoResult> feeds;
    @SerializedName("success")
    public boolean success;
}
