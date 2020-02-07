package com.example.artwokmabel.rubbish;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.artwokmabel.R;

public class GroupActivity extends AppCompatActivity {

    Button clk;
    VideoView videoView;
    MediaController mediaC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.z_activity_group);

        clk = (Button) findViewById(R.id.play_video);
        videoView = (VideoView) findViewById(R.id.video_test);
        mediaC = new MediaController(this);
    }

    public void videoplay(View v) {
        String videopath = "android.resource://com.example.artwokmabel/" + R.raw.crab;
        Uri uri = Uri.parse(videopath);
        videoView.setVideoURI(uri);
        videoView.setMediaController(mediaC);
        mediaC.setAnchorView(videoView);
        videoView.start();
    }
}