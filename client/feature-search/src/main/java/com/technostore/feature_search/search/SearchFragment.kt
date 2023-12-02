package com.technostore.feature_search.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.technostore.arch.mvi.News
import com.technostore.core.R as CoreR
import com.technostore.feature_search.databinding.SearchFragmentBinding
import com.technostore.feature_search.search.presentation.SearchNews
import com.technostore.feature_search.search.presentation.SearchState
import com.technostore.feature_search.search.presentation.SearchViewModel
import com.technostore.navigation.MainNavGraphDirections
import com.technostore.shared_search.business.model.ProductSearchModel
import com.technostore.shared_search.ui.ProductAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.technostore.shared_search.R as SharedSearchR

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private val viewModel by viewModels<SearchViewModel>()
    private lateinit var binding: SearchFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = SearchFragmentBinding.inflate(inflater)
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
    }

    private fun initViews() {
        if (binding.searchBar.mlSearchBar.currentState != SharedSearchR.id.no_back_button) {
            binding.searchBar.mlSearchBar.transitionToState(SharedSearchR.id.no_back_button)
        }
        setOnClickListenerForBackButton()
        setAdapter()
        setTextChangedListener()
        setOnClickListenerForFilterButton()
    }

    private fun setAdapter() {
        val adapter = ProductAdapter(
            onClickAdd = { item -> onClickPlus(item) },
            onClickProduct = { item -> onClickProduct(item) },
            loadMoreDataCallback = { viewModel.loadMoreProducts(binding.searchBar.etSearch.text.toString()) }
        )
        val layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
        binding.productsList.layoutManager = layoutManager
        binding.productsList.adapter = adapter
    }

    private fun setTextChangedListener() {
        binding.searchBar.etSearch.doOnTextChanged { text, start, before, count ->
            viewModel.search(text.toString())
            if (!text.isNullOrEmpty()) {
                if (binding.searchBar.mlSearchBar.currentState != SharedSearchR.id.with_back_button) {
                    binding.searchBar.mlSearchBar.transitionToState(SharedSearchR.id.with_back_button)
                }
            } else {
                if (binding.searchBar.mlSearchBar.currentState != SharedSearchR.id.no_back_button) {
                    binding.searchBar.mlSearchBar.transitionToState(SharedSearchR.id.no_back_button)
                }
            }
        }
    }

    private fun onClickPlus(product: ProductSearchModel) {
        viewModel.plusClicked(product)
    }

    private fun onClickProduct(product: ProductSearchModel) {
        viewModel.onProductClicked(product.id)
    }

    private fun setOnClickListenerForBackButton() {
        binding.searchBar.ivBackButton.setOnClickListener {
            viewModel.onBackButtonClicked()
        }
    }

    private fun setOnClickListenerForFilterButton() {
        binding.searchBar.ivFilterButton.setOnClickListener {
            viewModel.onFilterClicked()
        }
    }


    private fun render(state: SearchState) {
        with(binding) {
            if (state.isLoading) {
                binding.layoutShimmer.slShimmer.isVisible = true
                binding.emptyResult.clMain.isVisible = false
                binding.productsList.isVisible = false
            } else {
                binding.layoutShimmer.slShimmer.isVisible = false
                emptyResult.button.isVisible = false
                emptyResult.description.isVisible = false
                if (state.isEmpty) {
                    binding.emptyResult.clMain.isVisible = true
                    binding.emptyResult.title.isVisible = true
                    binding.emptyResult.title.text =
                        getString(SharedSearchR.string.search_not_found)
                    binding.productsList.isVisible = false
                } else {
                    binding.emptyResult.clMain.isVisible = false
                    binding.productsList.isVisible = true
                    val adapter = productsList.adapter as ProductAdapter
                    adapter.submitList(state.products)
                }
            }
        }
    }


    @SuppressLint("ShowToast")
    private fun showNews(news: News) {
        when (news) {
            SearchNews.ShowErrorToast -> {
                Toast.makeText(
                    context,
                    CoreR.string.error_toast,
                    Toast.LENGTH_SHORT
                ).show()
            }

            SearchNews.NavigateBack -> {
                binding.searchBar.etSearch.setText("")
            }

            is SearchNews.OpenProductPage -> {
                val action = MainNavGraphDirections.actionGlobalProductFlow()
                action.productId = news.productId
                findNavController().navigate(action)
            }

            SearchNews.OpenFilter -> {
                val action = MainNavGraphDirections.actionGlobalFilterFlow()
                findNavController().navigate(action)
            }
        }
    }
}