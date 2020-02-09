package app.yagi2.necomimi.ui.player

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import app.yagi2.necomimi.R
import app.yagi2.necomimi.databinding.ActivityMediaPlayerBinding

//TODO 動画ファイル名をToolbarのタイトルにする
class MediaPlayerActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(MediaPlayerViewModel::class.java)
    }
    private val bucketName by lazy { intent.getStringExtra(EXTRA_BUCKET_NAME) }
    private val fileKey by lazy { intent.getStringExtra(EXTRA_FILE_KEY) }

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

        with(viewModel) {
            val owner = this@MediaPlayerActivity

            presignedUrlData.observe(owner, Observer {
                //TODO implement
                Toast.makeText(owner, it.toURI().toString(), Toast.LENGTH_LONG).show()
            })

            progressData.observe(owner, Observer {
                binding.progress.isVisible = it
            })

            generatePresignedUrl(bucketName, fileKey)
        }
    }
}