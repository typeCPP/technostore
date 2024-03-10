package com.technostore.shared_search.filter.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.technostore.core.R as CoreR
import com.technostore.shared_search.business.model.CategoryWithCheck
import com.technostore.shared_search.databinding.ViewHolderCategoryFilterBinding

class CategoryFilterAdapter(private val onClickTag: (item: CategoryWithCheck) -> Unit) :
    ListAdapter<CategoryWithCheck, CategoryFilterAdapter.DataViewHolder>(TagFilterDiffCallback()) {
    private lateinit var binding: ViewHolderCategoryFilterBinding

    class DataViewHolder(private val binding: ViewHolderCategoryFilterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(tagWithCheck: CategoryWithCheck, onClickTag: (item: CategoryWithCheck) -> Unit) {
            binding.tvCategoryName.text = tagWithCheck.category.name
            if (tagWithCheck.isSelected) {
                binding.tvCategoryName.background =
                    ContextCompat.getDrawable(
                        context,
                        CoreR.drawable.selected_category_filter_background
                    )
            } else {
                binding.tvCategoryName.background = ContextCompat.getDrawable(
                    context,
                    CoreR.drawable.category_filter_background
                )
            }
            itemView.setOnClickListener {
                if (tagWithCheck.isSelected) {
                    binding.tvCategoryName.background = ContextCompat.getDrawable(
                        context,
                        CoreR.drawable.category_filter_background
                    )
                } else {
                    binding.tvCategoryName.background = ContextCompat.getDrawable(
                        context,
                        CoreR.drawable.selected_category_filter_background
                    )
                }
                onClickTag(tagWithCheck)
            }
        }

        private val RecyclerView.ViewHolder.context
            get() = this.itemView.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        binding = ViewHolderCategoryFilterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val item = getItem(position)
        holder.onBind(item, onClickTag)
    }
}

class TagFilterDiffCallback : DiffUtil.ItemCallback<CategoryWithCheck>() {
    override fun areItemsTheSame(oldItem: CategoryWithCheck, newItem: CategoryWithCheck): Boolean {
        return oldItem.category.id == newItem.category.id
    }

    override fun areContentsTheSame(
        oldItem: CategoryWithCheck,
        newItem: CategoryWithCheck
    ): Boolean {
        return oldItem == newItem
    }
}
