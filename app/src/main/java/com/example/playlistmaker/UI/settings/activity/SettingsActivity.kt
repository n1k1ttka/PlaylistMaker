package com.example.playlistmaker.UI.settings.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.UI.search.activity.SearchActivity
import com.example.playlistmaker.UI.settings.view_model.SettingsViewModel
import com.example.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[SettingsViewModel::class]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инициализация ViewBinding
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Использование ViewBinding для доступа к элементам макета
        binding.back.setOnClickListener { finish() }

        binding.shareButton.setOnClickListener {
            startActivity(viewModel.getShareIntent())
        }

        binding.support.setOnClickListener {
            startActivity(viewModel.getSupportIntent())
        }

        binding.contract.setOnClickListener {
            startActivity(viewModel.getContractIntent())
        }

        viewModel.themeState.observe(this) { isDarkTheme ->
            binding.modeSwitch.isChecked = isDarkTheme
            AppCompatDelegate.setDefaultNightMode(
                if (isDarkTheme) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }
            )
        }

        binding.modeSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleTheme(isChecked)
        }
    }

    companion object {
        fun show(context: Context) {
            val intent = Intent(context, SettingsActivity::class.java)
            context.startActivity(intent)
        }
    }
}