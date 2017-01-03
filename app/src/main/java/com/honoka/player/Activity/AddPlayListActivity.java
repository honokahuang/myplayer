package com.honoka.player.Activity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.honoka.player.Base.BaseActivity;
import com.honoka.player.Custom.TitleBar;
import com.honoka.player.R;

/**
 * Created by 41258 on 2016/11/23.
 */

public class AddPlayListActivity extends BaseActivity{
    public String playlist_name;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addplaylist);
        setbar("创建播放列表","   ");
        final TitleBar titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.setBackgroundColor(Color.parseColor("#CD2626"));

        Button add= (Button) findViewById(R.id.addplaylist);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText playlist= (EditText) findViewById(R.id.playlist_name);
                addnewPlaylist(AddPlayListActivity.this,playlist.getText().toString());
                finish();
            }
        });
    }
    public void addnewPlaylist(Context context, String newplaylist) {
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues(1);
        values.put(MediaStore.Audio.Playlists.NAME, newplaylist);
        resolver.insert(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, values);
    }
}
