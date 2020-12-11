package com.aditya.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.HashSet;

public class SpeechEngineSettings extends AppCompatActivity {

    SeekBar PitchseekBar;
    SeekBar SpeedseekBar;
    static float pitch;
    static float speed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_engine_settings);
        PitchseekBar = findViewById(R.id.PitchseekBar);
        SpeedseekBar = findViewById(R.id.SpeedseekBar);
        if(pitch<0.1){
            pitch = 0.1f;
        }
        float speed = (float) SpeedseekBar.getProgress()/50;
        if(speed<0.1){
            speed = 0.1f;
        }

    }
}