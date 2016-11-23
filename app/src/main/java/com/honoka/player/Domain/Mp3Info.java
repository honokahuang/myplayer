package com.honoka.player.Domain;

/**
 * Created by 41258 on 2016/10/29.
 */
public class Mp3Info {
    private long id; //音乐id
    private String title; // 音乐标题
    private String album; //专辑
    private long albumId;//专辑ID
    private String displayName; //Ï显示的名字
    private String artist; // 艺术家
    private long artistId;
    private long duration; // 时长
    private long size; // 文件大小
    private String url; // 路径
    private String lrcTitle; // 文件路径
    private String lrcSize; // 是否为音乐

    public Mp3Info() {
        super();
    }

    public Mp3Info(long id, String title, String album, long albumId,
                   String displayName, String artist, long artistId, long duration, long size,
                   String url, String lrcTitle, String lrcSize) {
        super();
        this.id = id;
        this.title = title;
        this.album = album;
        this.albumId = albumId;
        this.displayName = displayName;
        this.artist = artist;
        this.artistId=artistId;
        this.duration = duration;
        this.size = size;
        this.url = url;
        this.lrcTitle = lrcTitle;
        this.lrcSize = lrcSize;
    }

    @Override
    public String toString() {
        return "Mp3Info [id=" + id + ", title=" + title + ", album=" + album
                + ", albumId=" + albumId + ", displayName=" + displayName
                + ", artist=" + artist +", artistId=" + artistId + ", duration=" + duration + ", size="
                + size + ", url=" + url + ", lrcTitle=" + lrcTitle
                + ", lrcSize=" + lrcSize + "]";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }



    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getArtistId(){
        return artistId;
    }

    public void setArtistId(long artistId){
        this.artistId=artistId;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLrcTitle() {
        return lrcTitle;
    }

    public void setLrcTitle(String lrcTitle) {
        this.lrcTitle = lrcTitle;
    }

    public String getLrcSize() {
        return lrcSize;
    }

    public void setLrcSize(String lrcSize) {
        this.lrcSize = lrcSize;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }



}
