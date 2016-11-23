package com.honoka.player.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.honoka.player.Adapter.AlbumListAdapter;
import com.honoka.player.Base.BaseActivity;
import com.honoka.player.Custom.TitleBar;
import com.honoka.player.Domain.AlbumInfo;
import com.honoka.player.R;
import com.honoka.player.Utils.AlbumListUnit;

import java.util.List;

/**
 * Created by 41258 on 2016/11/17.
 */

public class AlbumListActivity extends BaseActivity {
    private ImageView mCollectView;
    private boolean mIsSelected;
    private ListView malbumlist;
    private List<AlbumInfo> albumInfos = null;
    AlbumListAdapter listAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albumlist_main);
        malbumlist= (ListView) findViewById(R.id.albumlist);
        setbar("专辑","");
        final TitleBar titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.setBackgroundColor(Color.parseColor("#CD2626"));
        albumInfos= AlbumListUnit.getAlbumInfo(AlbumListActivity.this);
        listAdapter=new AlbumListAdapter(this,albumInfos);
        malbumlist.setAdapter(listAdapter);
        malbumlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlbumInfo albumInfo= albumInfos.get(i);
                /*Toast.makeText(AlbumListActivity.this," "+albumInfo.getArtist()+" "+albumInfo.getAlbumid(),Toast.LENGTH_SHORT).show();*/
                Intent intent=new Intent(AlbumListActivity.this,AlbumInfoActivity.class);
                intent.putExtra("albumid",String.valueOf(albumInfo.getAlbumid()));
                intent.putExtra("artisrt",albumInfo.getArtist());
                intent.putExtra("albunmtitle",albumInfo.getAlbumtitle());
                intent.putExtra("number_of_song",String.valueOf(albumInfo.getNumber_of_song()));
                startActivity(intent);
            }
        });

    }
}
