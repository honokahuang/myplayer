package com.honoka.player.Utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.honoka.player.Domain.ArtistInfo;
import com.honoka.player.Domain.PlayListInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by 41258 on 2016/11/23.
 */

public class MyPlayListUnit {
    /*private static final Uri ArtistList = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;*/
    private static final Uri ArtistList = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;

    public static List<PlayListInfo> getPlayListInfo(Context context){
        Cursor cursor = context.getContentResolver().query(ArtistList, null, null, null, MediaStore.Audio.Playlists.DEFAULT_SORT_ORDER);
        List<PlayListInfo> playListInfos = new ArrayList<PlayListInfo>();
        for (int i = 0; i < cursor.getCount(); i++){
            cursor.moveToNext();
            PlayListInfo playListInfo=new PlayListInfo();
            long playlistid=cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Playlists._ID));//
            String playlistname = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Playlists.NAME)); //
            String playlistadded = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Playlists.DATE_ADDED));
            playListInfo.setPlaylistid(playlistid);
            playListInfo.setPlaylistname(playlistname);
            playListInfo.setPlaylistadded(playlistadded);
            playListInfos.add(playListInfo);
        }
        return playListInfos;
    }
    public static List<HashMap<String, String>> getPlayListMaps(List<PlayListInfo> playListInfos){
        List<HashMap<String, String>> playlist = new ArrayList<HashMap<String, String>>();
        for (Iterator iterator = playListInfos.iterator(); iterator.hasNext();) {
            PlayListInfo playListInfo = (PlayListInfo) iterator.next();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("Playlistname", playListInfo.getPlaylistname());
            map.put("ArtistId", String.valueOf(playListInfo.getPlaylistid()));
            map.put("number_of_albums",playListInfo.getPlaylistadded());
            playlist.add(map);
        }
        return playlist;
    }
}
