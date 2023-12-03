package com.technostore.feature_main_page.main_page.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.technostore.feature_main_page.R
import com.technostore.core.R as CoreR
import com.technostore.feature_main_page.databinding.ViewHolderPopularProductBinding
import com.technostore.shared_search.business.model.ProductSearchModel

class PopularProductAdapter(
    private val onClickProduct: (item: ProductSearchModel) -> Unit,
) : ListAdapter<ProductSearchModel, PopularProductAdapter.DataViewHolder>(ProductDiffCallback()) {
    private lateinit var binding: ViewHolderPopularProductBinding

    class DataViewHolder(private val binding: ViewHolderPopularProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun onBind(
            product: ProductSearchModel,
            onClickProduct: (item: ProductSearchModel) -> Unit,
        ) {
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
            binding.ivPicture.setOnClickListener {
                onClickProduct.invoke(product)
            }
            binding.reviewsCount.text = context.resources.getQuantityString(
                R.plurals.main_page_reviews,
                product.reviewCount,
                product.reviewCount
            )
        }

        private val RecyclerView.ViewHolder.context
            get() = this.itemView.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        binding = ViewHolderPopularProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.onBind(
            item,
            onClickProduct,
        )
    }
}

class ProductDiffCallback : DiffUtil.ItemCallback<ProductSearchModel>() {
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
