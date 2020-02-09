package app.yagi2.necomimi.ui.item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.yagi2.necomimi.databinding.ItemS3ItemBinding
import com.amazonaws.services.s3.model.S3ObjectSummary

class ItemListAdapter(private val listener: (S3ObjectSummary) -> Unit) :
    RecyclerView.Adapter<ItemListAdapter.ItemViewHolder>() {

    private var items: List<S3ObjectSummary> = listOf()

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder.create(parent, listener)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setItems(items: List<S3ObjectSummary>) {
        this.items = items
        notifyDataSetChanged()
    }

    class ItemViewHolder(
        private val binding: ItemS3ItemBinding,
        private val listener: (S3ObjectSummary) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun create(parent: ViewGroup, listener: (S3ObjectSummary) -> Unit): ItemViewHolder {
                val binding =
                    ItemS3ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ItemViewHolder(binding, listener)
            }
        }

        fun bind(item: S3ObjectSummary) {
            binding.itemName.text = item.key
            binding.root.setOnClickListener {
                listener.invoke(item)
            }
        }
    }
}