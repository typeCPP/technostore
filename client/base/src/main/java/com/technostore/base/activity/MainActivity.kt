package com.technostore.base.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.technostore.base.R
import com.technostore.navigation.NavigationFlow
import com.technostore.navigation.Navigator
import com.technostore.navigation.ToFlowNavigatable
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ToFlowNavigatable {

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

                else -> {}
            }
            return@setOnItemSelectedListener true
        }
    }

    override fun navigateToFlow(flow: NavigationFlow) {
        navigator.navigateToFlow(flow)
    }

    override fun navigateToAnotherActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}