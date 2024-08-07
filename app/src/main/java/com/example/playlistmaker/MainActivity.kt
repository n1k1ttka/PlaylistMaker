package com.example.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val searchButton = findViewById<Button>(R.id.search)
        val searchClickListener: View.OnClickListener = object : View.OnClickListener { override fun onClick(v: View?) {
            Toast.makeText(this@MainActivity, "Усиленно что-то ищем", Toast.LENGTH_SHORT).show()
        } }
        searchButton.setOnClickListener(searchClickListener)

        val mediaButton = findViewById<Button>(R.id.media)
        val mediaClickListener: View.OnClickListener = View.OnClickListener { Toast.makeText(this@MainActivity, "Я слышу музычку вдали", Toast.LENGTH_SHORT).show() }
        mediaButton.setOnClickListener(mediaClickListener)

        val settingButton = findViewById<Button>(R.id.settings)
        val settingClickListener: View.OnClickListener = View.OnClickListener { Toast.makeText(this@MainActivity, "Настроим...", Toast.LENGTH_SHORT).show() }
        settingButton.setOnClickListener(settingClickListener)
    }
}