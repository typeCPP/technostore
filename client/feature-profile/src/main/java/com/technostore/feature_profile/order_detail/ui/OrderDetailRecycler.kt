package com.technostore.feature_profile.order_detail.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.technostore.core.databinding.ViewHolderProductCardBinding
import com.technostore.feature_profile.business.model.ProductOrderModel
import com.technostore.core.R as CoreR

class OrderDetailAdapter(private val onClickProduct: (item: ProductOrderModel) -> Unit) :
    ListAdapter<ProductOrderModel, OrderDetailAdapter.DataViewHolder>(ProductOrderDiffCallback()) {
    private lateinit var binding: ViewHolderProductCardBinding

    class DataViewHolder(private val binding: ViewHolderProductCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(product: ProductOrderModel, onClickProduct: (item: ProductOrderModel) -> Unit) {
            binding.icPlus.isVisible = false
            binding.icMinus.isVisible = false
            binding.icClose.isVisible = false
            binding.count.isVisible = false
            binding.tvButtonConfirm.isVisible = false
            binding.name.text = product.name
            binding.price.text = context.getString(CoreR.string.price, product.price.toString())
            binding.rating.text = product.rating.toString()
            Glide.with(binding.ivPicture)
                .load(product.photoLink)
                .centerCrop()
                .placeholder(
                    ContextCompat.getDrawable(
                        context,
                        CoreR.drawable.icon_default_product
                    )
                )
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.ivPicture)
            itemView.setOnClickListener {
                onClickProduct(product)
            }
        }

        private val RecyclerView.ViewHolder.context
            get() = this.itemView.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        binding = ViewHolderProductCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val item = getItem(position)
        holder.onBind(item, onClickProduct)
    }
}

class ProductOrderDiffCallback : DiffUtil.ItemCallback<ProductOrderModel>() {
    override fun areItemsTheSame(oldItem: ProductOrderModel, newItem: ProductOrderModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ProductOrderModel,
        newItem: ProductOrderModel
    ): Boolean {
        return oldItem.id == newItem.id
    }
}
