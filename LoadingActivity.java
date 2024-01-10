package com.ved.Y1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.MediaController;

import androidx.appcompat.app.AppCompatActivity;

public class LoadingActivity extends AppCompatActivity {

    private VideoView videoView;
    private TextView tipsTextView;
    private TextView tapToContinueText;
    private boolean isTextVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        videoView = findViewById(R.id.VideoView);
        tipsTextView = findViewById(R.id.TipsMessage);
        tapToContinueText = findViewById(R.id.TapToContinueText);

        // Set media controller for video playback controls (optional)
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        // Disable default media controller
        videoView.setMediaController(null);

        // Set video file path (adjust the path based on your video file's location)
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.jinxieeee);
        videoView.setVideoURI(videoUri);

        // Start playing the video
        videoView.start();

        // Set on completion listener to loop the video
        videoView.setOnCompletionListener(mp -> {
            // Restart video playback when it finishes
            videoView.start();
        });

        // Delay showing the "Tap to Continue" text after 5 seconds
        new Handler().postDelayed(() -> {
            tapToContinueText.setVisibility(View.VISIBLE);
            // Start blinking the text
            blinkText();
        }, 5000);

        // Set onClickListener for the entire layout to handle taps
        findViewById(R.id.loadingLayout).setOnClickListener(v -> {
            // Open the main screen activity
            startActivity(new Intent(LoadingActivity.this, MyScreen.class));
            finish();
        });
    }

    // Method to make the "Tap to Continue" text blink
    private void blinkText() {
        new Handler().postDelayed(() -> {
            if (isTextVisible) {
                tapToContinueText.setVisibility(View.INVISIBLE);
                isTextVisible = false;
            } else {
                tapToContinueText.setVisibility(View.VISIBLE);
                isTextVisible = true;
            }
            blinkText();
        }, 500);
    }
}
