package com.saksham_.musicplayer;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Song> songList;

    public ArrayList<File> mySongs;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        songList=new ArrayList<Song>();

        lv=(ListView)findViewById(R.id.lvPlaylist);

        String sdpath = null;

        sdpath=Environment.getExternalStorageDirectory().toString();

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);

        BackgroundTask task=new BackgroundTask();

        try {

            mySongs=task.execute(sdpath).get();
            //Log.i("hello","hello");

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Collections.sort(songList, new Comparator<Song>(){
            public int compare(Song a, Song b){
                return a.getTitle().compareTo(b.getTitle());
            }
        });

        SongAdapter songAdt = new SongAdapter(this, songList);
        lv.setAdapter(songAdt);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getApplicationContext(),Player.class).putExtra("pos",i).putExtra("songs",songList));
                //startActivity(new Intent(getApplicationContext(),test.class).putExtra("songs",songList));
            }
        });
    }

    public void getSongList() {

        ContentResolver musicResolver = getContentResolver();

        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
      //  Log.i("hello",musicUri.getPath());
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ARTIST);
            int test=musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.DATA);
            //add songs to list
            do {

               // Log.i("hello",musicCursor.getString(test));
                String path=musicCursor.getString(test);
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                songList.add(new Song(thisId, thisTitle, thisArtist,path));
            }
            while (musicCursor.moveToNext());
        }
    }

    public class BackgroundTask extends AsyncTask<String,Void,ArrayList<File>>
    {

        @Override
        protected ArrayList<File> doInBackground(String... str) {

            getSongList();
            return findSongs(new File(str[0]));
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
         //   loadingDialog= ProgressDialog.show(MainActivity.this,"Please wait...","Fetching Songs",false,false);

        }

        @Override
        protected void onPostExecute(ArrayList<File> files) {
            super.onPostExecute(files);

        }

        public ArrayList<File> findSongs(File root){
            ArrayList<File> al=new ArrayList<File>();
            File[] files=root.listFiles();
            for(File singleFile:files){
                if(singleFile.isDirectory() && !singleFile.isHidden()){
                    al.addAll(findSongs(singleFile));
                }
                else{
                    if(singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")){
                        al.add(singleFile);
                    }
                }
            }
            return al;
        }

    }

    public void toast(String text){
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT);
    }
}
