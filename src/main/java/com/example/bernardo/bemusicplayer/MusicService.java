package com.example.bernardo.bemusicplayer;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import java.util.ArrayList;
import android.content.ContentUris;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.PowerManager;
import android.util.Log;
/**
 * Created by Bernardo on 2/6/2018.
 */

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {
    private final IBinder musicBind = new MusicBinder();
    //media player
    private MediaPlayer player;
    //song list
    private ArrayList<Song> songs;
    //current position
    private int songPosn;
    private String songTitle="";
    private static final int NOTIFY_ID=1;
    public void onCreate(){
        //create the service
        super.onCreate();
        //initialize position
        songPosn=0;
        //create player
        player = new MediaPlayer();
        initMusicPlayer();
    }
    public void initMusicPlayer(){
        //set player properties
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    public void setList(ArrayList<Song> theSongs){
        songs=theSongs;
    }
    //Binder for bind MusicService With another Class(Main Activity)
    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }
    public void setSong(int songIndex){
        songPosn=songIndex;
    }

    public void playSong(){
        //we need to reset the player for each new song.
        player.reset();
        //get song
        Song song = songs.get(songPosn);
        songTitle=song.getTitle();
        //get id
        long currSong = song.getId();
        Log.v("Entrou:" , song.getTitle());
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.INTERNAL_CONTENT_URI,
                currSong);
        try{
            Log.v("Try",trackUri.toString());
            player.setDataSource(getApplicationContext(), trackUri);
        }
        catch(Exception e){
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
        //Prepare the media player to play the song and calls the method onPrepared(the media player is prepared to start)
        player.prepareAsync();
    }
    public int getPosn(){
        return player.getCurrentPosition();
    }

    public int getDur(){
        return player.getDuration();
    }

    public boolean isPng(){
        return player.isPlaying();
    }

    public void pausePlayer(){
        player.pause();
    }

    public void seek(int posn){
        player.seekTo(posn);
    }

    public void go(){
        player.start();
    }
    public void playPrev(){
        songPosn--;
        if(songPosn < 0)
        {
            songPosn=songs.size()-1;
        }
        playSong();
    }
    //skip to next
    public void playNext(){
        songPosn++;
        if(songPosn>=songs.size())
        {
            songPosn=0;
        }
        playSong();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return musicBind;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        //Start media player
        mediaPlayer.start();
    }
    //When the Bind between the Activity and the MusicService no long exits, we need to release the media player resources.
    @Override
    public boolean onUnbind(Intent intent){
        player.stop();
        player.release();
        return false;
    }
    @Override
    public void onDestroy() {
        stopForeground(true);
    }
}
