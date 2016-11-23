package com.honoka.player.Activity;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.honoka.player.Adapter.MusicListAdapter;
import com.honoka.player.Base.BaseActivity;
import com.honoka.player.Domain.AppConstant;
import com.honoka.player.R;
import com.honoka.player.Custom.TitleBar;
import com.honoka.player.Domain.Mp3Info;
import com.honoka.player.Service.PlayService;
import com.honoka.player.Utils.PlayListUnit;

import java.util.List;

public class HomeActivity extends BaseActivity {
    private ImageView mCollectView;
    private boolean mIsSelected;
    private String title; // 歌曲标题
    private String artist; // 歌曲艺术家
    private String url; // 歌曲路径
    private int currentTime; // 当前歌曲播放时间
    private int duration; // 歌曲长度
    private ListView mMusiclist; // 音乐列表
    private List<Mp3Info> mp3Infos = null;
    private int listPosition = 0; // 标识列表位置
    MusicListAdapter listAdapter; // 改为自定义列表适配器
    private final int from =3;
    public static final String UPDATE_ACTION = "com.honoka.action.UPDATE_ACTION"; // 更新动作
    public static final String MUSIC_CURRENT = "com.honoka.action.MUSIC_CURRENT"; // 音乐当前时间改变动作
    public static final String MUSIC_DURATION = "com.honoka.action.MUSIC_DURATION";// 音乐播放长度改变动作
    public static final String GET_MUSICINFO="com.honoka.action.GET_MUSICINFO";//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_main);
        setbar("音乐播放器","");
        final TitleBar titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.setBackgroundColor(Color.parseColor("#CD2626"));
        settitleright();
        ImageButton download = (ImageButton) findViewById(R.id.download);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,LocalListActivity.class);
                startActivity(intent);
            }
        });
        ImageButton lately = (ImageButton) findViewById(R.id.latelyplay);
        lately.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,MainHomeActivity.class);
                startActivity(intent);
            }
        });
        ImageButton like = (ImageButton) findViewById(R.id.like);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,AlbumListActivity.class);
                startActivity(intent);
            }
        });
        ImageButton local = (ImageButton) findViewById(R.id.localmusic);
        local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (HomeActivity.this,ArtistListActivity.class);
                startActivity(intent);
            }
        });
/*        mMusiclist= (ListView) findViewById(R.id.music_list);
        mp3Infos = PlayListUnit.getMp3Infos(HomeActivity.this,from,0); // 获取歌曲对象集合
        listAdapter = new MusicListAdapter(this, mp3Infos);
        mMusiclist.setAdapter(listAdapter);
        mMusiclist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listPosition = i;
                playMusic(listPosition);
            }
        });*/
    }

    private void settitleright(){
        final TitleBar titleBar = (TitleBar) findViewById(R.id.title_bar);
        mCollectView = (ImageView) titleBar.addAction(new TitleBar.ImageAction(R.mipmap.musicnote) {
            @Override
            public void performAction(View view) {
                if (isServiceRunning(HomeActivity.this,"com.honoka.player.Service.PlayService")){
                    Intent intent = new Intent(HomeActivity.this,PlayService.class);
                    intent.putExtra("MSG",AppConstant.PlayerMsg.GET_INFO);
                    startService(intent);
                }else {
                    Toast.makeText(HomeActivity.this,"暂时没有正在播放的音乐\n请选择需要播放的歌曲",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

/*    public void playMusic(int listPosition) {
        if (mp3Infos != null) {
            Mp3Info mp3Info = mp3Infos.get(listPosition);
            Intent intent = new Intent(HomeActivity.this, PlayActivity.class); // 定义Intent对象，跳转到PlayActivity
            // 添加一系列要传递的数据
            intent.putExtra("title", mp3Info.getTitle());
            intent.putExtra("url", mp3Info.getUrl());
            intent.putExtra("artist", mp3Info.getArtist());
            intent.putExtra("listPosition", listPosition);
            intent.putExtra("currentTime", currentTime);
            intent.putExtra("from",String.valueOf(from));
            intent.putExtra("fromid",String.valueOf(from));
            intent.putExtra("MSG", AppConstant.PlayerMsg.PLAY_MSG);
            startActivity(intent);
        }
    }*/

}
