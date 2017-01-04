package com.honoka.player.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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
 * Created by 41258 on 2016/11/18.
 */

public class AlbumInfoActivity extends BaseActivity {
    private int number;
    private ListView InfoList;// 音乐列表
    private int albumid;
    private String albumtitle;
    private String artist;
    private int albumsong;
    private String title;
    private List<Mp3Info> mp3Infos = null;
    private final int from=1;
    MusicListAdapter mMusicAdappter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albumlist_info);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String tmp = bundle.getString("albumid");
        albumid=Integer.valueOf(tmp);
        albumsong=Integer.valueOf(bundle.getString("number_of_song"));
        artist=bundle.getString("artisrt");
        albumtitle=bundle.getString("albunmtitle");
        setbar(albumtitle,artist);
        final TitleBar titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.setBackgroundColor(Color.parseColor("#CD2626"));
        InfoList = (ListView) findViewById(R.id.album_song);

        mp3Infos= PlayListUnit.getMp3Infos(AlbumInfoActivity.this,from,albumid);
        mMusicAdappter=new MusicListAdapter(this,mp3Infos);
        InfoList.setAdapter(mMusicAdappter);

        Bitmap bm = PlayListUnit.getArtwork(this, 2, albumid, true, false);
        ImageView albumimage= (ImageView) findViewById(R.id.albuminfo_image);
        final TextView title= (TextView) findViewById(R.id.albuminfo_title);
        TextView albumartist= (TextView) findViewById(R.id.albuminfo_artist);
        TextView number_of_song= (TextView) findViewById(R.id.number_of_song_info);
        if (bm != null){
            albumimage.setImageBitmap(bm);
        }
        albumartist.setText(artist);
        title.setText(albumtitle);
        number_of_song.setText("共"+albumsong+"首");
        albumartist.setText(artist);
        InfoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                playmusic(i);
            }
        });
    }

    private void playmusic(int i) {
            Mp3Info mp3Info=mp3Infos.get(i);
            Intent intent = new Intent(AlbumInfoActivity.this, PlayActivity.class); // 定义Intent对象，跳转到PlayActivity
            // 添加一系列要传递的数据
            intent.putExtra("title", mp3Info.getTitle());
            intent.putExtra("url", mp3Info.getUrl());
            intent.putExtra("artist", mp3Info.getArtist());
            intent.putExtra("listPosition", i);
            intent.putExtra("from", String.valueOf(from));
            intent.putExtra("fromid",String.valueOf(albumid));
            intent.putExtra("MSG", AppConstant.PlayerMsg.PLAY_MSG);
            startActivity(intent);

    }

}
