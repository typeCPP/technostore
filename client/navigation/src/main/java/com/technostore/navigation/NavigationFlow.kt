package com.technostore.navigation

sealed class NavigationFlow {
    data object OnboardingFlow : NavigationFlow()
    data object LoginFlow : NavigationFlow()
}