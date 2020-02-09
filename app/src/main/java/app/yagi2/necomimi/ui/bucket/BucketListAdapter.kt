package app.yagi2.necomimi.ui.bucket

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.yagi2.necomimi.databinding.ItemBucketBinding
import com.amazonaws.services.s3.model.Bucket

class BucketListAdapter(private val listener: (Bucket) -> Unit) :
    RecyclerView.Adapter<BucketListAdapter.BucketListViewHolder>() {

    private var buckets: List<Bucket> = listOf()

    override fun getItemCount() = buckets.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BucketListViewHolder {
        return BucketListViewHolder.create(parent, listener)
    }

    override fun onBindViewHolder(holder: BucketListViewHolder, position: Int) {
        holder.bind(buckets[position])
    }

    fun setBuckets(buckets: List<Bucket>) {
        this.buckets = buckets
        notifyDataSetChanged()
    }

    class BucketListViewHolder(
        private val binding: ItemBucketBinding,
        private val listener: (Bucket) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun create(parent: ViewGroup, listener: (Bucket) -> Unit): BucketListViewHolder {
                val binding =
                    ItemBucketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return BucketListViewHolder(binding, listener)
            }
        }

        fun bind(item: Bucket) {
            binding.bucketName.text = item.name
            binding.root.setOnClickListener {
                listener.invoke(item)
            }
        }
    }
}