package com.example.bernardo.bemusicplayer;

/**
 * Created by Bernardo on 2/6/2018.
 */

public class Song {
    private long id;
    private String artist;
    private String title;

    public Song(long SongID, String songTitle, String songArtist){
        this.id = SongID;
        this.artist = songArtist;
        this.title = songTitle;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
