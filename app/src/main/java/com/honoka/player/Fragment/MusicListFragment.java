package com.honoka.player.Fragment;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.honoka.player.Activity.AddPlayListActivity;
import com.honoka.player.Activity.AlbumListActivity;
import com.honoka.player.Activity.ArtistListActivity;
import com.honoka.player.Activity.LocalListActivity;
import com.honoka.player.Activity.PlayListInfoActivity;
import com.honoka.player.Adapter.PlayListAdapter;
import com.honoka.player.Domain.PlayListInfo;
import com.honoka.player.R;
import com.honoka.player.Utils.MyPlayListUnit;

import java.util.List;

/**
 * Created by 41258 on 2016/11/21.
 */

public class MusicListFragment extends ListFragment {
    private View view;
    private CardView localmusic;
    private CardView artistmusic;
    private CardView albummusic;
    private ImageButton addplaylist;
    private ListView mplaylist; // 音乐列表
    private List<PlayListInfo> playListInfoLists = null;
    PlayListAdapter listAdapter; // 改为自定义列表适配器
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        PlayListInfo playListInfo= playListInfoLists.get(position);
                Intent intent = new Intent (getActivity(),PlayListInfoActivity.class);
                intent.putExtra("playlist_id",String.valueOf(playListInfo.getPlaylistid()));
                intent.putExtra("playlist_name",playListInfo.getPlaylistname());
                startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1){
            mplaylist= (ListView) view.findViewById(android.R.id.list);
            playListInfoLists= MyPlayListUnit.getPlayListInfo(getActivity());
            listAdapter=new PlayListAdapter(this.getActivity(),playListInfoLists);
            mplaylist.setAdapter(listAdapter);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null){
            view = inflater.inflate(R.layout.fragment_musiclist_main,container,false);
            initview();

        }else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        mplaylist= (ListView) view.findViewById(android.R.id.list);
        playListInfoLists= MyPlayListUnit.getPlayListInfo(getActivity());
        listAdapter=new PlayListAdapter(this.getActivity(),playListInfoLists);
        mplaylist.setAdapter(listAdapter);
        mplaylist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final PlayListInfo playListInfo= playListInfoLists.get(position);
                //定义AlertDialog.Builder对象，当长按列表项的时候弹出确认删除对话框
                final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                builder.setTitle("提示");
                builder.setMessage("确定删除"+playListInfo.getPlaylistname()+"这个播放列表？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reemove_Playlist(getActivity(),playListInfo.getPlaylistid(),playListInfo.getPlaylistname());
                        listAdapter=new PlayListAdapter(getActivity(),playListInfoLists);
                        listAdapter.notifyDataSetInvalidated();
                        mplaylist.deferNotifyDataSetChanged();
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




        return view;
    }

    private void initview() {
        localmusic= (CardView) view.findViewById(R.id.localmusiclist);
        artistmusic= (CardView) view.findViewById(R.id.artistmusiclist);
        albummusic= (CardView) view.findViewById(R.id.albummusiclist);
        addplaylist= (ImageButton) view.findViewById(R.id.add_playlist);
        ViewOnClickListener viewOnClickListener = new ViewOnClickListener();
        addplaylist.setOnClickListener(viewOnClickListener);
        localmusic.setOnClickListener(viewOnClickListener);
        artistmusic.setOnClickListener(viewOnClickListener);
        albummusic.setOnClickListener(viewOnClickListener);
    }
    public class ViewOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.localmusiclist:
                    LocalListActivity.StartActivity(getActivity());
                    break;
                case R.id.artistmusiclist:
                    ArtistListActivity.StartActivity(getActivity());
                    break;
                case R.id.albummusiclist:
                    AlbumListActivity.StrartActivity(getActivity());
                    break;
                case R.id.add_playlist:
                    AddPlayListActivity.StartAcvivity(getActivity());
                    break;
                default:
            }
        }
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    public void reemove_Playlist(Context context,long playlist_id,String playlist_name){
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(MediaStore.Audio.Playlists._ID,playlist_id);
        resolver.delete(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI,MediaStore.Audio.Playlists._ID + "='" + playlist_id + "'",null);
    }


}
