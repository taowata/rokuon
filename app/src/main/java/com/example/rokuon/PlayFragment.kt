package com.example.rokuon

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.rokuon.databinding.FragmentPlayBinding
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import java.io.IOException

class PlayFragment : Fragment() {
    private val args: PlayFragmentArgs by navArgs()

    private  lateinit var player: MediaPlayer
    private lateinit var filePath: String
    private lateinit var playerView: PlayerView
    private var mediaPlayer: SimpleExoPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        filePath = args.filePath

        val binding = FragmentPlayBinding.inflate(layoutInflater, container, false)
        val viewModel: PlayViewModel by viewModels()

        playerView = binding.playerView
        initPlayer()

        val playButton = binding.playButton
        viewModel.playingState.observe(viewLifecycleOwner) { playingState ->
            playButton.setOnClickListener {
                if (playingState == PlayingState.PLAYING) stopPlaying() else startPlaying()
                viewModel.onClickPlayButton()
            }
        }
        viewModel.playingTag.observe(viewLifecycleOwner) {
            playButton.text = it
        }

        return binding.root
    }

    private fun startPlaying() {
        player = MediaPlayer()
        player.setDataSource(filePath)
        try {
            player.prepare()
            player.start()
        } catch (e: IOException) {
            Log.e("PlayFragment", "prepare() failed")
        }

    }

    private fun stopPlaying() {
        player.release()
    }

    private fun initPlayer() {
        val context = requireContext()
        mediaPlayer = SimpleExoPlayer.Builder(context).build()
        playerView.player = mediaPlayer
    }


}