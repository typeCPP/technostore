package com.technostore.feature_product.product.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.technostore.core.R
import com.technostore.feature_product.business.model.ReviewModel
import com.technostore.feature_product.databinding.ViewHolderReviewBinding

class ReviewAdapter : ListAdapter<ReviewModel, ReviewAdapter.DataViewHolder>(ReviewDiffCallback()) {
    private lateinit var binding: ViewHolderReviewBinding

    class DataViewHolder(private val binding: ViewHolderReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(review: ReviewModel) {
            binding.tvUserName.text = review.userName
            binding.tvReview.text = review.text
            Glide.with(binding.ivPicture)
                .load(review.photoLink)
                .centerCrop()
                .placeholder(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.icon_default_user
                    )
                )
                .into(binding.ivPicture)
        }

        private val RecyclerView.ViewHolder.context
            get() = this.itemView.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        binding = ViewHolderReviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val item = getItem(position)
        holder.onBind(item)
    }
}

class ReviewDiffCallback : DiffUtil.ItemCallback<ReviewModel>() {
    override fun areItemsTheSame(oldItem: ReviewModel, newItem: ReviewModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ReviewModel, newItem: ReviewModel): Boolean {
        return oldItem == newItem
    }
}
