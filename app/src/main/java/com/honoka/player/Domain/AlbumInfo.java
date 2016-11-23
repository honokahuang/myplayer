package com.honoka.player.Domain;

/**
 * Created by 41258 on 2016/11/17.
 */

public class AlbumInfo {
    private long albumid;
    private long number_of_song;
    private String artist;
    private String albumtitle;

    public long getAlbumid() {
        return albumid;
    }

    public void setAlbumid(long albumid) {
        this.albumid = albumid;
    }

    public long getNumber_of_song() {
        return number_of_song;
    }

    public void setNumber_of_song(long number_of_song) {
        this.number_of_song = number_of_song;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbumtitle() {
        return albumtitle;
    }

    public void setAlbumtitle(String albumtitle) {
        this.albumtitle = albumtitle;
    }

    public AlbumInfo(){
        super();
    }
    @Override
    public String toString() {
        return "AblumInfo[albumid="+albumid+",number_of_song="+number_of_song+",artist="+artist+",albumtitle="+albumtitle+"]";
    }
}
