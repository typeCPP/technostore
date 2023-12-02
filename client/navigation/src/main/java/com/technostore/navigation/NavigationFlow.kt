package com.technostore.navigation

sealed class NavigationFlow {
    data object OnboardingFlow : NavigationFlow()
    data object LoginFlow : NavigationFlow()
    data object MainActivityFlow : NavigationFlow()
    data object FilterFlow : NavigationFlow()
    data object ProfileFlow : NavigationFlow()
    data object ShoppingCartFlow : NavigationFlow()
    data class ProductFlow(val productId: Long) : NavigationFlow()
    data class ReviewListFlow(val productId: Long) : NavigationFlow()
    data object SearchFlow : NavigationFlow()
}