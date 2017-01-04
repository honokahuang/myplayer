package com.honoka.player.Activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.honoka.player.Adapter.PlayListAdapter;
import com.honoka.player.Base.BaseActivity;
import com.honoka.player.Custom.LrcView;
import com.honoka.player.Custom.TitleBar;
import com.honoka.player.Domain.AppConstant;
import com.honoka.player.Domain.PlayListInfo;
import com.honoka.player.R;
import com.honoka.player.Domain.Mp3Info;
import com.honoka.player.Utils.ImageUtil;
import com.honoka.player.Utils.MyPlayListUnit;
import com.honoka.player.Utils.PlayListUnit;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Honoka on 2016/11/2.
 */

public class PlayActivity extends BaseActivity {
    private ImageView mCollectView;
    private TextView musicTitle = null;
    private TextView musicArtist = null;
    private Button previousBtn; // 上一首
    private Button playModel; // 重复（单曲循环、全部循环）
    private Button playBtn; // 播放（播放、暂停）
    private Button nextBtn; // 下一首
    private Button queueBtn; // 歌曲列表
    private SeekBar music_progressBar; // 歌曲进度
    private TextView currentProgress; // 当前进度消耗的时间
    private TextView finalProgress; // 歌曲时间
    private String title; // 歌曲标题
    private String artist; // 歌曲艺术家
    private String url; // 歌曲路径
    private int listPosition; // 播放歌曲在mp3Infos的位置
    private int currentTime; // 当前歌曲播放时间
    private int duration; // 歌曲长度
    private int flag; // 播放标识
    private ImageView musicAlbum;//音乐专辑封面
    private ImageView musicAblumReflection;	//倒影反射
    private int from;
    private int fromid;
    private int repeatState = 0;



    private final int isCurrentRepeat = 1; // 单曲循环
    private final int isAllRepeat = 2; // 全部循环
    private final int isNoneRepeat = 3; // 无重复播放
    private final int isShuffle = 4;//顺序播放
    private boolean isPlaying; // 正在播放
    private boolean isPause; // 暂停
    private boolean Pausing;
    public static final String UPDATE_ACTION = "com.honoka.action.UPDATE_ACTION"; // 更新动作
    public static final String CTL_ACTION = "com.honoka.action.CTL_ACTION"; // 控制动作
    public static final String MUSIC_CURRENT = "com.honoka.action.MUSIC_CURRENT"; // 音乐当前时间改变动作
    public static final String MUSIC_DURATION = "com.honoka.action.MUSIC_DURATION";// 音乐播放长度改变动作
    public final static String ACTION_BUTTON = "com.honoka.player.action.ButtonClick";
    public static final String MUSIC_MODEL = "com.honoka.action.MUSIC_MODEL"; // 音乐正在播放动作
    public static final String MODEL_ACTION = "com.honoka.action.MODEL_ACTION"; // 音乐重复播放动作
    public static final String REPEAT_ACTION = "com.honoka.action.REPEAT_ACTION"; // 音乐重复播放动作
    public static final String SHUFFLE_ACTION = "com.honoka.action.SHUFFLE_ACTION";// 音乐随机播放动作
    public static final String SHOW_LRC = "com.honoka.action.SHOW_LRC"; // 通知显示歌词
    private List<Mp3Info> mp3Infos;
    public static LrcView lrcView; // 自定义歌词视图
    private PlayerReceiver playerReceiver;


    private AudioManager am;		//音频管理引用，提供对音频的控制
    RelativeLayout ll_player_voice;	//音量控制面板布局
    int currentVolume;				//当前音量
    int maxVolume;					//最大音量
    ImageButton ibtn_player_voice;	//显示音量控制面板的按钮
    SeekBar player_voice;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_main);
        getIntentData();//获得Intent里面的数据
        mp3Infos = PlayListUnit.getMp3Infos(PlayActivity.this,from,fromid);
        if (repeatState == 0){
            repeatState=isNoneRepeat;
        }
        setbar(title,artist);//设置titlebar
        settitleright();
        setfindViewById();
        setViewOnclickListener();
        music_progressBar.setProgress(currentTime);
        music_progressBar.setMax(duration);
        setView();
        playBtn.setBackgroundResource(R.drawable.pause_selector);
        if (flag == AppConstant.PlayerMsg.PLAY_MSG){
            playmusic();
        }else if (flag == AppConstant.PlayerMsg.PLAYING_MSG){
            resettitlebar(mp3Infos.get(listPosition).getTitle(),mp3Infos.get(listPosition).getArtist());
            showArtwork(mp3Infos.get(listPosition));
            if (Pausing){
                playBtn.setBackgroundResource(R.drawable.play_selector);
                isPause = true;
            }
            long tmp_duration=mp3Infos.get(listPosition).getDuration();
            music_progressBar.setMax((int)mp3Infos.get(listPosition).getDuration());
            finalProgress.setText(PlayListUnit.formatTime((int)mp3Infos.get(listPosition).getDuration()));
            /*showButtonNotify(mp3Infos.get(listPosition));*/
        }
    }


    private void setView() {
        switch (repeatState){
            case isCurrentRepeat:
                playModel.setClickable(false);
                playModel.setBackgroundResource(R.drawable.repeat_current_selector);
                break;
            case isAllRepeat:
                playModel.setClickable(false);
                playModel.setBackgroundResource(R.drawable.repeat_all_selector);
                break;
            case isNoneRepeat:
                playModel.setClickable(true);
                playModel.setBackgroundResource(R.drawable.repeat_none_selector);
                break;
            case isShuffle:
                playModel.setBackgroundResource(R.drawable.shuffle_selector);
                playModel.setClickable(false);
                break;
        }
    }

    @Override
    protected void onPause() {
        unregisterReceiver(playerReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        registerReceiver();
        super.onResume();
    }

    private void setViewOnclickListener() {
        ViewOnclickListener ViewOnClickListener = new ViewOnclickListener();
        previousBtn.setOnClickListener(ViewOnClickListener);
        playBtn.setOnClickListener(ViewOnClickListener);
        playModel.setOnClickListener(ViewOnClickListener);
        nextBtn.setOnClickListener(ViewOnClickListener);
        queueBtn.setOnClickListener(ViewOnClickListener);
        music_progressBar.setOnSeekBarChangeListener(new SeekBarChangeListener());
    }
    private class SeekBarChangeListener implements SeekBar.OnSeekBarChangeListener{

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser){
                        audioTrackChang(progress);
                    }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    private void playmusic(){
        //从播放列表点击进来的
        isPlaying = true;
        isPause = false;
        Intent intent1 = new Intent(CTL_ACTION);
        intent1.putExtra("control", repeatState);
        sendBroadcast(intent1);
        Mp3Info mp3Info=mp3Infos.get(listPosition);
        showArtwork(mp3Info);
        playBtn.setBackgroundResource(R.drawable.pause_selector);
        Intent intent = new Intent();
        intent.setAction("com.honoka.media.MUSIC_SERVICE");
        intent.putExtra("url",url);
        intent.putExtra("listPosition",listPosition);
        intent.putExtra("title",title);
        intent.putExtra("artist",artist);
        intent.putExtra("MSG",flag);
        intent.setPackage("com.honoka.player");
        intent.putExtra("from",String.valueOf(from));
        intent.putExtra("fromid",String.valueOf(fromid));
        startService(intent);

    }

    public void repeat_one() {
        //单曲循环
        Intent intent = new Intent(CTL_ACTION);
        intent.putExtra("control", 1);
        sendBroadcast(intent);
    }

    public void repeat_all() {
        //全部循环
        Intent intent = new Intent(CTL_ACTION);
        intent.putExtra("control", 2);
        sendBroadcast(intent);
    }

    public void repeat_none() {
        //顺序播放
        Intent intent = new Intent(CTL_ACTION);
        intent.putExtra("control", 3);
        sendBroadcast(intent);
    }

    public void shuffleMusic() {
        //随机播放
        Intent intent = new Intent(CTL_ACTION);
        intent.putExtra("control", 4);
        sendBroadcast(intent);
    }
    private void audioTrackChang(int progress) {
        //实现移动音乐播放条改变播放时间
        Intent intent = new Intent();
        intent.setAction("com.honoka.media.MUSIC_SERVICE");
        intent.putExtra("url",url);
        intent.putExtra("listPosition",listPosition);
        intent.putExtra("MSG",AppConstant.PlayerMsg.PROGRESS_CHANGE);
        intent.putExtra("progress",progress);
        intent.setPackage("com.honoka.player");
        startService(intent);
    }

    private class ViewOnclickListener implements View.OnClickListener{
        Intent intent =new Intent();
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.play_music:
                    if (isPause == false&&isPause == false){
                        isPlaying = true;
                        isPause = false;
                    }
                    if (isPlaying){
                        playBtn.setBackgroundResource(R.drawable.play_selector);
                        intent.setAction("com.honoka.media.MUSIC_SERVICE");
                        intent.putExtra("MSG", AppConstant.PlayerMsg.PAUSE_MSG);
                        intent.setPackage("com.honoka.player");
                        startService(intent);
                        isPlaying=false;
                        isPause=true;
                    }else if (isPause){
                        playBtn.setBackgroundResource(R.drawable.pause_selector);
                        intent.setAction("com.honoka.media.MUSIC_SERVICE");
                        intent.putExtra("MSG", AppConstant.PlayerMsg.CONTINUE_MSG);
                        intent.setPackage("com.honoka.player");
                        startService(intent);
                        isPlaying=true;
                        isPause=false;
                    }
                    break;
                case R.id.previous_music:
                    previous_music();
                    break;
                case R.id.next_music:
                    next_music();
                    break;
                case R.id.play_model:
                    play_model();
                  break;
                case R.id.play_queue:
                    play_queue();
                    break;
            }
        }
    }

    private void play_model() {
        if (repeatState == isNoneRepeat) {
            repeat_one();
            repeatState = isCurrentRepeat;
        } else if (repeatState == isCurrentRepeat) {
            repeat_all();
            repeatState = isAllRepeat;
        } else if (repeatState == isAllRepeat) {
            repeat_none();
            repeatState = isShuffle;
        }else if (repeatState == isShuffle){
            shuffleMusic();
            repeatState = isNoneRepeat;
        }
        Intent intent = new Intent (MODEL_ACTION);
        switch (repeatState){
            case isCurrentRepeat://1
                playModel.setBackgroundResource(R.drawable.repeat_current_selector);
                Toast.makeText(PlayActivity.this,"切换为单曲循环模式",Toast.LENGTH_SHORT).show();
                intent.putExtra("repeatState", isCurrentRepeat);
                sendBroadcast(intent);
                break;
            case isAllRepeat://2
                playModel.setBackgroundResource(R.drawable.repeat_all_selector);
                Toast.makeText(PlayActivity.this,"切换为列表循环模式",Toast.LENGTH_SHORT).show();
                intent.putExtra("repeatState", isCurrentRepeat);
                sendBroadcast(intent);
                break;
            case isNoneRepeat://3
                playModel.setBackgroundResource(R.drawable.repeat_none_selector);
                Toast.makeText(PlayActivity.this,"切换为列表播放模式",Toast.LENGTH_SHORT).show();
                intent.putExtra("repeatState", isCurrentRepeat);
                sendBroadcast(intent);
                break;
            case isShuffle://4
                playModel.setBackgroundResource(R.drawable.shuffle_selector);
                Toast.makeText(PlayActivity.this,"切换为随机播放模式",Toast.LENGTH_SHORT).show();
                intent.putExtra("repeatState", isCurrentRepeat);
                sendBroadcast(intent);
                break;

        }
    }

    private void play_queue() {
        //这是设置paly_queue按钮
        LayoutInflater layoutInflater= (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View playQueueLayout = layoutInflater.inflate(R.layout.activity_play_queue,(ViewGroup)findViewById(R.id.play_queue_layout));
        ListView queuelist = (ListView) playQueueLayout.findViewById(R.id.lv_play_queue);
        List<HashMap<String,String>> queues = PlayListUnit.getMusicMaps(mp3Infos);
        SimpleAdapter adapter=new SimpleAdapter(this,queues,R.layout.activity_play_queue_item,new String[]{"title","Artist","duration"},new int[]{R.id.music_title,R.id.music_Artist,R.id.music_duration});
        queuelist.setAdapter(adapter);
        AlertDialog.Builder builder;
        final AlertDialog dialog;
        builder=new AlertDialog.Builder(this);
        builder.setTitle("正在播放");
        dialog=builder.create();
        Window window=dialog.getWindow();
        WindowManager.LayoutParams lp=window.getAttributes();
        window.setGravity(Gravity.LEFT | Gravity.TOP);
        lp.alpha=0.9f;
        lp.width = 300;
        lp.height = 600;
        window.setAttributes(lp);
        dialog.setView(playQueueLayout);
        dialog.show();

        queuelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mp3Infos != null) {
                    Mp3Info mp3Info = mp3Infos.get(i);
                    showArtwork(mp3Info);
                    url=mp3Info.getUrl();
                    playBtn.setBackgroundResource(R.drawable.pause_selector);
                    Intent intent = new Intent();
                    intent.setAction("com.honoka.media.MUSIC_SERVICE");
                    intent.putExtra("url",url);
                    intent.putExtra("listPosition",i);
                    intent.putExtra("MSG",AppConstant.PlayerMsg.NEXT_MSG);
                    intent.setPackage("com.honoka.player");
                    startService(intent);
                    dialog.cancel();

                }
            }
        });
    }

    private void next_music() {
        //这是设置next_music按钮
        listPosition = listPosition+1;
        if (listPosition<=mp3Infos.size()-1){
            Mp3Info mp3Info=mp3Infos.get(listPosition);
            showArtwork(mp3Info);
            url=mp3Info.getUrl();
            playBtn.setBackgroundResource(R.drawable.pause_selector);
            Intent intent = new Intent();
            intent.setAction("com.honoka.media.MUSIC_SERVICE");
            intent.putExtra("url",url);
            intent.putExtra("listPosition",listPosition);
            intent.putExtra("MSG",AppConstant.PlayerMsg.NEXT_MSG);
            intent.setPackage("com.honoka.player");
            startService(intent);
        }else {
            listPosition = mp3Infos.size();
            listPosition = listPosition-1;
            Toast.makeText(PlayActivity.this,"没有下一首了",Toast.LENGTH_SHORT).show();
        }
    }

    private void previous_music() {
        //这是设置previous_music按钮
        listPosition = listPosition -1;
        if (listPosition >=0 ){
            Mp3Info mp3Info=mp3Infos.get(listPosition);
            showArtwork(mp3Info);
            url=mp3Info.getUrl();
            playBtn.setBackgroundResource(R.drawable.pause_selector);
            Intent intent=new Intent();
            intent.setAction("com.honoka.media.MUSIC_SERVICE");
            intent.putExtra("url",url);
            intent.putExtra("listPosition",listPosition);
            intent.putExtra("MSG", AppConstant.PlayerMsg.PRIVIOUS_MSG);
            intent.setPackage("com.honoka.player");
            startService(intent);
        }else {
            listPosition=0;
            Toast.makeText(PlayActivity.this,"没有上一首了",Toast.LENGTH_SHORT).show();
        }
    }

    private void setfindViewById() {
        previousBtn= (Button) findViewById(R.id.previous_music);
        playBtn= (Button) findViewById(R.id.play_music);
        playModel= (Button) findViewById(R.id.play_model);
        nextBtn= (Button) findViewById(R.id.next_music);
        queueBtn= (Button) findViewById(R.id.play_queue);
        music_progressBar= (SeekBar) findViewById(R.id.audioTrack);
        currentProgress= (TextView) findViewById(R.id.current_progress);
        finalProgress= (TextView) findViewById(R.id.final_progress);
        lrcView= (LrcView) findViewById(R.id.lrcShowView);
        musicAlbum= (ImageView) findViewById(R.id.iv_music_ablum);
        musicAblumReflection= (ImageView) findViewById(R.id.iv_music_ablum_reflection);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        title = bundle.getString("title","");
        artist = bundle.getString("artist","");
        url = bundle.getString("url","");
        from=Integer.valueOf(bundle.getString("from",""));
        fromid=Integer.valueOf(bundle.getString("fromid"));
        listPosition = bundle.getInt("listPosition");
        currentTime = bundle.getInt("currentTime");
        flag=bundle.getInt("MSG");
        Pausing=bundle.getBoolean("isPause",false);
    }

    private void registerReceiver() {
        //定义和注册广播接收器
        playerReceiver = new PlayerReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(UPDATE_ACTION);
        filter.addAction(MUSIC_CURRENT);
        filter.addAction(MUSIC_DURATION);
        filter.addAction(MUSIC_MODEL);
        registerReceiver(playerReceiver, filter);
    }

    /**
     * 用来接收从service传回来的广播的内部类
     */
    public class PlayerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(MUSIC_CURRENT)) {
                currentTime = intent.getIntExtra("currentTime", -1);
                currentProgress.setText(PlayListUnit.formatTime(currentTime));
                music_progressBar.setProgress(currentTime);
            } else if (action.equals(MUSIC_DURATION)) {
                int duration = intent.getIntExtra("duration", -1);
                music_progressBar.setMax(duration);
                finalProgress.setText(PlayListUnit.formatTime(duration));
            } else if (action.equals(UPDATE_ACTION)) {
                // 获取Intent中的current消息，current代表当前正在播放的歌曲
                listPosition = intent.getIntExtra("current", -1);
                url = mp3Infos.get(listPosition).getUrl();
                if (listPosition >= 0) {
                    resettitlebar(mp3Infos.get(listPosition).getTitle(),mp3Infos.get(listPosition).getArtist());
                    showArtwork(mp3Infos.get(listPosition));
                    music_progressBar.setMax(duration);
                }
                if (listPosition == 0) {
                    finalProgress.setText(PlayListUnit.formatTime(mp3Infos.get(
                            listPosition).getDuration()));
                    playBtn.setBackgroundResource(R.drawable.music_pause_selector);
                    isPause = true;
                }
            }else if (action.equals(MUSIC_MODEL)){
                    isPause=intent.getBooleanExtra("isPause",true);
                if (isPause){
                    playBtn.setBackgroundResource(R.drawable.play_selector);
                }else {
                    playBtn.setBackgroundResource(R.drawable.pause_selector);
                }

            }
        }
    }



    private void showArtwork(Mp3Info mp3Info) {
        Bitmap bm = PlayListUnit.getArtwork(this, mp3Info.getId(), mp3Info.getAlbumId(), true, false);
        //切换播放时候专辑图片出现透明效果
        Animation albumanim = AnimationUtils.loadAnimation(PlayActivity.this, R.anim.album_replace);
        //开始播放动画效果
        musicAlbum.startAnimation(albumanim);
        if(bm != null) {
            musicAlbum.setImageBitmap(bm);	//显示专辑封面图片
            musicAblumReflection.setImageBitmap(ImageUtil.createReflectionBitmapForSingle(bm));	//显示倒影
        } else {
            bm = PlayListUnit.getDefaultArtwork(this, false);
            musicAlbum.setImageBitmap(bm);	//显示专辑封面图片
            musicAblumReflection.setImageBitmap(ImageUtil.createReflectionBitmapForSingle(bm));	//显示倒影
        }

    }

    private void resettitlebar(String retitle,String reartist){
        final TitleBar titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.setTitle(retitle+"\n"+reartist);
        titleBar.setBackgroundColor(Color.parseColor("#00000000"));

    }

    public void addTrackToPlaylist(Context context, long audio_id, long playlist_id, int pos) {
        Uri newuri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlist_id);
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, pos);
        values.put(MediaStore.Audio.Playlists.Members.AUDIO_ID, audio_id);
        values.put(MediaStore.Audio.Playlists.Members.PLAYLIST_ID, playlist_id);
        resolver.insert(newuri, values);

    }
    private void settitleright(){
        final TitleBar titleBar = (TitleBar) findViewById(R.id.title_bar);
        mCollectView = (ImageView) titleBar.addAction(new TitleBar.ImageAction(android.R.drawable.ic_input_add) {
            @Override
            public void performAction(View view) {
                showplaylist();
            }
        });
    }
    public void showplaylist(){
        //这是设置paly_queue按钮
        LayoutInflater layoutInflater= (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View playQueueLayout = layoutInflater.inflate(R.layout.activity_play_queue,(ViewGroup)findViewById(R.id.play_queue_layout));
        ListView queuelist = (ListView) playQueueLayout.findViewById(R.id.lv_play_queue);
        final List<PlayListInfo> playListInfoLists= MyPlayListUnit.getPlayListInfo(this);
        PlayListAdapter listAdapter=new PlayListAdapter(this,playListInfoLists);
        queuelist.setAdapter(listAdapter);
        AlertDialog.Builder builder;
        final AlertDialog dialog;
        builder=new AlertDialog.Builder(this);
        builder.setTitle("请选择需要添加到的播放列表！");
        builder.setNegativeButton("添加到新的播放列表", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent= new Intent(PlayActivity.this,AddPlayListActivity.class);
                startActivity(intent);
            }
        });
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog=builder.create();
        dialog.setView(playQueueLayout);
        dialog.show();
        queuelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mp3Infos != null) {
                    Mp3Info mp3Info = mp3Infos.get(listPosition);
                    playListInfoLists.get(i);
                    addTrackToPlaylist(getApplicationContext(),mp3Info.getId(),playListInfoLists.get(i).getPlaylistid(),0);
                    dialog.cancel();
                    Toast.makeText(PlayActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
