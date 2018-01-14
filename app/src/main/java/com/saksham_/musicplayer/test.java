package com.saksham_.musicplayer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Intent i;
        ArrayList<Song> arr=(ArrayList<Song>)(getIntent().getSerializableExtra("songs"));
        Log.i("hello",arr.get(5).getTitle());

    }
}
