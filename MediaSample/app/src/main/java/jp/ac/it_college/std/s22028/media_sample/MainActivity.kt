package jp.ac.it_college.std.s22028.media_sample

import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import jp.ac.it_college.std.s22028.media_sample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var mediaPlayer: MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mediaPlayer = MediaPlayer()
        val mediaFileUri = Uri.parse(
            "android.resource://${packageName}/${R.raw.haihaihai}"
        )
        mediaPlayer?.apply {
            setDataSource(applicationContext, mediaFileUri)
            setOnPreparedListener(::mediaPlayerOnPrepared)
            setOnCompletionListener(::mediaPlayerOnCompletion)
            prepareAsync()
        }
        binding.btPlay.setOnClickListener(::onPlayButtonClick)
        binding.btBack.setOnClickListener(::onBackButtonClick)
        binding.btForward.setOnClickListener(::onForwardButtonClick)
        binding.swLoop.setOnCheckedChangeListener(::onLoopSwitchChange)
    }
    private fun mediaPlayerOnPrepared(mediaPlayer: MediaPlayer) {
        binding.btPlay.isEnabled = true
        binding.btBack.isEnabled = true
        binding.btForward.isEnabled = true

    }
    private fun mediaPlayerOnCompletion(mediaPlayer: MediaPlayer) {
        binding.btPlay.setText(R.string.bt_play_play)

    }
    private fun onPlayButtonClick(view: View) {
        mediaPlayer?.run {
            if(isPlaying) {
                pause()
                binding.btPlay.setText(R.string.bt_play_play)
            }else {
                start()
                binding.btPlay.setText(R.string.bt_play_pause)
            }
        }
    }

    override fun onStop() {
        mediaPlayer?.run {
            if (isPlaying) {
                stop()
            }
            release()
        }
        super.onStop()
    }
    private fun onBackButtonClick(view: View) {
        mediaPlayer?.seekTo(0)
    }
    private fun onForwardButtonClick(view: View) {
        mediaPlayer?.run {
            seekTo(duration)
            if (!isPlaying) {
                binding.btPlay.setText(R.string.bt_play_pause)
                start()
            }
        }
    }
    private fun onLoopSwitchChange(buttonView: CompoundButton, isChecked: Boolean) {
        mediaPlayer?.isLooping = isChecked
    }
}
