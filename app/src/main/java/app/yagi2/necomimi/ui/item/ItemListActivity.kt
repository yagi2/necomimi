package app.yagi2.necomimi.ui.item

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import app.yagi2.necomimi.R
import app.yagi2.necomimi.databinding.ActivityItemListBinding

//TODO 選んでいるバケットの名前をToolbarのタイトルにする
class ItemListActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ItemListViewModel::class.java)
    }

    private val bucketName by lazy { intent.getStringExtra(EXTRA_BUCKET_NAME) }
    private val adapter = ItemListAdapter() {
        //TODO 画面遷移
        Toast.makeText(this, it.key, Toast.LENGTH_LONG).show()
    }

    private lateinit var binding: ActivityItemListBinding

    companion object {
        private const val EXTRA_BUCKET_NAME = "bucket_name"

        fun start(context: Context, bucketName: String) {
            context.startActivity(createIntent(context, bucketName))
        }

        private fun createIntent(context: Context, bucketName: String): Intent {
            return Intent(context, ItemListActivity::class.java).apply {
                putExtra(EXTRA_BUCKET_NAME, bucketName)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_item_list)

        binding.apply {
            items.layoutManager = LinearLayoutManager(this@ItemListActivity)
            items.addItemDecoration(
                DividerItemDecoration(
                    this@ItemListActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
            items.adapter = adapter
        }

        with(viewModel) {
            val owner = this@ItemListActivity

            fileItemData.observe(owner, Observer {
                adapter.setItems(it)
            })

            progressData.observe(owner, Observer {
                binding.progress.isVisible = it
            })
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadItems(bucketName)
    }
}