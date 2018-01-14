package com.saksham_.musicplayer;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Player extends AppCompatActivity implements View.OnClickListener {

    int totalDuration;
    int currentPosition;
    int position;
    Uri u;
    Thread updateSeekBar;
    static MediaPlayer mp;
    ArrayList<Song> mySongs;
    SeekBar sb;
    Button btPlay, btFF, btFB, btNxt, btPrev,back;
    TextView title,artist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        setTitle("Now Playing");

        back=(Button)findViewById(R.id.backButton);
        title=(TextView)findViewById(R.id.titlesong);
        artist=(TextView)findViewById(R.id.artist);

        btPlay = (Button) findViewById(R.id.btPlay);
        btFF = (Button) findViewById(R.id.btFF);
        btFB = (Button) findViewById(R.id.btFB);
        btNxt = (Button) findViewById(R.id.btNxt);
        btPrev = (Button) findViewById(R.id.btPrev);
        sb = (SeekBar) findViewById(R.id.seekBar);

        btPlay.setOnClickListener(this);
        btFF.setOnClickListener(this);
        btNxt.setOnClickListener(this);
        btPrev.setOnClickListener(this);
        btFB.setOnClickListener(this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        updateSeekBar=new Thread(){
            @Override
            public void run(){
                /*totalDuration =mp.getDuration();
                currentPosition=0;
                sb.setMax(totalDuration);*/
                while(currentPosition<totalDuration){
                    try{

                        sleep(1000);
                        currentPosition=mp.getCurrentPosition();
                        sb.setProgress(currentPosition);

                    } catch(InterruptedException e){
                        e.printStackTrace();
                    }
                    catch(Exception r)
                    {
                        r.printStackTrace();
                    }
                }
            }
        };

        if (mp != null) {
            mp.stop();
            mp.release();
        }

        Intent i = getIntent();
        Bundle b = i.getExtras();

        mySongs = (ArrayList<Song>)(b.getSerializable("songs"));

        position = b.getInt("pos", 0);

        title.setText(mySongs.get(position).getTitle());
        artist.setText(mySongs.get(position).getArtist());

        u = Uri.parse(mySongs.get(position).getPath());

        mp=MediaPlayer.create(getApplicationContext(),u);
        mp.start();
        sb.setProgress(0);
        sb.setMax(mp.getDuration());
        currentPosition=0;
        totalDuration=mp.getDuration();
        updateSeekBar.start();

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(seekBar.getProgress());

            }
        });
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btPlay:
                if (mp.isPlaying()) {
                    btPlay.setText(">");
                    mp.pause();
                } else {
                    mp.start();
                    btPlay.setText("||");
                }
                break;
            case R.id.btFF:
                mp.seekTo(mp.getCurrentPosition() + 5000);
                break;
            case R.id.btFB:
                mp.seekTo(mp.getCurrentPosition() - 5000);
                break;
            case R.id.btNxt:
                mp.stop();
                mp.release();
                position = (position + 1) % mySongs.size();
                u = Uri.parse(mySongs.get(position).getPath());
                title.setText(mySongs.get(position).getTitle());
                artist.setText(mySongs.get(position).getArtist());
                mp = MediaPlayer.create(getApplicationContext(), u);
                mp.start();
                btPlay.setText("||");
                currentPosition=0;
                totalDuration=mp.getDuration();
                sb.setProgress(0);
                sb.setMax(mp.getDuration());
                break;
            case R.id.btPrev:
                mp.stop();
                mp.release();
                position = (position - 1 < 0) ? mySongs.size() - 1 : position - 1;
                u = Uri.parse(mySongs.get(position).getPath());
                title.setText(mySongs.get(position).getTitle());
                artist.setText(mySongs.get(position).getArtist());
                mp = MediaPlayer.create(getApplicationContext(), u);
                mp.start();
                btPlay.setText("||");
                currentPosition=0;
                totalDuration=mp.getDuration();
                sb.setProgress(0);
                sb.setMax(mp.getDuration());
                break;
        }
    }
}