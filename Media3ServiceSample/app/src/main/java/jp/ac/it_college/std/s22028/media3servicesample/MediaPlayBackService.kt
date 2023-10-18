package jp.ac.it_college.std.s22028.media3servicesample

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService

//専用のクラス　MediaSessionService を継承する必要がある。
class MediaPlayBackService : MediaSessionService(){
    //クライアント(MediaController) と連携するためのコンポーネント
    private var mediaSerssion: MediaSession?= null
    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo):
            MediaSession? = mediaSerssion

    // ExoPlayer に設定するイベントリスナ
    // inner class で定義せずに　object 式をつかって無名クラスとして作る。
    private val playerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
                //　再生完了
                Player.STATE_ENDED -> this@MediaPlayBackService.stopSelf()
                // バッファリング中(開始?)
                Player.STATE_BUFFERING -> {}
                //　アイドル(待機)状態
                Player.STATE_IDLE -> {}
                //　再生準備完了
                Player.STATE_READY -> {}
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        val player = ExoPlayer.Builder(this).build()

        player.addListener(playerListener)

        mediaSerssion = MediaSession.Builder(this, player).build()
    }

    override fun onDestroy() {
        mediaSerssion?.run {
            // ExoPlayer のリソース解放
            player.release()

            // MediaSession　そのもののリソース解放
            release()
        }
        //　MediaSession　を破棄
        mediaSerssion = null
        super.onDestroy()
    }
}