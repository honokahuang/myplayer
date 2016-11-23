package com.honoka.player.Utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.honoka.player.Domain.ArtistInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by 41258 on 2016/11/15.
 */

public class ArtistListUnit  {

    private static final Uri ArtistList = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;


    public static List<ArtistInfo> getArtistInfo(Context context){
        Cursor cursor = context.getContentResolver().query(ArtistList, null, null, null, MediaStore.Audio.Artists.DEFAULT_SORT_ORDER);
        List<ArtistInfo> artistinfos = new ArrayList<ArtistInfo>();
        for (int i = 0; i < cursor.getCount(); i++){
            cursor.moveToNext();
            ArtistInfo artistInfo=new ArtistInfo();
            long artistId=cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Artists._ID));//艺术家ID
            String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST)); // 艺术家
            long number_of_albums = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS));
            long number_of_tracks = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS));//
            artistInfo.setArtist(artist);
            artistInfo.setArtistId(artistId);
            artistInfo.setNumber_of_albums(number_of_albums);
            artistInfo.setNumber_of_tracks(number_of_tracks);
            artistinfos.add(artistInfo);
        }
        return artistinfos;
    }
    public static List<HashMap<String, String>> getArtistMaps(List<ArtistInfo> artistInfos){
        List<HashMap<String, String>> artistlist = new ArrayList<HashMap<String, String>>();
        for (Iterator iterator = artistInfos.iterator(); iterator.hasNext();) {
            ArtistInfo artistInfo = (ArtistInfo) iterator.next();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("Artist", artistInfo.getArtist());
            map.put("ArtistId", String.valueOf(artistInfo.getArtistId()));
            map.put("number_of_albums",String.valueOf(artistInfo.getNumber_of_albums()));
            map.put("number_of_tracks",String.valueOf(artistInfo.getNumber_of_tracks()));
            artistlist.add(map);
        }
        return artistlist;
    }
}
