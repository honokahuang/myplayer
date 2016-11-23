package com.honoka.player.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.honoka.player.Activity.PlayActivity;
import com.honoka.player.Custom.LrcProcess;
import com.honoka.player.Domain.AppConstant;
import com.honoka.player.Domain.LrcContent;
import com.honoka.player.Domain.Mp3Info;
import com.honoka.player.R;
import com.honoka.player.Utils.PlayListUnit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Honoka on 2016/11/2.
 */

public class PlayService extends Service{
    private MediaPlayer mediaPlayer; // 媒体播放器对象
    private String path; 			// 音乐文件路径
    private int msg;				//播放信息
    private boolean isPause; 		// 暂停状态
    private int current = 0; 		// 记录当前正在播放的音乐
    private List<Mp3Info> mp3Infos;	//存放Mp3Info对象的集合
    private int status = 3;			//播放状态，默认为顺序播放
    private MyReceiver myReceiver;	//自定义广播接收器
    private int currentTime;		//当前播放进度
    private int duration;			//播放长度
    private LrcProcess mLrcProcess;	//歌词处理
    private List<LrcContent> lrcList = new ArrayList<LrcContent>(); //存放歌词列表对象
    private int index = 0;			//歌词检索值
    private int from=3;
    private int fromid=3;
    private String title;
    private String artist;
    private String tile_activity;
    private String artist_service;


    //服务要发送的一些Action
    public static final String UPDATE_ACTION = "com.honoka.action.UPDATE_ACTION"; // 更新动作
    public static final String CTL_ACTION = "com.honoka.action.CTL_ACTION"; // 控制动作
    public static final String MUSIC_CURRENT = "com.honoka.action.MUSIC_CURRENT"; // 音乐当前时间改变动作
    public static final String MUSIC_DURATION = "com.honoka.action.MUSIC_DURATION";// 音乐播放长度改变动作
    public static final String MUSIC_PLAYING = "com.honoka.action.MUSIC_PLAYING"; // 音乐正在播放动作
    public static final String MUSIC_MODEL = "com.honoka.action.MUSIC_MODEL"; // 音乐正在播放动作
    public static final String SHOW_LRC = "com.honoka.action.SHOW_LRC"; // 通知显示歌词
    public final static String ACTION_BUTTON = "com.honoka.player.action.ButtonClick";//notification的action
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String path_tmp=intent.getStringExtra("url");
        title=intent.getStringExtra("title");
        artist=intent.getStringExtra("artist");
         if (path_tmp!=null){
             path = path_tmp;		//歌曲路径
         }
        int tmp3=intent.getIntExtra("listPosition", -1);
        if (tmp3 != -1){
            current = intent.getIntExtra("listPosition", -1);
        }
        	//当前播放歌曲的在mp3Infos的位置
        Log.d("Service","this id："+current);
        msg = intent.getIntExtra("MSG", 0);			//播放信息
        String tmp=intent.getStringExtra("from");
        String tmp1=intent.getStringExtra("fromid");
        if (tmp!=null&&tmp1!=null){
            from=Integer.valueOf(tmp);
            fromid=Integer.valueOf(tmp1);
        }

        mp3Infos= PlayListUnit.getMp3Infos(PlayService.this,from,fromid);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (status == 1) { // 单曲循环
                    mediaPlayer.start();
                } else if (status == 2) { // 全部循环
                    current++;
                    if(current > mp3Infos.size() - 1) {	//变为第一首的位置继续播放
                        current = 0;
                    }
                    Intent sendIntent = new Intent(UPDATE_ACTION);
                    sendIntent.putExtra("current", current);
                    // 发送广播，将被Activity组件中的BroadcastReceiver接收到
                    sendBroadcast(sendIntent);
                    path = mp3Infos.get(current).getUrl();
                    play(0);
                } else if (status == 3) { // 顺序播放
                    current++;	//下一首位置
                    Log.d("Service","顺序播放下一首");
                    if (current <= mp3Infos.size() - 1) {
                        Intent sendIntent = new Intent(UPDATE_ACTION);
                        sendIntent.putExtra("current", current);
                        // 发送广播，将被Activity组件中的BroadcastReceiver接收到
                        sendBroadcast(sendIntent);
                        path = mp3Infos.get(current).getUrl();
                        play(0);
                    }else {
                        mediaPlayer.seekTo(0);
                        current = 0;
                        Intent sendIntent = new Intent(UPDATE_ACTION);
                        sendIntent.putExtra("current", current);
                        // 发送广播，将被Activity组件中的BroadcastReceiver接收到
                        sendBroadcast(sendIntent);
                    }
                } else if(status == 4) {	//随机播放
                    current = getRandomIndex(mp3Infos.size() - 1);
                    System.out.println("currentIndex ->" + current);
                    Intent sendIntent = new Intent(UPDATE_ACTION);
                    sendIntent.putExtra("current", current);
                    // 发送广播，将被Activity组件中的BroadcastReceiver接收到
                    sendBroadcast(sendIntent);
                    path = mp3Infos.get(current).getUrl();
                    play(0);
                }
            }
        });
        if (msg == AppConstant.PlayerMsg.PLAY_MSG) {	//直接播放音乐
            play(0);
            showButtonNotify(mp3Infos.get(current));
        } else if (msg == AppConstant.PlayerMsg.PAUSE_MSG) {	//暂停
            pause();
            showButtonNotify(mp3Infos.get(current));
        } else if (msg == AppConstant.PlayerMsg.STOP_MSG) {	//停止
            stop();
            showButtonNotify(mp3Infos.get(current));
        } else if (msg == AppConstant.PlayerMsg.CONTINUE_MSG) {	//继续播放
            resume();
            showButtonNotify(mp3Infos.get(current));
        } else if (msg == AppConstant.PlayerMsg.PRIVIOUS_MSG) {	//上一首
            previous();
            showButtonNotify(mp3Infos.get(current));
        } else if (msg == AppConstant.PlayerMsg.NEXT_MSG) {//下一首
            next();
            showButtonNotify(mp3Infos.get(current));
        } else if (msg == AppConstant.PlayerMsg.PROGRESS_CHANGE) {	//进度更新
            currentTime = intent.getIntExtra("progress", -1);
            play(currentTime);
        } else if (msg == AppConstant.PlayerMsg.GET_INFO){
            returnActivity();
        }
        return super.onStartCommand(intent, flags, startId);
    }
    private void returnActivity(){
        String tmp_pause1;
        Intent activityintent = new Intent(PlayService.this,PlayActivity.class);
        activityintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activityintent.putExtra("MSG",AppConstant.PlayerMsg.PLAYING_MSG);
        activityintent.putExtra("status",String.valueOf(status));
        activityintent.putExtra("listPosition", current);
        activityintent.putExtra("duration",String.valueOf(duration));
        activityintent.putExtra("from",String.valueOf(from));
        activityintent.putExtra("fromid",String.valueOf(fromid));
        activityintent.putExtra("isPause",isPause);
        startActivity(activityintent);
        showButtonNotify(mp3Infos.get(current));
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1) {
                if(mediaPlayer != null) {
                    currentTime = mediaPlayer.getCurrentPosition(); // 获取当前音乐播放的位置
                    Intent intent = new Intent();
                    intent.setAction(MUSIC_CURRENT);
                    intent.putExtra("currentTime", currentTime);
                    sendBroadcast(intent); // 给PlayerActivity发送广播
                    handler.sendEmptyMessageDelayed(1, 1000);
                }
            }
        };
    };


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Service","后台服务已启动");
        mediaPlayer=new MediaPlayer();

        myReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(CTL_ACTION);
        filter.addAction(SHOW_LRC);
        filter.addAction(MUSIC_PLAYING);
        filter.addAction(ACTION_BUTTON);
        registerReceiver(myReceiver, filter);
    }
    protected int getRandomIndex(int end) {
        int index = (int) (Math.random() * end);
        return index;
    }


    /**
     * 播放音乐
     */
    private void play(int currentTime) {
        try {
            initLrc();
            mediaPlayer.reset();// 把各项参数恢复到初始状态
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare(); // 进行缓冲
            mediaPlayer.setOnPreparedListener(new PreparedListener(currentTime));// 注册一个监听器
            Log.d("Service","播放");
            handler.sendEmptyMessage(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initLrc(){
        //初始化歌词配置
        mLrcProcess = new LrcProcess();
        //读取歌词文件
        mLrcProcess.readLRC(mp3Infos.get(current).getUrl());
        //传回处理后的歌词文件
        lrcList = mLrcProcess.getLrcList();
        PlayActivity.lrcView.setmLrcList(lrcList);
        //切换带动画显示歌词
/*        PlayActivity.lrcView.setAnimation(AnimationUtils.loadAnimation(PlayService.this, R.anim.alpha_z));*/
        handler.post(mRunnable);
    }
    Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            PlayActivity.lrcView.setIndex(lrcIndex());
            PlayActivity.lrcView.invalidate();
            handler.postDelayed(mRunnable, 100);
        }
    };


    public int lrcIndex() {
        //根据时间获取歌词显示的索引值
        if(mediaPlayer.isPlaying()) {
            currentTime = mediaPlayer.getCurrentPosition();
            duration = mediaPlayer.getDuration();
        }
        if(currentTime < duration) {
            for (int i = 0; i < lrcList.size(); i++) {
                if (i < lrcList.size() - 1) {
                    if (currentTime < lrcList.get(i).getLrcTime() && i == 0) {
                        index = i;
                    }
                    if (currentTime > lrcList.get(i).getLrcTime()
                            && currentTime < lrcList.get(i + 1).getLrcTime()) {
                        index = i;
                    }
                }
                if (i == lrcList.size() - 1
                        && currentTime > lrcList.get(i).getLrcTime()) {
                    index = i;
                }
            }
        }
        return index;
    }

    /**
     * 暂停音乐
     */
    private void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPause = true;
        }
    }

    private void resume() {
        if (isPause) {
            mediaPlayer.start();
            isPause = false;
        }
    }

    /**
     * 上一首
     */
    private void previous() {
        Intent sendIntent = new Intent(UPDATE_ACTION);
        sendIntent.putExtra("current", current);
        // 发送广播，将被Activity组件中的BroadcastReceiver接收到
        sendBroadcast(sendIntent);
        isPause=false;
        play(0);
    }

    /**
     * 下一首
     */
    private void next() {
        Intent sendIntent = new Intent(UPDATE_ACTION);
        sendIntent.putExtra("current", current);
        // 发送广播，将被Activity组件中的BroadcastReceiver接收到
        sendBroadcast(sendIntent);
        isPause=false;
        play(0);
    }

    /**
     * 停止音乐
     */
    private void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            try {
                mediaPlayer.prepare(); // 在调用stop后如果需要再次通过start进行播放,需要之前调用prepare函数
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private final class PreparedListener implements MediaPlayer.OnPreparedListener {
        private int currentTime;

        public PreparedListener(int currentTime) {
            this.currentTime = currentTime;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            mediaPlayer.start(); // 开始播放
            if (currentTime > 0) { // 如果音乐不是从头播放
                mediaPlayer.seekTo(currentTime);
            }
            Intent intent = new Intent();
            intent.setAction(MUSIC_DURATION);
            duration = mediaPlayer.getDuration();
            intent.putExtra("duration", duration);	//通过Intent来传递歌曲的总长度
            sendBroadcast(intent);
        }
    }

    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int control = intent.getIntExtra("control", -1);
            switch (control) {
                case 1:
                    status = 1; // 将播放状态置为1表示：单曲循环
                    break;
                case 2:
                    status = 2;	//将播放状态置为2表示：全部循环
                    break;
                case 3:
                    status = 3;	//将播放状态置为3表示：顺序播放
                    break;
                case 4:
                    status = 4;	//将播放状态置为4表示：随机播放
                    break;
            }

            String action = intent.getAction();
            if(action.equals(SHOW_LRC)){
                current = intent.getIntExtra("listPosition", -1);
                initLrc();
            }else if (action.equals(MUSIC_PLAYING)){
                Intent sendIntent = new Intent(UPDATE_ACTION);
                sendIntent.putExtra("current", current);
                sendIntent.putExtra("duration",duration);
                // 发送广播，将被Activity组件中的BroadcastReceiver接收到
                sendBroadcast(sendIntent);
            }else if (action.equals(ACTION_BUTTON)){
                //通过传递过来的ID判断按钮点击属性或者通过getResultCode()获得相应点击事件
                int buttonId = intent.getIntExtra(INTENT_BUTTONID_TAG, 0);
                switch (buttonId) {
                    case BUTTON_PREV_ID:
                        current--;	//上一首位置
                        Log.d("Service","上一首");
                        if (current > 0) {
                            Intent sendIntent = new Intent(UPDATE_ACTION);
                            sendIntent.putExtra("current", current);
                            // 发送广播，将被Activity组件中的BroadcastReceiver接收到
                            sendBroadcast(sendIntent);
                            path = mp3Infos.get(current).getUrl();
                            isPause=false;
                            play(0);
                        }else {
                            mediaPlayer.seekTo(0);
                            current = 0;
                            Intent sendIntent = new Intent(UPDATE_ACTION);
                            sendIntent.putExtra("current", current);
                            // 发送广播，将被Activity组件中的BroadcastReceiver接收到
                            sendBroadcast(sendIntent);
                        }
                        showButtonNotify(mp3Infos.get(current));
                        break;
                    case BUTTON_PALY_ID:
                        if(isPause){
                            resume();
                            showButtonNotify(mp3Infos.get(current));
                        }else{
                            pause();
                            showButtonNotify(mp3Infos.get(current));
                        }
                        Intent intent3 = new Intent(MUSIC_MODEL);
                        intent3.putExtra("isPause", isPause);
                        // 发送广播，将被Activity组件中的BroadcastReceiver接收到
                        sendBroadcast(intent3);
                        showButtonNotify(mp3Infos.get(current));
                        Log.d("playactivity" , "");
                        break;
                    case BUTTON_NEXT_ID:
                        current++;	//下一首位置
                        Log.d("Service","下一首");
                        if (current <= mp3Infos.size() - 1) {
                            Intent sendIntent = new Intent(UPDATE_ACTION);
                            sendIntent.putExtra("current", current);
                            // 发送广播，将被Activity组件中的BroadcastReceiver接收到
                            sendBroadcast(sendIntent);
                            path = mp3Infos.get(current).getUrl();
                            isPause=false;
                            play(0);
                        }else {
                           current--;
                        }
                        showButtonNotify(mp3Infos.get(current));
                        break;
                    case BUTTON_RETURN_ID:
                        returnActivity();
                        break;

                    default:
                        break;
                }

            }
        }
    }

    public void showButtonNotify(Mp3Info mp3Info){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        RemoteViews mRemoteViews = new RemoteViews(getPackageName(), R.layout.notification_view);
        Bitmap bm = PlayListUnit.getArtwork(this, mp3Info.getId(), mp3Info.getAlbumId(), true, true);
        mRemoteViews.setImageViewBitmap(R.id.custom_song_icon, bm);
        //API3.0 以上的时候显示按钮，否则消失
        mRemoteViews.setTextViewText(R.id.tv_custom_song_singer, mp3Info.getArtist());
        mRemoteViews.setTextViewText(R.id.tv_custom_song_name, mp3Info.getTitle());
        mRemoteViews.setViewVisibility(R.id.ll_custom_button, View.VISIBLE);
        //
        if(isPause){
            mRemoteViews.setImageViewResource(R.id.btn_custom_play, R.drawable.play_selector);
        }else{
            mRemoteViews.setImageViewResource(R.id.btn_custom_play, R.drawable.pause_selector);
        }
        //点击的事件处理
        Intent buttonIntent = new Intent(ACTION_BUTTON);
		/* 上一首按钮 */
        buttonIntent.putExtra(INTENT_BUTTONID_TAG, BUTTON_PREV_ID);
        //这里加了广播，所及INTENT的必须用getBroadcast方法
        PendingIntent intent_prev = PendingIntent.getBroadcast(this, 1, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.btn_custom_prev, intent_prev);
		/* 播放/暂停  按钮 */
        buttonIntent.putExtra(INTENT_BUTTONID_TAG, BUTTON_PALY_ID);
        PendingIntent intent_paly = PendingIntent.getBroadcast(this, 2, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.btn_custom_play, intent_paly);
		/* 下一首 按钮  */
        buttonIntent.putExtra(INTENT_BUTTONID_TAG, BUTTON_NEXT_ID);
        PendingIntent intent_next = PendingIntent.getBroadcast(this, 3, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.btn_custom_next, intent_next);

        buttonIntent.putExtra(INTENT_BUTTONID_TAG,BUTTON_RETURN_ID);
        PendingIntent intent_return= PendingIntent.getBroadcast(this,4,buttonIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.custom_song_icon,intent_return);

        mBuilder.setContent(mRemoteViews)
                .setContentIntent(getDefalutIntent(Notification.FLAG_ONGOING_EVENT))
                .setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
                .setTicker("正在播放")
                .setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
                .setOngoing(true)
                .setSmallIcon(R.drawable.music5);
        Notification notify = mBuilder.build();
        notify.flags = Notification.FLAG_ONGOING_EVENT;
        //会报错，还在找解决思路
       /*notify.contentView = mRemoteViews;*/
        notify.contentIntent = PendingIntent.getActivity(this, 0, new Intent(), 0);
        mNotificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(200, notify);
    }
    public final static String INTENT_BUTTONID_TAG = "ButtonId";
    /** 上一首 按钮点击 ID */
    public final static int BUTTON_PREV_ID = 1;
    /** 播放/暂停 按钮点击 ID */
    public final static int BUTTON_PALY_ID = 2;
    /** 下一首 按钮点击 ID */
    public final static int BUTTON_NEXT_ID = 3;
    public final static int BUTTON_RETURN_ID = 4;
    /** Notification管理 */
    public NotificationManager mNotificationManager;
    public PendingIntent getDefalutIntent(int flags){
        PendingIntent pendingIntent= PendingIntent.getActivity(this, 1, new Intent(), flags);
        return pendingIntent;
    }
    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        handler.removeCallbacks(mRunnable);
        unregisterReceiver(myReceiver);
        mNotificationManager.cancelAll();
    }
}
