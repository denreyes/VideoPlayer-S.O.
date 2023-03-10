package com.silverorange.videoplayer.ui.main

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.silverorange.videoplayer.R
import io.noties.markwon.Markwon
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val markwon = Markwon.create(this)

        // Observe LiveDatas
        viewModel.video.observe(this) {
            video_title.text = it.title;
            video_author.text = it.author.name;
            markwon.setMarkdown(video_description, it.description)
        }

        viewModel.fetchVideos()
    }
}