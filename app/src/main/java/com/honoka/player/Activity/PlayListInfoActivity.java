package com.honoka.player.Activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.honoka.player.Adapter.MusicListAdapter;
import com.honoka.player.Base.BaseActivity;
import com.honoka.player.Custom.TitleBar;
import com.honoka.player.Domain.AppConstant;
import com.honoka.player.Domain.Mp3Info;
import com.honoka.player.R;
import com.honoka.player.Utils.PlayListUnit;

import java.util.List;

/**
 * Created by 41258 on 2016/11/23.
 */

public class PlayListInfoActivity extends BaseActivity {
    private List<Mp3Info> mp3Infos = null;
    MusicListAdapter mMusicAdappter;
    private ListView playlist;
    public int from=4;
    private int playlist_id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_main);
        initview();
    }
    private void initview() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String tmp = bundle.getString("playlist_id");
        final int playlist_id=Integer.valueOf(tmp);
        this.playlist_id=playlist_id;
        String playlist_name=bundle.getString("playlist_name");
        setbar(playlist_name,"   ");
        final TitleBar titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.setBackgroundColor(Color.parseColor("#CD2626"));
        playlist= (ListView) findViewById(R.id.playlist_main);
        mp3Infos= PlayListUnit.getMp3Infos(PlayListInfoActivity.this,from,playlist_id);
        mMusicAdappter=new MusicListAdapter(this,mp3Infos);
        playlist.setEmptyView(findViewById(R.id.emptylist));
        playlist.setAdapter(mMusicAdappter);
        playlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                playmusic(position);
            }
        });
        playlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder=new AlertDialog.Builder(PlayListInfoActivity.this);
                builder.setTitle("提示");
                builder.setMessage("确定删除？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        remove_music_from_Playlist(PlayListInfoActivity.this,playlist_id,position);
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                return true;
            }
        });
    }
    private void playmusic(int i) {
        Mp3Info mp3Info=mp3Infos.get(i);
        Intent intent = new Intent(PlayListInfoActivity.this, PlayActivity.class); // 定义Intent对象，跳转到PlayActivity
        // 添加一系列要传递的数据
        intent.putExtra("title", mp3Info.getTitle());
        intent.putExtra("url", mp3Info.getUrl());
        intent.putExtra("artist", mp3Info.getArtist());
        intent.putExtra("listPosition", i);
        intent.putExtra("from", String.valueOf(from));
        intent.putExtra("fromid",String.valueOf(playlist_id));
        intent.putExtra("MSG", AppConstant.PlayerMsg.PLAY_MSG);
        startActivity(intent);

    }
    public void remove_music_from_Playlist(Context context, long playlist_id,int i){
        Mp3Info mp3Info=mp3Infos.get(i);
        ContentResolver resolver = context.getContentResolver();
        Uri newuri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlist_id);
        resolver.delete(newuri,MediaStore.Audio.Playlists.Members.AUDIO_ID + "='" + mp3Info.getId() + "'",null);
    }
}
