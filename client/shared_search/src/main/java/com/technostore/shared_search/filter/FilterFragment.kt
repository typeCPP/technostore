package com.technostore.shared_search.filter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.slider.RangeSlider
import com.technostore.arch.mvi.News
import com.technostore.shared_search.business.model.CategoryWithCheck
import com.technostore.core.R as CoreR
import com.technostore.shared_search.databinding.FilterFragmentBinding
import com.technostore.shared_search.filter.presentation.FilterNews
import com.technostore.shared_search.filter.presentation.FilterState
import com.technostore.shared_search.filter.presentation.FilterViewModel
import com.technostore.shared_search.filter.ui.CategoryFilterAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FilterFragment : Fragment() {

    private val viewModel by viewModels<FilterViewModel>()
    private lateinit var binding: FilterFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = FilterFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.newsQueue.collect(::showNews)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect(::render)
            }
        }
        viewModel.init()
        initViews()
    }

    private fun initViews() {
        setAdapterForTagsRecyclerView()
        setSliderCostListener()
        setSliderRatingListener()
        setPopularityClickListener()
        setRatingClickListener()
        setOnClickListenerForNextButton()
        setOnClickListenerForBackButton()
    }

    private fun setSliderCostListener() {
        binding.sliderCost.addOnSliderTouchListener(
            object : RangeSlider.OnSliderTouchListener {
                override fun onStartTrackingTouch(slider: RangeSlider) = Unit

                override fun onStopTrackingTouch(slider: RangeSlider) {
                    viewModel.changeCostBoundaries(
                        minValue = slider.values[0],
                        maxValue = slider.values[1]
                    )
                }
            }
        )
    }

    private fun setSliderRatingListener() {
        binding.sliderRating.addOnSliderTouchListener(
            object : RangeSlider.OnSliderTouchListener {
                override fun onStartTrackingTouch(slider: RangeSlider) = Unit

                override fun onStopTrackingTouch(slider: RangeSlider) {
                    viewModel.changeRatingBoundaries(
                        minValue = slider.values[0],
                        maxValue = slider.values[1]
                    )
                }
            }
        )
    }

    private fun setPopularityClickListener() {
        binding.tvSortPopular.setOnClickListener {
            viewModel.changeIsSortingByPopularity()
        }
    }

    private fun setRatingClickListener() {
        binding.tvSortRating.setOnClickListener {
            viewModel.changeIsSortingByRating()
        }
    }

    private fun setOnClickListenerForNextButton() {
        binding.tvSearch.setOnClickListener {
            viewModel.onNextButtonClicked()
        }
    }

    private fun setOnClickListenerForBackButton() {
        binding.ivBackButton.setOnClickListener {
            viewModel.onBackClicked()
        }
    }

    private fun setAdapterForTagsRecyclerView() {
        val adapter = CategoryFilterAdapter { item -> onClickTag(item) }
        val layoutManager = GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false)
        binding.rvCategory.layoutManager = layoutManager
        binding.rvCategory.adapter = adapter
    }

    private fun onClickTag(categoryWithCheck: CategoryWithCheck) {
        categoryWithCheck.isSelected = !categoryWithCheck.isSelected
        viewModel.onClickCategory(categoryWithCheck)
    }

    private fun render(state: FilterState) {
        binding.slFilter.isVisible = state.isLoading
        setVisibilityForMainContent(!state.isLoading)
        if (state.isLoading) {
            binding.slFilter.startShimmer()
        } else {
            binding.slFilter.stopShimmer()
        }
        val adapter = binding.rvCategory.adapter as CategoryFilterAdapter
        adapter.submitList(state.categories)
        if (state.isSortingByPopularity) {
            binding.tvSortPopular.background = ContextCompat.getDrawable(
                requireContext(),
                CoreR.drawable.selected_category_filter_background
            )
        } else {
            binding.tvSortPopular.background = ContextCompat.getDrawable(
                requireContext(),
                CoreR.drawable.category_filter_background
            )
        }
        if (state.isSortingByRating) {
            binding.tvSortRating.background = ContextCompat.getDrawable(
                requireContext(),
                CoreR.drawable.selected_category_filter_background
            )
        } else {
            binding.tvSortRating.background = ContextCompat.getDrawable(
                requireContext(),
                CoreR.drawable.category_filter_background
            )
        }
        binding.sliderCost.setValues(state.minPrice, state.maxPrice)
        binding.sliderRating.setValues(state.minRating, state.maxRating)
    }

    private fun setVisibilityForMainContent(visibility: Boolean) {
        with(binding) {
            tvSortTitle.isVisible = visibility
            tvSortPopular.isVisible = visibility
            tvSortRating.isVisible = visibility
            tvCategoryTitle.isVisible = visibility
            rvCategory.isVisible = visibility
            tvCost.isVisible = visibility
            sliderCost.isVisible = visibility
            tvRatingTitle.isVisible = visibility
            sliderRating.isVisible = visibility
            tvSearch.isVisible = visibility
        }

    }

    @SuppressLint("ShowToast")
    private fun showNews(news: News) {
        when (news) {
            FilterNews.ShowErrorToast -> Toast.makeText(
                context,
                CoreR.string.error_toast,
                Toast.LENGTH_SHORT
            ).show()

            FilterNews.OpenPreviousPage -> {
                findNavController().popBackStack()
            }

            is FilterNews.ChangeIsSortingByPopularityBackground -> {
                if (news.isSelected) {
                    binding.tvSortPopular.background = ContextCompat.getDrawable(
                        requireContext(),
                        CoreR.drawable.selected_category_filter_background
                    )
                } else {
                    binding.tvSortPopular.background = ContextCompat.getDrawable(
                        requireContext(),
                        CoreR.drawable.category_filter_background
                    )
                }
            }

            is FilterNews.ChangeIsSortingByRatingBackground -> {
                if (news.isSelected) {
                    binding.tvSortRating.background = ContextCompat.getDrawable(
                        requireContext(),
                        CoreR.drawable.selected_category_filter_background
                    )
                } else {
                    binding.tvSortRating.background = ContextCompat.getDrawable(
                        requireContext(),
                        CoreR.drawable.category_filter_background
                    )
                }
            }
        }
    }
}