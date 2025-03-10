package com.example.playlistmaker.UI.main.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.Presentation.utils.isNightMode
import com.example.playlistmaker.UI.main.view_model.MainViewModel
import com.example.playlistmaker.UI.media.activity.MediaPlayerActivity
import com.example.playlistmaker.UI.search.activity.SearchActivity
import com.example.playlistmaker.UI.settings.activity.SettingsActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding // ViewBinding
    private val viewModel by lazy {
        ViewModelProvider(this,
            MainViewModel.getViewModelFactory(Creator.provideThemeInteractor(this))
        )[MainViewModel::class]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инициализация ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()

        // Обработка системных окон (insets)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Установка темы
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

        // Обработка нажатий на кнопки
        binding.search.setOnClickListener {
            SearchActivity.show(this)
        }

        binding.media.setOnClickListener {
            MediaPlayerActivity.show(this)
        }

        binding.settings.setOnClickListener {
            SettingsActivity.show(this)
        }
    }
}