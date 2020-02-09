package app.yagi2.necomimi.ui.player

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import app.yagi2.necomimi.R
import app.yagi2.necomimi.databinding.ActivityMediaPlayerBinding
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

//TODO 動画ファイル名をToolbarのタイトルにする
class MediaPlayerActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(MediaPlayerViewModel::class.java)
    }
    private val bucketName by lazy { intent.getStringExtra(EXTRA_BUCKET_NAME) }
    private val fileKey by lazy { intent.getStringExtra(EXTRA_FILE_KEY) }
    private val player by lazy {
        SimpleExoPlayer.Builder(this).build().apply { playWhenReady = true }
    }

    private lateinit var binding: ActivityMediaPlayerBinding

    companion object {
        private const val EXTRA_BUCKET_NAME = "bucket_name"
        private const val EXTRA_FILE_KEY = "file_key"

        fun start(context: Context, bucketName: String, fileKey: String) {
            context.startActivity(createIntent(context, bucketName, fileKey))
        }

        private fun createIntent(context: Context, bucketName: String, fileKey: String): Intent {
            return Intent(context, MediaPlayerActivity::class.java).apply {
                putExtra(EXTRA_BUCKET_NAME, bucketName)
                putExtra(EXTRA_FILE_KEY, fileKey)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_media_player)

        binding.apply {
            playerView.player = player
        }

        with(viewModel) {
            val owner = this@MediaPlayerActivity

            presignedUrlData.observe(owner, Observer {
                playMedia(it.toURI().toString())
            })

            progressData.observe(owner, Observer {
                binding.progress.isVisible = it
            })

            generatePresignedUrl(bucketName, fileKey)
        }
    }

    override fun onDestroy() {
        player.release()
        super.onDestroy()
    }

    private fun playMedia(url: String) {
        val dataSourceFactory = DefaultDataSourceFactory(this, Util.getUserAgent(this, "necomimi"))
        val mediaSource =
            ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(url))

        player.prepare(mediaSource)
    }
}