package com.technostore.feature_profile.orders.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.technostore.feature_profile.R
import com.technostore.feature_profile.databinding.ViewHolderOrdersBinding
import com.technostore.network.model.order.response.Order

class OrdersAdapter(private val onClickOrder: (item: Order) -> Unit) :
    ListAdapter<Order, OrdersAdapter.DataViewHolder>(OrdersDiffCallback()) {
    private lateinit var binding: ViewHolderOrdersBinding

    class DataViewHolder(private val binding: ViewHolderOrdersBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(order: Order, onClickOrder: (item: Order) -> Unit) {
            binding.orderNumber.text =
                context.getString(R.string.orders_item_title, order.id.toString())
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

class OrdersDiffCallback : DiffUtil.ItemCallback<Order>() {
    override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
        return oldItem.id == newItem.id
    }
}
