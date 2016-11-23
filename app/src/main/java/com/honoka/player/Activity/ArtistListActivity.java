package com.honoka.player.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.honoka.player.Adapter.ArtistListAdapter;
import com.honoka.player.Base.BaseActivity;
import com.honoka.player.Custom.TitleBar;
import com.honoka.player.Domain.AppConstant;
import com.honoka.player.Domain.ArtistInfo;
import com.honoka.player.R;
import com.honoka.player.Domain.Mp3Info;
import com.honoka.player.Utils.ArtistListUnit;

import java.util.List;

/**
 * Created by Honoka on 2016/11/2.
 */

public class ArtistListActivity extends BaseActivity {
    private ImageView mCollectView;
    private final String title="艺术家";
    private ListView mArtistList; // 音乐列表
    private List<ArtistInfo> artistInfos = null;
    ArtistListAdapter listAdapter; // 改为自定义列表适配器

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artistlist_main);
        setbar(title,"");
        final TitleBar titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.setBackgroundColor(Color.parseColor("#CD2626"));
        mArtistList = (ListView) findViewById(R.id.music_list_artistlist);
        artistInfos = ArtistListUnit.getArtistInfo(ArtistListActivity.this);
        listAdapter = new ArtistListAdapter(this, artistInfos);
        mArtistList.setAdapter(listAdapter);
        mArtistList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ShowMusic(i);
            }
        });
    }
    public void ShowMusic(int i) {
            ArtistInfo artistInfo = artistInfos.get(i);
            Intent intent = new Intent(ArtistListActivity.this, ArtistInfoActivity.class); // 定义Intent对象，跳转到ArtistInfoActivity
            intent.putExtra("artist", artistInfo.getArtist()); // 添加一系列要传递的数据
            intent.putExtra("artistId",String.valueOf(artistInfo.getArtistId()));
            intent.putExtra("number_of_tracks",String.valueOf(artistInfo.getNumber_of_tracks()));
            startActivity(intent);
    }
}
