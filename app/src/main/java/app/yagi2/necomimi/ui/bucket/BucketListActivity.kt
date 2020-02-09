package app.yagi2.necomimi.ui.bucket

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import app.yagi2.necomimi.R
import app.yagi2.necomimi.databinding.ActivityBucketListBinding
import app.yagi2.necomimi.ui.item.ItemListActivity

class BucketListActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(BucketListViewModel::class.java)
    }
    private val adapter = BucketListAdapter() {
        ItemListActivity.start(this, it.name)
    }

    private lateinit var binding: ActivityBucketListBinding

    companion object {
        fun start(context: Context) {
            context.startActivity(createIntent(context))
        }

        private fun createIntent(context: Context): Intent {
            return Intent(context, BucketListActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bucket_list)

        binding.apply {
            buckets.layoutManager = LinearLayoutManager(this@BucketListActivity)
            buckets.addItemDecoration(
                DividerItemDecoration(
                    this@BucketListActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
            buckets.adapter = adapter
        }

        with(viewModel) {
            val owner = this@BucketListActivity

            bucketData.observe(owner, Observer {
                adapter.setBuckets(it)
            })

            progressData.observe(owner, Observer {
                binding.progress.isVisible = it
            })
        }
    }
}