package com.example.playlistmaker.UI.main.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.Presentation.utils.isNightMode
import com.example.playlistmaker.R
import com.example.playlistmaker.UI.main.view_model.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding // ViewBinding
    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container_view) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        if (viewModel.loadTheme()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            viewModel.saveTheme(true)
        } else {
            if (isNightMode(this)) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                viewModel.saveTheme(true)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                viewModel.saveTheme(false)
            }
        }
    }

    fun animateBottomNavigationView() {
        binding.bottomNavigationView.visibility = View.GONE
        binding.divider.visibility = View.GONE
    }

    fun checkBottomNavigationView() {
        binding.bottomNavigationView.isVisible = true
        binding.divider.isVisible = true
    }
}