package com.honoka.player.Domain;

/**
 * Created by 41258 on 2016/11/15.
 */

public class ArtistInfo {
    private String artist; // 艺术家
    private long artistId;
    private long number_of_albums;
    private long number_of_tracks;

    public long getNumber_of_tracks() {
        return number_of_tracks;
    }

    public void setNumber_of_tracks(long number_of_tracks) {
        this.number_of_tracks = number_of_tracks;
    }

    public long getNumber_of_albums() {
        return number_of_albums;
    }

    public void setNumber_of_albums(long number_of_albums) {
        this.number_of_albums = number_of_albums;
    }

    public ArtistInfo(){
        super();
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getArtistId() {
        return artistId;
    }

    public void setArtistId(long artistId) {
        this.artistId = artistId;
    }

    public String toString(){
        return "ArtistInfo[artist="+artist+"artistId="+artistId+"number_of_albums="+number_of_albums+"number_of_tracks="+number_of_tracks+"]";
    }

}
