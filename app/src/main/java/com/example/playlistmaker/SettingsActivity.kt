package com.example.playlistmaker

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

const val NIGHT_MODE_PREFERENCES = "night_mode_preferences"
const val DAYNIGHT_SWITCHER_KEY = "key_for_daynight_switcher"

class SettingsActivity : AppCompatActivity() {

    private var darkTheme = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backBttn = findViewById<ImageView>(R.id.back)
        val backClickListener: View.OnClickListener = View.OnClickListener { finish() }
        backBttn.setOnClickListener(backClickListener)

        val share = findViewById<ImageView>(R.id.share_button)
        val shareClickListener: View.OnClickListener = View.OnClickListener {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getString(R.string.course_url))
                type = "text/plain"
            }
            startActivity(shareIntent)
        }
        share.setOnClickListener(shareClickListener)

        val support = findViewById<ImageView>(R.id.support)
        val supportClickListener: View.OnClickListener = View.OnClickListener {
            val sendMailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:${getString(R.string.my_mail)}")
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.for_developer))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.thanks_developers))
            }
            startActivity(sendMailIntent)
        }
        support.setOnClickListener(supportClickListener)

        val userContract = findViewById<ImageView>(R.id.contract)
        userContract.setOnClickListener {
            val uri = Uri.parse(getString(R.string.ya_offer))
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        }

        val modeSwitch = findViewById<SwitchCompat>(R.id.mode_switch)
        val sharedPrefs = getSharedPreferences(NIGHT_MODE_PREFERENCES, MODE_PRIVATE)
        modeSwitch.isChecked = sharedPrefs.getBoolean(DAYNIGHT_SWITCHER_KEY, false)

        modeSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }
            )
            sharedPrefs.edit()
                .putBoolean(DAYNIGHT_SWITCHER_KEY, isChecked)
                .apply()
        }
    }
}