package com.technostore.shared_search.ui

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
import com.technostore.shared_search.business.model.ProductSearchModel
import com.technostore.core.R as CoreR

private const val PAGINATION_OFFSET = 20

class ProductAdapter(
    private val onClickProduct: (item: ProductSearchModel) -> Unit,
    private val onClickAdd: (item: ProductSearchModel) -> Unit,
    private val loadMoreDataCallback: () -> Unit
) : ListAdapter<ProductSearchModel, ProductAdapter.DataViewHolder>(ShoppingCartDiffCallback()) {
    private lateinit var binding: ViewHolderProductCardBinding

    class DataViewHolder(private val binding: ViewHolderProductCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun onBind(
            product: ProductSearchModel,
            onClickProduct: (item: ProductSearchModel) -> Unit,
            onClickAdd: (item: ProductSearchModel) -> Unit,
        ) {
            binding.name.text = product.name
            binding.price.text = context.getString(CoreR.string.price, product.price.toString())
            binding.rating.text = product.rating.toString()
            binding.count.isVisible = false
            binding.icPlus.isVisible = false
            binding.icMinus.isVisible = false
            binding.icClose.isVisible = false
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
            binding.tvButtonConfirm.setOnClickListener {
                onClickAdd.invoke(product)
            }
            binding.ivPicture.setOnClickListener {
                onClickProduct.invoke(product)
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
        if (position > itemCount - PAGINATION_OFFSET) {
            loadMoreDataCallback.invoke()
        }
        val item = getItem(position) ?: return
        holder.onBind(
            item,
            onClickProduct,
            onClickAdd,
        )
    }
}

class ShoppingCartDiffCallback : DiffUtil.ItemCallback<ProductSearchModel>() {
    override fun areItemsTheSame(
        oldItem: ProductSearchModel,
        newItem: ProductSearchModel
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ProductSearchModel,
        newItem: ProductSearchModel
    ): Boolean {
        return oldItem == newItem
    }
}
