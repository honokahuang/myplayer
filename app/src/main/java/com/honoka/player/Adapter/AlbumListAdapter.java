package com.honoka.player.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.honoka.player.Domain.AlbumInfo;
import com.honoka.player.R;
import com.honoka.player.Utils.PlayListUnit;

import java.util.List;

import static android.R.id.list;

/**
 * Created by 41258 on 2016/11/17.
 */

public class AlbumListAdapter extends BaseAdapter {
    private AlbumInfo albumInfo;
    private Context context;
    private int pos = -1;
    private List<AlbumInfo> albumInfos;

    public AlbumListAdapter(Context context,List<AlbumInfo>albumInfos){
        this.context=context;
        this.albumInfos=albumInfos;
    }
    @Override
    public int getCount() {
        return albumInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return  i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view==null){
            viewHolder= new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.activity_albumlist_albumlist,null);
            viewHolder.albumtitle= (TextView) view.findViewById(R.id.albumtitle);
            viewHolder.album_aritist= (TextView) view.findViewById(R.id.album_aritist);
            viewHolder.albumimage= (ImageView) view.findViewById(R.id.albumimage);
            viewHolder.number_of_song= (TextView) view.findViewById(R.id.number_of_song);
            view.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) view.getTag();
        }
        albumInfo=albumInfos.get(i);
        Bitmap bitmap= PlayListUnit.getArtwork(context,1,albumInfo.getAlbumid(),true,true);
        if (bitmap!=null){
            viewHolder.albumimage.setImageBitmap(bitmap);
        }
        viewHolder.albumtitle.setText(albumInfo.getAlbumtitle());
        viewHolder.album_aritist.setText(albumInfo.getArtist());
        viewHolder.number_of_song.setText("共"+(int) albumInfo.getNumber_of_song()+"首");

        return view;
    }
    public class ViewHolder {
        //所有控件对象引用
        public TextView albumtitle;    //音乐艺术家
        public ImageView albumimage;//
        public TextView album_aritist;//
        public TextView number_of_song;//
    }
}
