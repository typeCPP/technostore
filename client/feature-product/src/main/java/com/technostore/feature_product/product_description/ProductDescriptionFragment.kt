package com.technostore.feature_product.product_description

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.technostore.arch.mvi.News
import com.technostore.feature_product.databinding.ProductDescriptionFragmentBinding
import com.technostore.feature_product.product_description.presentation.ProductDescriptionNews
import com.technostore.feature_product.product_description.presentation.ProductDescriptionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDescriptionFragment : Fragment() {
    private val viewModel by viewModels<ProductDescriptionViewModel>()
    private lateinit var binding: ProductDescriptionFragmentBinding
    private val ars: ProductDescriptionFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = ProductDescriptionFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.newsQueue.collect(::showNews)
            }
        }
        initViews()
    }

    private fun initViews() {
        binding.tvProductName.text = ars.productName
        binding.tvProductDescription.text = ars.productDescription
        setOnClickListenerForBackButton()
    }

    private fun setOnClickListenerForBackButton() {
        binding.ivBack.setOnClickListener {
            viewModel.onBackClicked()
        }
    }

    private fun showNews(news: News) {
        when (news) {
            ProductDescriptionNews.OpenPreviousPage -> findNavController().popBackStack()
        }
    }
}
