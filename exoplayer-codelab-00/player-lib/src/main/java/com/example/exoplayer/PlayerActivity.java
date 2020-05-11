/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
* limitations under the License.
 */
package com.example.exoplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;


/**
 * A fullscreen activity to play audio or video streams.
 */
public class PlayerActivity extends AppCompatActivity {

  private PlayerView playerView;
  private SimpleExoPlayer player;

  boolean playWhenReady = true;
  private long playBackPosition;
  private int currentWindow;


  @Override
  protected void onStart() {
    super.onStart();
    if (Util.SDK_INT >= 24) {
      initializePlayer();
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    hideSystemUi();
    if ((Util.SDK_INT < 24 || player==null)) {
      initializePlayer();
    }
  }

  @Override
  protected void onStop() {
    super.onStop();
    if (Util.SDK_INT >= 24) {
      releasePlayer();
    }
  }

  @Override
  protected void onPause() {
    super.onPause();
    if (Util.SDK_INT < 24) {
      releasePlayer();
    }
  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_player);

    playerView = findViewById(R.id.video_view);
  }

  private void hideSystemUi() {
    playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
  }

  private void initializePlayer() {
    player = ExoPlayerFactory.newSimpleInstance(this);
    playerView.setPlayer(player);

    Uri uri = Uri.parse(getString(R.string.media_url_mp4));
    MediaSource mediaSource = buildMediaSource(uri);

    player.setPlayWhenReady(playWhenReady);
    player.seekTo(currentWindow, playBackPosition);
    player.prepare(mediaSource, false, false);
  }

  private MediaSource buildMediaSource(Uri uri) {
    DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, "exoplayer-codelab");
    return new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
  }

  private void releasePlayer() {
    if (player != null) {
      playWhenReady = player.getPlayWhenReady();
      playBackPosition = player.getCurrentPosition();
      currentWindow = player.getCurrentWindowIndex();
      player.release();
      player = null;
    }
  }

}
