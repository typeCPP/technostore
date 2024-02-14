package com.technostore.feature_shopping_cart.shopping_cart.ui

import android.annotation.SuppressLint
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
import com.technostore.feature_shopping_cart.business.model.ProductOrderModel
import com.technostore.core.R as CoreR

private const val MAX_PRODUCT_COUNT = 99
private const val MIN_PRODUCT_COUNT = 1

class ShoppingCartAdapter(
    private val onClickPlus: (item: ProductOrderModel) -> Unit,
    private val onClickMinus: (item: ProductOrderModel) -> Unit,
    private val onClickRemove: (item: ProductOrderModel) -> Unit,
) :
    ListAdapter<ProductOrderModel, ShoppingCartAdapter.DataViewHolder>(ShoppingCartDiffCallback()) {
    private lateinit var binding: ViewHolderProductCardBinding

    class DataViewHolder(private val binding: ViewHolderProductCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun onBind(
            product: ProductOrderModel,
            onClickPlus: (item: ProductOrderModel) -> Unit,
            onClickMinus: (item: ProductOrderModel) -> Unit,
            onClickRemove: (item: ProductOrderModel) -> Unit
        ) {
            binding.tvButtonConfirm.isVisible = false
            binding.name.text = product.name
            binding.price.text = context.getString(CoreR.string.price, product.price.toString())
            binding.rating.text = product.rating.toString()
            binding.count.text = product.count.toString()
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
            binding.icMinus.setOnClickListener {
                val count = binding.count.text.toString().toInt()
                if (count == MIN_PRODUCT_COUNT) {
                    onClickRemove(product)
                } else {
                    onClickMinus(product)
                    binding.count.text = (count - 1).toString()
                }
            }
            binding.icPlus.setOnClickListener {
                val count = binding.count.text.toString().toInt()
                if (count < MAX_PRODUCT_COUNT) {
                    onClickPlus(product)
                    binding.count.text = (count + 1).toString()
                }
            }
            binding.icClose.setOnClickListener {
                onClickRemove(product)
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
        holder.onBind(
            item,
            onClickPlus,
            onClickMinus,
            onClickRemove,
        )
    }
}

class ShoppingCartDiffCallback : DiffUtil.ItemCallback<ProductOrderModel>() {
    override fun areItemsTheSame(oldItem: ProductOrderModel, newItem: ProductOrderModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ProductOrderModel,
        newItem: ProductOrderModel
    ): Boolean {
        return oldItem == newItem
    }
}
