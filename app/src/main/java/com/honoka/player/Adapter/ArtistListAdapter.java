package com.honoka.player.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.honoka.player.Domain.ArtistInfo;
import com.honoka.player.R;

import java.util.List;

/**
 * Created by 41258 on 2016/11/15.
 */

public class ArtistListAdapter extends BaseAdapter {
    private ArtistInfo artistInfo;
    private Context context;
    private int pos = -1;
    private List<ArtistInfo> artistInfos;

    public ArtistListAdapter(Context context, List<ArtistInfo>artistInfos){
        this.context=context;
        this.artistInfos=artistInfos;
    }

    @Override
    public int getCount() {
        return artistInfos.size();
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
        ViewHolder viewHolder = null;
        if (view == null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.activity_artistlist_artistlist, null);
            viewHolder.Artist= (TextView) view.findViewById(R.id.artist_name);
            viewHolder.Artist_of_track= (TextView) view.findViewById(R.id.number_of_track);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();//通过getTag的方法将数据取出来
        }
        artistInfo=artistInfos.get(i);
        viewHolder.Artist.setText(artistInfo.getArtist());
        viewHolder.Artist_of_track.setText((int) artistInfo.getNumber_of_tracks()+"首");
        return view;
    }
    public class ViewHolder {
        //所有控件对象引用
        public TextView Artist;    //音乐艺术家
        public TextView Artist_of_track;//
    }
}
