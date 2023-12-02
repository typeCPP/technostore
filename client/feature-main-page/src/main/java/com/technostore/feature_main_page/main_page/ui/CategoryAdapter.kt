package com.technostore.feature_main_page.main_page.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.technostore.feature_main_page.databinding.ViewHolderCategoryBinding
import com.technostore.shared_search.business.model.CategoryWithCheck

class CategoryAdapter(private val onClickTag: (item: CategoryWithCheck) -> Unit) :
    ListAdapter<CategoryWithCheck, CategoryAdapter.DataViewHolder>(CategoryDiffCallback()) {
    private lateinit var binding: ViewHolderCategoryBinding

    class DataViewHolder(private val binding: ViewHolderCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(tagWithCheck: CategoryWithCheck, onClickTag: (item: CategoryWithCheck) -> Unit) {
            binding.tvName.text = tagWithCheck.category.name
            itemView.setOnClickListener {
                onClickTag(tagWithCheck)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        binding = ViewHolderCategoryBinding.inflate(
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

class CategoryDiffCallback : DiffUtil.ItemCallback<CategoryWithCheck>() {
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
