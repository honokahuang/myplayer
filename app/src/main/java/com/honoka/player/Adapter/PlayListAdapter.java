package com.honoka.player.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.honoka.player.Domain.AlbumInfo;
import com.honoka.player.Domain.PlayListInfo;
import com.honoka.player.R;

import java.util.List;

/**
 * Created by 41258 on 2016/11/23.
 */

public class PlayListAdapter extends BaseAdapter {
    private PlayListInfo playListInfo;
    private Context context;
    private int pos = -1;
    private List<PlayListInfo> playListInfos;
    private LayoutInflater mInflater = null;
    public PlayListAdapter(Context context,List<PlayListInfo> playListInfos){
        super();
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.playListInfos=playListInfos;
    }
    @Override
    public int getCount() {
        return playListInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder=null;
        if (view == null){
            viewHolder = new ViewHolder();
            view=mInflater.inflate(R.layout.fragment_musiclist_main_playlist,null);
            viewHolder.playlistname= (TextView) view.findViewById(R.id.playlisttitle);
            viewHolder.playlistadded= (TextView) view.findViewById(R.id.playlistadded);
            view.setTag(viewHolder);            //表示给View添加一个格外的数据，
        }else {
            viewHolder = (ViewHolder) view.getTag();//通过getTag的方法将数据取出来
        }
        playListInfo=playListInfos.get(i);
        viewHolder.playlistname.setText(playListInfo.getPlaylistname());
        /*viewHolder.playlistadded.setText(playListInfo.getPlaylistadded());*/
        return view;
    }
    public class ViewHolder{
        public TextView playlistname;
/*        public TextView playlistid;*/
        public TextView playlistadded;
    }
}
