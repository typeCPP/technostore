package com.technostore.feature_main_page.main_page

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
import com.technostore.feature_main_page.databinding.MainFragmentBinding
import com.technostore.feature_main_page.main_page.presentation.MainNews
import com.technostore.feature_main_page.main_page.presentation.MainState
import com.technostore.feature_main_page.main_page.presentation.MainViewModel
import com.technostore.feature_main_page.main_page.ui.CategoryAdapter
import com.technostore.feature_main_page.main_page.ui.PopularProductAdapter
import com.technostore.navigation.MainNavGraphDirections
import com.technostore.shared_search.R as SharedSearchR
import com.technostore.shared_search.business.model.CategoryWithCheck
import com.technostore.shared_search.business.model.ProductSearchModel
import com.technostore.shared_search.ui.ProductAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainPageFragment : Fragment() {
    private val viewModel by viewModels<MainViewModel>()
    private lateinit var binding: MainFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = MainFragmentBinding.inflate(inflater)
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
        setTextChangedListener()
        if (binding.searchBar.mlSearchBar.currentState != SharedSearchR.id.no_back_button) {
            binding.searchBar.mlSearchBar.transitionToState(SharedSearchR.id.no_back_button)
        }
        setOnClickListenerForBackButton()
        setSearchResultAdapter()
        setOnClickListenerForFilterButton()
        setCategoryProductAdapter()
        setPopularsProductAdapter()
        setOnClickListenerForMorePopularity()
    }

    private fun setOnClickListenerForMorePopularity() {
        binding.popularMore.setOnClickListener {
            viewModel.morePopularityClicked()
        }
    }

    private fun setSearchResultAdapter() {
        val adapter = ProductAdapter(
            onClickAdd = { item -> onClickPlus(item) },
            onClickProduct = { item -> onClickProduct(item) },
            loadMoreDataCallback = { viewModel.loadMoreProducts(binding.searchBar.etSearch.text.toString()) }
        )
        val layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
        binding.productsList.layoutManager = layoutManager
        binding.productsList.adapter = adapter
    }


    private fun setPopularsProductAdapter() {
        val adapter = PopularProductAdapter(
            onClickProduct = { item -> onClickPopularProduct(item) },
        )
        val layoutManager = GridLayoutManager(requireContext(), 1, RecyclerView.HORIZONTAL, false)
        binding.rvPopular.layoutManager = layoutManager
        binding.rvPopular.adapter = adapter
    }

    private fun setCategoryProductAdapter() {
        val adapter = CategoryAdapter(
            onClickTag = { item -> onClickTag(item) }
        )
        val layoutManager = GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false)
        binding.rvCategory.layoutManager = layoutManager
        binding.rvCategory.adapter = adapter
    }

    private fun onClickTag(categoryWithCheck: CategoryWithCheck) {
        viewModel.onClickCategory(categoryWithCheck.category.id)
    }

    private fun setTextChangedListener() {
        binding.searchBar.llSearchBar.setOnClickListener {
        }
        binding.searchBar.etSearch.doOnTextChanged { text, _, _, _ ->
            viewModel.search(text.toString())
            if (text.isNullOrEmpty()) {
                viewModel.textIsEmpty()
                if (binding.searchBar.mlSearchBar.currentState != SharedSearchR.id.no_back_button) {
                    binding.searchBar.mlSearchBar.transitionToState(SharedSearchR.id.no_back_button)
                }
            } else {
                viewModel.onClickSearch()
                if (binding.searchBar.mlSearchBar.currentState != SharedSearchR.id.with_back_button) {
                    binding.searchBar.mlSearchBar.transitionToState(SharedSearchR.id.with_back_button)
                }
            }
        }
    }

    private fun onClickPlus(product: ProductSearchModel) {
        viewModel.plusClicked(product)
    }

    private fun onClickPopularProduct(product: ProductSearchModel) {
        viewModel.onProductClicked(product.id)
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


    private fun render(state: MainState) {
        with(binding) {
            if (state.isLoading) {
                binding.layoutShimmer.slShimmer.isVisible = true
                binding.emptyResult.clMain.isVisible = false
                binding.productsList.isVisible = false
                binding.tvPopular.isVisible = false
                binding.rvPopular.isVisible = false
                binding.tvCategoryTitle.isVisible = false
                binding.rvCategory.isVisible = false
            } else {
                binding.layoutShimmer.slShimmer.isVisible = false
                emptyResult.button.isVisible = false
                emptyResult.description.isVisible = false
                if (state.isMainPage) {
                    binding.tvPopular.isVisible = true
                    binding.rvPopular.isVisible = true
                    binding.tvCategoryTitle.isVisible = true
                    binding.rvCategory.isVisible = true
                    binding.emptyResult.clMain.isVisible = false
                    binding.productsList.isVisible = false
                    val popularProductsAdapter = rvPopular.adapter as PopularProductAdapter
                    popularProductsAdapter.submitList(state.popularProducts)
                    val categoryAdapter = rvCategory.adapter as CategoryAdapter
                    categoryAdapter.submitList(state.categories)
                } else {
                    binding.tvPopular.isVisible = false
                    binding.rvPopular.isVisible = false
                    binding.tvCategoryTitle.isVisible = false
                    binding.rvCategory.isVisible = false
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
                        adapter.submitList(state.productsResult)
                    }
                }
            }
        }
    }


    @SuppressLint("ShowToast")
    private fun showNews(news: News) {
        when (news) {
            MainNews.ShowErrorToast -> {
                Toast.makeText(
                    context,
                    com.technostore.core.R.string.error_toast,
                    Toast.LENGTH_SHORT
                ).show()
            }

            MainNews.NavigateBack -> {
                if (binding.searchBar.mlSearchBar.currentState != SharedSearchR.id.no_back_button) {
                    binding.searchBar.mlSearchBar.transitionToState(SharedSearchR.id.no_back_button)
                }
                binding.searchBar.etSearch.setText("")
            }

            is MainNews.OpenProductPage -> {
                val action = MainNavGraphDirections.actionGlobalProductFlow()
                action.productId = news.productId
                findNavController().navigate(action)
            }

            MainNews.OpenFilter -> {
                val action = MainNavGraphDirections.actionGlobalFilterFlow()
                findNavController().navigate(action)
            }

            is MainNews.OpenResultByCategory -> {
                val action =
                    MainPageFragmentDirections.actionMainPageFragmentToSearchResultFragment()
                action.isPopularity = false
                action.categoryId = news.categoryId
                findNavController().navigate(action)
            }

            MainNews.OpenResultByPopularity -> {
                val action =
                    MainPageFragmentDirections.actionMainPageFragmentToSearchResultFragment()
                action.isPopularity = true
                findNavController().navigate(action)
            }
        }
    }
}