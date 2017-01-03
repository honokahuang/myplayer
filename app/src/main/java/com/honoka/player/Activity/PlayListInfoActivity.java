package com.honoka.player.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.honoka.player.Base.BaseActivity;
import com.honoka.player.Custom.TitleBar;
import com.honoka.player.R;

/**
 * Created by 41258 on 2016/11/23.
 */

public class PlayListInfoActivity extends BaseActivity {
    public int from=4;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_main);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String tmp = bundle.getString("playlist_id");
        int playlist_id=Integer.valueOf(tmp);
        String playlist_name=bundle.getString("playlist_name");
        setbar(playlist_name,"   ");
        final TitleBar titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.setBackgroundColor(Color.parseColor("#CD2626"));
    }
}
