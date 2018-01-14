package com.saksham_.musicplayer;

import java.io.Serializable;

/**
 * Created by saksham_ on 23-Dec-17.
 */

public class Song implements Serializable{

    private long id;
    private String title;
    private String artist;
    private String path;

    public Song(long songID, String songTitle, String songArtist,String path) {
        id=songID;
        title=songTitle;
        artist=songArtist;
        this.path=path;
    }

    public long getID()
    {
        return id;
    }
    public String getTitle()
    {
        return title;
    }
    public String getArtist()
    {
        return artist;
    }
    public String getPath()
    {
        return path;
    }

}
