package com.honoka.player.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
    private ImageButton localmusic;
    private ImageButton artistmusic;
    private ImageButton albummusic;
    private ImageButton addplaylist;
    private ListView mplaylist; // 音乐列表
    private List<PlayListInfo> playListInfoLists = null;
    PlayListAdapter listAdapter; // 改为自定义列表适配器
   /* public static MusicListFragment newInstance(int i){
        MusicListFragment fragment=new MusicListFragment();
        return fragment;
    }*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null){
            view = inflater.inflate(R.layout.fragment_musiclist_main,container,false);
            localmusic= (ImageButton) view.findViewById(R.id.localmusiclist);
            artistmusic= (ImageButton) view.findViewById(R.id.artistmusiclist);
            albummusic= (ImageButton) view.findViewById(R.id.albummusiclist);
            addplaylist= (ImageButton) view.findViewById(R.id.add_playlist);
            addplaylist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), AddPlayListActivity.class);
                    startActivity(intent);
                }
            });
            localmusic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), LocalListActivity.class);
                    startActivity(intent);
                }
            });
            artistmusic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), ArtistListActivity.class);
                    startActivity(intent);
                }
            });
            albummusic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), AlbumListActivity.class);
                    startActivity(intent);
                }
            });
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
        mplaylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlayListInfo playListInfo= playListInfoLists.get(position);
                Intent intent = new Intent (getActivity(),PlayListInfoActivity.class);
/*                intent.putExtra("playlist_id",String.valueOf(playListInfo.getPlaylistid()));
                intent.putExtra("playlist_name",playListInfo.getPlaylistname());*/
                startActivity(intent);

            }
        });
        mplaylist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });




        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
