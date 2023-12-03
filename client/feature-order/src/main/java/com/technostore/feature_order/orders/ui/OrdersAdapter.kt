package com.technostore.feature_order.orders.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.technostore.feature_order.R
import com.technostore.feature_order.databinding.ViewHolderOrdersBinding

class OrdersAdapter(private val onClickOrder: (item: Long) -> Unit) :
    ListAdapter<Long, OrdersAdapter.DataViewHolder>(OrdersDiffCallback()) {
    private lateinit var binding: ViewHolderOrdersBinding

    class DataViewHolder(private val binding: ViewHolderOrdersBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(order: Long, onClickOrder: (item: Long) -> Unit) {
            binding.orderNumber.text =
                context.getString(R.string.orders_item_title, order.toString())
            itemView.setOnClickListener {
                onClickOrder(order)
            }
        }

        private val RecyclerView.ViewHolder.context
            get() = this.itemView.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        binding = ViewHolderOrdersBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val item = getItem(position)
        holder.onBind(item, onClickOrder)
    }
}

class OrdersDiffCallback : DiffUtil.ItemCallback<Long>() {
    override fun areItemsTheSame(oldItem: Long, newItem: Long): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Long, newItem: Long): Boolean {
        return oldItem == newItem
    }
}
