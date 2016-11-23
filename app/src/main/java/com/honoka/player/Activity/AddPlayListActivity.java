package com.honoka.player.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.honoka.player.Base.BaseActivity;
import com.honoka.player.Custom.TitleBar;
import com.honoka.player.R;

/**
 * Created by 41258 on 2016/11/23.
 */

public class AddPlayListActivity extends BaseActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addplaylist);
        setbar("创建播放列表","   ");
        final TitleBar titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.setBackgroundColor(Color.parseColor("#CD2626"));

    }
}
