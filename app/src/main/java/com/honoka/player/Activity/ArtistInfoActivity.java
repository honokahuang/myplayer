package com.honoka.player.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.honoka.player.Adapter.MusicListAdapter;
import com.honoka.player.Base.BaseActivity;
import com.honoka.player.Custom.TitleBar;
import com.honoka.player.Domain.AlbumInfo;
import com.honoka.player.Domain.AppConstant;
import com.honoka.player.Domain.Mp3Info;
import com.honoka.player.R;
import com.honoka.player.Utils.PlayListUnit;

import java.util.List;

/**
 * Created by 41258 on 2016/11/20.
 */

public class ArtistInfoActivity extends BaseActivity {
    private String Artist;
    private int ArtistId;
    private int number_of_tracks;
    private final int from=2;
    private List<Mp3Info> mp3Infos = null;
    MusicListAdapter mMusicAdappter;
    private ListView InfoList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artistlist_info);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Artist=bundle.getString("artist");
        String tmp =bundle.getString("artistId");
        ArtistId=Integer.valueOf(tmp);
        String tmp2=bundle.getString("number_of_tracks");
        number_of_tracks=Integer.valueOf(tmp2);
        setbar(Artist,"");
        final TitleBar titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.setBackgroundColor(Color.parseColor("#CD2626"));
        InfoList= (ListView) findViewById(R.id.artist_info);
        mp3Infos= PlayListUnit.getMp3Infos(ArtistInfoActivity.this,from,ArtistId);
        mMusicAdappter=new MusicListAdapter(this,mp3Infos);
        InfoList.setAdapter(mMusicAdappter);
        TextView artist= (TextView) findViewById(R.id.artist_artist);
        TextView artist_number_of_tracks= (TextView) findViewById(R.id.artist_number_of_tracks);
        artist.setText(Artist);
        artist_number_of_tracks.setText("共"+number_of_tracks+"首");
        InfoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                playmusic(i);
            }
        });
    }

    private void playmusic(int i) {
        Mp3Info mp3Info=mp3Infos.get(i);
        Intent intent = new Intent(ArtistInfoActivity.this,PlayActivity.class);
        intent.putExtra("title", mp3Info.getTitle());
        intent.putExtra("url", mp3Info.getUrl());
        intent.putExtra("artist", mp3Info.getArtist());
        intent.putExtra("listPosition", i);
        intent.putExtra("from", String.valueOf(from));
        intent.putExtra("fromid",String.valueOf(ArtistId));
        intent.putExtra("MSG", AppConstant.PlayerMsg.PLAY_MSG);
        startActivity(intent);
    }
}
