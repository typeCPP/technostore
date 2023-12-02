package com.technostore.navigation

import androidx.navigation.NavController

class Navigator {
    lateinit var navController: NavController

    fun navigateToFlow(navigationFlow: NavigationFlow) = when (navigationFlow) {
        NavigationFlow.LoginFlow -> {
            navController.navigate(LoginNavGraphDirections.actionGlobalLoginFlow())
        }

        NavigationFlow.OnboardingFlow -> {
            navController.navigate(LoginNavGraphDirections.actionGlobalOnboardingFlow())
        }

        NavigationFlow.MainActivityFlow -> {
            navController.navigate(LoginNavGraphDirections.actionGlobalMainFlow())
        }

        NavigationFlow.FilterFlow -> {
            navController.navigate(MainNavGraphDirections.actionGlobalFilterFlow())
        }

        NavigationFlow.ProfileFlow -> {
            navController.navigate(MainNavGraphDirections.actionGlobalProfileFlow())
        }

        NavigationFlow.ShoppingCartFlow -> {
            navController.navigate(MainNavGraphDirections.actionGlobalShoppingCartFlow())
        }

        is NavigationFlow.ProductFlow -> {
            val action = MainNavGraphDirections.actionGlobalProductFlow()
            action.productId = navigationFlow.productId
            navController.navigate(action)
        }

        is NavigationFlow.ReviewListFlow -> {
            val action = MainNavGraphDirections.actionGlobalReviewFlow()
            action.productId = navigationFlow.productId
            navController.navigate(action)
        }

        NavigationFlow.SearchFlow -> {
            navController.navigate(MainNavGraphDirections.actionGlobalSearchFlow())
        }
    }
}