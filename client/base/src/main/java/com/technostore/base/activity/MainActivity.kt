package com.technostore.base.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.technostore.base.R
import com.technostore.navigation.BottomNavigatable
import com.technostore.navigation.NavigationFlow
import com.technostore.navigation.Navigator
import com.technostore.navigation.ToFlowNavigatable
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ToFlowNavigatable, BottomNavigatable {

    private val navigator: Navigator = Navigator()

    private var selectedItemId = R.id.home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acitvity_main)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_main) as NavHostFragment
        navigator.navController = navHostFragment.findNavController()
        val navView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.personal_account -> {
                    if (selectedItemId != R.id.personal_account) {
                        navigateToFlow(NavigationFlow.ProfileFlow)
                    }
                    selectedItemId = R.id.personal_account
                }

                R.id.shopping_cart -> {
                    if (selectedItemId != R.id.shopping_cart) {
                        navigateToFlow(NavigationFlow.ShoppingCartFlow)
                    }
                    selectedItemId = R.id.shopping_cart
                }

                R.id.search -> {
                    if (selectedItemId != R.id.search) {
                        navigateToFlow(NavigationFlow.SearchFlow)
                    }
                    selectedItemId = R.id.search
                }

                R.id.home -> {
                    if (selectedItemId != R.id.home) {
                        navigateToFlow(NavigationFlow.MainPageFlow)
                    }
                    selectedItemId = R.id.home
                }

                else -> {}
            }
            return@setOnItemSelectedListener true
        }
        selectedItemId = R.id.home
        navigateToFlow(NavigationFlow.MainPageFlow)
    }

    override fun navigateToFlow(flow: NavigationFlow) {
        navigator.navigateToFlow(flow)
    }

    override fun navigateToAnotherActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun navigateToHome() {
        val navView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        navView.selectedItemId = R.id.home
        selectedItemId = R.id.home
        navigateToFlow(NavigationFlow.MainPageFlow)
    }
}