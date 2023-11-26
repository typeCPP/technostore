package com.technostore.navigation

import android.util.Log
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
            Log.wtf("Navigator","Navigator")
            navController.navigate(MainNavGraphDirections.actionGlobalProfileFlow())
        }
    }
}