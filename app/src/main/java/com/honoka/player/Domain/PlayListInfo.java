package com.honoka.player.Domain;

/**
 * Created by 41258 on 2016/11/23.
 */

public class PlayListInfo {
    private long playlistid;
    private String playlistname;
    private String playlistadded;

    public PlayListInfo() {
        super();
    }

    public long getPlaylistid() {
        return playlistid;
    }

    public void setPlaylistid(long playlistid) {
        this.playlistid = playlistid;
    }

    public String getPlaylistname() {
        return playlistname;
    }

    public void setPlaylistname(String playlistname) {
        this.playlistname = playlistname;
    }

    public String getPlaylistadded() {
        return playlistadded;
    }

    public void setPlaylistadded(String playlistadded) {
        this.playlistadded = playlistadded;
    }

    public String toString(){
        return "PlayListInfo[playlistname="+playlistname+"playlistid="+playlistid+"playlistadded="+playlistadded+"]";
    }
}
