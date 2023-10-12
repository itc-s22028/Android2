package jp.ac.it_college.std.s22028.service_sample

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import jp.ac.it_college.std.s22028.service_sample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btPlay.setOnClickListener { startSoundManagerService() }
        binding.btStop.setOnClickListener { stopSoundManagerService() }
    }

    private fun startSoundManagerService() {
        val intent = Intent(this,SoundManageService::class.java)
        startService(intent)
        binding.btPlay.isEnabled = false
        binding.btStop.isEnabled = true
    }
    private fun stopSoundManagerService() {
        val intent = Intent(this, SoundManageService::class.java)
        stopService(intent)
        binding.btPlay.isEnabled = true
        binding.btStop.isEnabled = false
    }

}