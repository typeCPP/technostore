package com.technostore.review_list.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.technostore.business.model.ReviewModel
import com.technostore.core.R as CoreR
import com.technostore.feature_review.databinding.ViewHolderExtendedReviewBinding

class ReviewListAdapter(
    private val onClickReview: (item: ReviewModel) -> Unit
) : ListAdapter<ReviewModel, ReviewListAdapter.DataViewHolder>(ExtendedReviewDiffCallback()) {

    private lateinit var binding: ViewHolderExtendedReviewBinding

    class DataViewHolder(private val binding: ViewHolderExtendedReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(review: ReviewModel, onClickReview: (item: ReviewModel) -> Unit) {
            with(binding) {
                Glide.with(context)
                    .load(review.photoLink)
                    .into(binding.ivUserPhoto)
                tvUserName.text = review.userName
                tvReviewText.text = review.text
                tvDate.text = review.date
                tvRating.text = review.rate.toString()
                when (review.rate) {
                    in 1..3 -> llRating.background =
                        ContextCompat.getDrawable(
                            context,
                            CoreR.drawable.negative_rating_background
                        )

                    in 4..6 -> llRating.background =
                        ContextCompat.getDrawable(context, CoreR.drawable.neural_rating_background)

                    in 7..10 -> llRating.background =
                        ContextCompat.getDrawable(
                            context,
                            CoreR.drawable.positive_rating_background
                        )

                    else -> {}
                }
            }
            itemView.setOnClickListener {
                onClickReview(review)
            }
        }

        private val RecyclerView.ViewHolder.context
            get() = this.itemView.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        binding = ViewHolderExtendedReviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.animation =
            AnimationUtils.loadAnimation(holder.itemView.context, CoreR.anim.fade_in)
        holder.onBind(item, onClickReview)
    }
}


class ExtendedReviewDiffCallback : DiffUtil.ItemCallback<ReviewModel>() {
    override fun areItemsTheSame(oldItem: ReviewModel, newItem: ReviewModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ReviewModel, newItem: ReviewModel): Boolean {
        return oldItem == newItem
    }
}