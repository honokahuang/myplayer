package com.honoka.player.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import com.honoka.player.Adapter.MusicListAdapter;
import com.honoka.player.Base.BaseActivity;
import com.honoka.player.Custom.TitleBar;
import com.honoka.player.Domain.AppConstant;
import com.honoka.player.Domain.Mp3Info;
import com.honoka.player.R;
import com.honoka.player.Service.PlayService;
import com.honoka.player.Utils.PlayListUnit;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

/**
 * Created by 41258 on 2016/11/21.
 */

public class LocalListActivity extends BaseActivity implements SearchView.OnQueryTextListener {
    private ImageView mCollectView;
    private ListView localmusic;//
    private List<Mp3Info> mp3Infos = null;
    private int listPosition = 0; // 标识列表位置
    MusicListAdapter listAdapter; // 改为自定义列表适配器
    private final int from =3;//
    private final String title = "本地音乐";
    DisplayImageOptions options;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locallist_main);
        localmusic= (ListView) findViewById(R.id.localmusic_list);
        settitleright();
        setbar(title,"");
        final TitleBar titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.setBackgroundColor(Color.parseColor("#CD2626"));


        mp3Infos = PlayListUnit.getMp3Infos(LocalListActivity.this,from,0); // 获取歌曲对象集合
        listAdapter = new MusicListAdapter(this, mp3Infos);
        localmusic.setAdapter(listAdapter);
        localmusic.setTextFilterEnabled(true);
        localmusic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                playMusic(i);
            }
        });
        SearchView search = (SearchView) findViewById(R.id.search_loacl);
        search.setOnQueryTextListener(this);
        //为该SearchView组件设置事件监听器
        search.setSubmitButtonEnabled(false);
        search.setQueryHint("查找音乐");




    }
    public void playMusic(int listPosition) {
        if (mp3Infos != null) {
            Mp3Info mp3Info = mp3Infos.get(listPosition);
            Intent intent = new Intent(LocalListActivity.this, PlayActivity.class); // 定义Intent对象，跳转到PlayActivity
            // 添加一系列要传递的数据
            intent.putExtra("title", mp3Info.getTitle());
            intent.putExtra("url", mp3Info.getUrl());
            intent.putExtra("artist", mp3Info.getArtist());
            intent.putExtra("listPosition", listPosition);
            intent.putExtra("from",String.valueOf(from));
            intent.putExtra("fromid",String.valueOf(from));
            intent.putExtra("MSG", AppConstant.PlayerMsg.PLAY_MSG);
            startActivity(intent);
        }
    }
    private void settitleright(){
        final TitleBar titleBar = (TitleBar) findViewById(R.id.title_bar);
        mCollectView = (ImageView) titleBar.addAction(new TitleBar.ImageAction(R.mipmap.musicnote) {
            @Override
            public void performAction(View view) {
                Intent intent = new Intent(LocalListActivity.this,PlayService.class);
                intent.putExtra("MSG",AppConstant.PlayerMsg.GET_INFO);
                startService(intent);
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)){
            //localmusic.clearTextFilter();
        }else {
            localmusic.setFilterText(newText);
        }
        return false;
    }
}
