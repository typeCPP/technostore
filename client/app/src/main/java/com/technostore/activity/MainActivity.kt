package com.technostore.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Window.FEATURE_NO_TITLE
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.technostore.R
import com.technostore.databinding.AcitvityMainBinding
import com.technostore.di.App
import com.technostore.feature_login.welcome_page.WelcomePageFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: AcitvityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AcitvityMainBinding.inflate(layoutInflater)
        (applicationContext as App).appComponent.inject(this)
        requestWindowFeature(FEATURE_NO_TITLE)
        setContentView(R.layout.acitvity_main)
        replaceFragment(WelcomePageFragment())
    }

    @SuppressLint("CommitTransaction")
    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
            .addToBackStack(null).commit()
    }
}