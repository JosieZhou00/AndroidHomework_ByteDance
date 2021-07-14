package com.example.chapter3.homework.recyclerView;

import java.util.ArrayList;
import java.util.List;

public class FriendsDataSet {
    public static List<FriendsData> getData(int ID){
        //int resId = mContext.getResources().getIdentifier("v_icon", "drawable", mContext.getPackageName());
        List<FriendsData> res = new ArrayList();
        res.add(new FriendsData("游戏小助手", "新游戏上线了！大家都在等你来！",ID));
        res.add(new FriendsData("抖音小助手", "您的视频刚刚通过审核了！快跟...",ID));
        res.add(new FriendsData("系统消息", "您的账号有异常登录情况！",ID));
        res.add(new FriendsData("肉肉丸", "感谢您的关注！",ID));
        res.add(new FriendsData("咕咕", "感谢您的关注！",ID));
        res.add(new FriendsData("法式蝴蝶薯条", "这个视频超好看的",ID));
        res.add(new FriendsData("林木", "快去关注!",ID));
        res.add(new FriendsData("大家都说名字越长越好但是我不这么认...", "[Hi]",ID));
        res.add(new FriendsData("Customer", "您好",ID));
        res.add(new FriendsData("来自附近的人", "让我康康",ID));
        res.add(new FriendsData("233", "666",ID));
        return res;
    }
}