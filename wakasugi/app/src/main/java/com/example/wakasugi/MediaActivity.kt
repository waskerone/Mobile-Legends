package com.example.wakasugi

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MediaActivity : AppCompatActivity() {

    private lateinit var btnVideo: Button
    private lateinit var btnAudio: Button
    private lateinit var btnPlay: Button
    private lateinit var btnPause: Button
    private lateinit var btnStop: Button

    private lateinit var videoView: VideoView
    private lateinit var audioStatus: TextView
    private lateinit var seekBar: SeekBar

    private var mediaPlayer: MediaPlayer? = null
    private var isVideoMode = true

    private val handler = Handler(Looper.getMainLooper())
    private val updateSeekBarRunnable = object : Runnable {
        override fun run() {
            updateSeekBar()
            handler.postDelayed(this, 500) // обновляем каждые 0.5 секунды
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnVideo = findViewById(R.id.btnVideo)
        btnAudio = findViewById(R.id.btnAudio)
        btnPlay = findViewById(R.id.btnPlay)
        btnPause = findViewById(R.id.btnPause)
        btnStop = findViewById(R.id.btnStop)

        videoView = findViewById(R.id.videoView)
        audioStatus = findViewById(R.id.audioStatus)
        seekBar = findViewById(R.id.seekBar)

        setMode(true)

        btnVideo.setOnClickListener { setMode(true) }
        btnAudio.setOnClickListener { setMode(false) }

        btnPlay.setOnClickListener { playMedia() }
        btnPause.setOnClickListener { pauseMedia() }
        btnStop.setOnClickListener { stopMedia() }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            var userSeeking = false

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser && userSeeking) {
                    if (isVideoMode) {
                        videoView.seekTo(progress)
                    } else {
                        mediaPlayer?.seekTo(progress)
                    }
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                userSeeking = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                userSeeking = false
            }
        })
    }

    private fun setMode(video: Boolean) {
        isVideoMode = video

        if (video) {
            videoView.visibility = VideoView.VISIBLE
            audioStatus.visibility = TextView.GONE

            mediaPlayer?.release()
            mediaPlayer = null

            val videoUri = Uri.parse("android.resource://${packageName}/${R.raw.video}")
            videoView.setVideoURI(videoUri)
            videoView.seekTo(1)

            videoView.setOnPreparedListener { mp ->
                seekBar.max = mp.duration
                handler.post(updateSeekBarRunnable)
            }

            videoView.setOnCompletionListener {
                handler.removeCallbacks(updateSeekBarRunnable)
                seekBar.progress = 0
            }
        } else {
            videoView.visibility = VideoView.GONE
            audioStatus.visibility = TextView.VISIBLE

            videoView.stopPlayback()

            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(this, R.raw.audio)

            mediaPlayer?.setOnPreparedListener { mp ->
                seekBar.max = mp.duration
                audioStatus.text = "Аудио готово"
                handler.post(updateSeekBarRunnable)
            }

            mediaPlayer?.setOnCompletionListener {
                handler.removeCallbacks(updateSeekBarRunnable)
                seekBar.progress = 0
                audioStatus.text = "Аудио завершено"
            }
        }
    }

    private fun playMedia() {
        if (isVideoMode) {
            videoView.start()
        } else {
            mediaPlayer?.start()
            audioStatus.text = "Аудио играет"
        }
        handler.post(updateSeekBarRunnable)
    }

    private fun pauseMedia() {
        if (isVideoMode) {
            if (videoView.isPlaying) videoView.pause()
        } else {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
                audioStatus.text = "Аудио на паузе"
            }
        }
        handler.removeCallbacks(updateSeekBarRunnable)
    }

    private fun stopMedia() {
        if (isVideoMode) {
            videoView.stopPlayback()
            videoView.setVideoURI(Uri.parse("android.resource://${packageName}/${R.raw.video}"))
            videoView.seekTo(1)
            handler.removeCallbacks(updateSeekBarRunnable)
            seekBar.progress = 0
        } else {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(this, R.raw.audio)
            audioStatus.text = "Аудио остановлено"
            handler.removeCallbacks(updateSeekBarRunnable)
            seekBar.progress = 0
        }
    }

    private fun updateSeekBar() {
        if (isVideoMode) {
            if (videoView.isPlaying) {
                seekBar.progress = videoView.currentPosition
            }
        } else {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    seekBar.progress = it.currentPosition
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateSeekBarRunnable)
        mediaPlayer?.release()
        mediaPlayer = null
    }
}