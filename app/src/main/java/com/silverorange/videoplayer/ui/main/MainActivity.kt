package com.silverorange.videoplayer.ui.main

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSource
import com.silverorange.videoplayer.R
import io.noties.markwon.Markwon
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var player: SimpleExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val markwon = Markwon.create(this)
        // Create a SimpleExoPlayer instance
        player = SimpleExoPlayer.Builder(this).build()
        // Bind the player to the player view
        player_view.player = player

        player.addListener(object : com.google.android.exoplayer2.Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                play_pause_button.setImageResource(if (isPlaying) R.drawable.pause else R.drawable.play)
                player_view.postDelayed({
                    //Do a fade animation when hiding and showing buttons
                    if (isPlaying) {
                        animateButtonVisibility(false, play_pause_button, next_button, prev_button)
                    } else {
                        animateButtonVisibility(true, play_pause_button, next_button, prev_button)
                    }
                }, 2000)
            }
        })

        player_view.videoSurfaceView?.setOnClickListener { view ->
            animateButtonVisibility(true, play_pause_button, next_button, prev_button)
            player_view.postDelayed({
                //Do a fade animation when hiding and showing buttons
                if (player.isPlaying) {
                    animateButtonVisibility(false, play_pause_button, next_button, prev_button)
                } else {
                    animateButtonVisibility(true, 5000, play_pause_button, next_button, prev_button)
                }
            }, 5000)
        }

        next_button.setOnClickListener {
            animateButtonPressed(next_button)
            viewModel.nextVideo()
        }
        prev_button.setOnClickListener {
            animateButtonPressed(prev_button)
            viewModel.prevVideo()
        }
        play_pause_button.setOnClickListener {
            animateButtonPressed(play_pause_button)

            if (player.isPlaying) {
                player.pause()
            } else {
                player.playWhenReady = true
                player.play()
            }
        }

        // Observe LiveDatas
        viewModel.video.observe(this) {
            video_title.text = it.title;
            video_author.text = it.author.name;
            markwon.setMarkdown(video_description, it.description)

            val mediaSource: MediaSource = DefaultMediaSourceFactory(this)
                .createMediaSource(MediaItem.fromUri(Uri.parse(it.fullURL)))

            player.setMediaSource(mediaSource);
            player.prepare();
            player.playWhenReady = true
        }

        viewModel.nextVideoExists.observe(this) {
            next_button.isEnabled = it
            next_button.setImageResource(if (it) R.drawable.next else R.drawable.next_opaque)
        }

        viewModel.prevVideoExists.observe(this) {
            prev_button.isEnabled = it
            prev_button.setImageResource(if (it) R.drawable.previous else R.drawable.previous_opaque)
        }

        viewModel.fetchVideos()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

    fun animateButtonPressed(vararg views: View) {
        for (view in views) {
            view.animate().scaleX(0.5f).scaleY(0.5f).setDuration(100).withEndAction {
                view.animate().scaleX(1f).scaleY(1f).setDuration(100)
            }
        }
    }

    private fun animateButtonVisibility(visible: Boolean, vararg views: View) {
        animateButtonVisibility(visible, 500, *views)
    }

    private fun animateButtonVisibility(visible: Boolean, duration: Long, vararg views: View) {
        for (view in views) {
            if (visible)
                view.visibility =  View.VISIBLE
            view.animate().alpha(if (visible) 1f else 0f).setDuration(duration).withEndAction {
                if (!visible)
                    view.visibility =  View.GONE
            }
        }
    }

}