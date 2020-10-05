package com.example.rokuon

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.rokuon.databinding.FragmentPlayBinding
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView

class PlayFragment : Fragment() {
    private val args: PlayFragmentArgs by navArgs()

    private lateinit var filePath: String

    // ExoPlayer周りの値
    private lateinit var playerView: PlayerView
    private var mediaPlayer: SimpleExoPlayer? = null
    private var playWhenReady: Boolean = true
    private var playbackPosition: Long = 0L
    private var currentWindow: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        filePath = args.filePath
        val binding = FragmentPlayBinding.inflate(layoutInflater, container, false)

        // playerの初期化
        playerView = binding.playerView
        initPlayer()

        return binding.root
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    private fun buildMediaItem(): MediaItem {
        val uri = Uri.parse(filePath)
        return MediaItem.fromUri(uri)
    }

    private fun releasePlayer() {
        mediaPlayer?.let {
            playWhenReady = it.playWhenReady
            playbackPosition = it.currentPosition
            currentWindow = it.currentWindowIndex
            it.release()
            mediaPlayer = null
        }
    }

    private fun initPlayer() {
        val context = requireContext()
        val mediaSource = buildMediaItem()
        mediaPlayer = SimpleExoPlayer.Builder(context).build().apply {
            playWhenReady = playWhenReady
            seekTo(currentWindow, playbackPosition)
            setMediaItem(mediaSource)
            prepare()
        }
        playerView.player = mediaPlayer
    }
}