package com.technostore.feature_main_page.search_result

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.technostore.arch.mvi.News
import com.technostore.feature_main_page.databinding.SearchResultBinding
import com.technostore.feature_main_page.search_result.presentation.SearchResultNews
import com.technostore.feature_main_page.search_result.presentation.SearchResultState
import com.technostore.feature_main_page.search_result.presentation.SearchResultViewModel
import com.technostore.navigation.MainNavGraphDirections
import com.technostore.shared_search.business.model.ProductSearchModel
import com.technostore.shared_search.ui.ProductAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchResultFragment : Fragment() {
    private val viewModel by viewModels<SearchResultViewModel>()
    private lateinit var binding: SearchResultBinding
    private val args: SearchResultFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = SearchResultBinding.inflate(inflater)
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
        initViews()
        viewModel.init(args.isPopularity, args.categoryId)
    }

    private fun initViews() {
        setAdapter()
        setOnClickListenerForBackButton()
    }

    private fun setAdapter() {
        val adapter = ProductAdapter(
            onClickAdd = { item -> onClickPlus(item) },
            onClickProduct = { item -> onClickProduct(item) },
            loadMoreDataCallback = { }
        )
        val layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
        binding.productsList.layoutManager = layoutManager
        binding.productsList.adapter = adapter
    }

    private fun onClickPlus(product: ProductSearchModel) {
        viewModel.plusClicked(product)
    }

    private fun onClickProduct(product: ProductSearchModel) {
        viewModel.onProductClicked(product.id)
    }

    private fun setOnClickListenerForBackButton() {
        binding.ivBackButton.setOnClickListener {
            viewModel.onBackButtonClicked()
        }
    }


    private fun render(state: SearchResultState) {
        with(binding) {
            if (state.isLoading) {
                binding.layoutShimmer.slShimmer.isVisible = true
                binding.productsList.isVisible = false
            } else {
                binding.layoutShimmer.slShimmer.isVisible = false
                binding.productsList.isVisible = true
                val adapter = productsList.adapter as ProductAdapter
                adapter.submitList(state.products)
            }
        }
    }


    @SuppressLint("ShowToast")
    private fun showNews(news: News) {
        when (news) {
            SearchResultNews.ShowErrorToast -> {
                Toast.makeText(
                    context,
                    com.technostore.core.R.string.error_toast,
                    Toast.LENGTH_SHORT
                ).show()
            }

            SearchResultNews.NavigateBack -> {
                findNavController().popBackStack()
            }

            is SearchResultNews.OpenProductPage -> {
                val action = MainNavGraphDirections.actionGlobalProductFlow()
                action.productId = news.productId
                findNavController().navigate(action)
            }
        }
    }
}