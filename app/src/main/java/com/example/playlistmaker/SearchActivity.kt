package com.example.playlistmaker

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.model.Track
import com.example.playlistmaker.remote.ITunesApiService
import com.example.playlistmaker.remote.ITunesResponce
import com.example.playlistmaker.view.SearchHistory
import com.example.playlistmaker.view.StoryAdapter
import com.example.playlistmaker.view.TrackAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

const val TRACK_HISTORY_PREFERENCES = "track_history_preferences"

class SearchActivity : AppCompatActivity() {

    private lateinit var inputEditText: EditText
    private lateinit var textValue: String
    private lateinit var recycler: RecyclerView
    private lateinit var placeholder: ViewGroup
    private lateinit var placeholderImage: ImageView
    private lateinit var placeholderMessage: TextView
    private lateinit var placeholderButton: Button
    private lateinit var iTunesService: ITunesApiService

    private val songs = mutableListOf<Track>()
    private val story = mutableListOf<Track>()
    private lateinit var adapter: TrackAdapter
    private lateinit var storyAdapter: StoryAdapter
    private lateinit var searchHistory: SearchHistory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val url = "https://itunes.apple.com"
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        iTunesService = retrofit.create<ITunesApiService>()

        val sharedPrefs = getSharedPreferences(TRACK_HISTORY_PREFERENCES, MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPrefs)
        story.addAll(searchHistory.readHistory())

        adapter = TrackAdapter(songs, story)
        storyAdapter = StoryAdapter(story)

        recycler = findViewById(R.id.track_recycler_view)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = storyAdapter

        val backBttn = findViewById<ImageView>(R.id.back)
        val backClickListener: View.OnClickListener = View.OnClickListener { finish() }
        backBttn.setOnClickListener(backClickListener)

        val clearButton = findViewById<ImageButton>(R.id.clear_button)
        val clearHistoryBttn = findViewById<Button>(R.id.clear_history)
        val historyHint = findViewById<TextView>(R.id.history_hint)
        inputEditText = findViewById(R.id.searching)
        placeholder = findViewById(R.id.placeholderMessage)
        placeholderImage = findViewById(R.id.placeholderImage)
        placeholderMessage = findViewById(R.id.error_text)
        placeholderButton = findViewById(R.id.refresh)

        clearButton.isVisible = false
        if (story.size != 0) {
            clearHistoryBttn.isVisible = true
            historyHint.isVisible = true
            recycler.isVisible = true
        }

        clearButton.setOnClickListener {
            searchHistory.saveHistory(story)
            inputEditText.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(clearButton.windowToken, 0)
            if (searchHistory.readHistory().isNotEmpty()) {
                story.clear()
                story.addAll(searchHistory.readHistory())
                recycler.adapter = storyAdapter
                clearHistoryBttn.isVisible = true
                historyHint.isVisible = true
                recycler.isVisible = true
                placeholder.isVisible = false
                storyAdapter.notifyDataSetChanged()
            } else recycler.isVisible = false
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //TODO()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    textValue = s.toString()
                }
                clearButton.visibility = clearButtonVisibility(s)
                clearHistoryBttn.isVisible = inputEditText.hasFocus() && inputEditText.text.isEmpty() && searchHistory.readHistory().isNotEmpty()
                historyHint.isVisible = clearHistoryBttn.isVisible
                placeholder.isVisible = false
                recycler.isVisible = clearHistoryBttn.isVisible
                recycler.adapter = storyAdapter
                storyAdapter.notifyDataSetChanged()
            }

            override fun afterTextChanged(s: Editable?) {
                // TODO()
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)
        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (inputEditText.text.isEmpty()) {
                    searchHistory.saveHistory(story)
                    if (searchHistory.readHistory().isNotEmpty()) {
                        story.clear()
                        story.addAll(searchHistory.readHistory())
                        recycler.adapter = storyAdapter
                        clearHistoryBttn.isVisible = true
                        historyHint.isVisible = true
                        recycler.isVisible = true
                        placeholder.isVisible = false
                        storyAdapter.notifyDataSetChanged()
                    } else recycler.isVisible = false
                } else {
                    remoteRequest()
                }
            }
            false
        }
        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            clearHistoryBttn.isVisible = hasFocus && inputEditText.text.isEmpty() && searchHistory.readHistory().size != 0
            historyHint.isVisible = clearHistoryBttn.isVisible
            placeholder.isVisible = false
            recycler.adapter = storyAdapter
            storyAdapter.notifyDataSetChanged()
        }

        placeholderButton.setOnClickListener{
            remoteRequest()
        }

        clearHistoryBttn.setOnClickListener{
            searchHistory.saveHistory(mutableListOf())
            recycler.isVisible = false
            clearHistoryBttn.isVisible = false
            historyHint.isVisible = clearHistoryBttn.isVisible
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_EDIT_TEXT, textValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputEditText.setText(savedInstanceState.getString(SEARCH_EDIT_TEXT, ""))
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun showMessage(text: String, image: Drawable?, additionalMessage: String) {
        if (text.isNotEmpty()) {
            recycler.isVisible = false
            placeholder.isVisible = true
            songs.clear()
            story.clear()
            story.addAll(searchHistory.readHistory())
            recycler.adapter = adapter
            adapter.notifyDataSetChanged()
            placeholderImage.setImageDrawable(image)
            placeholderMessage.text = text
            if (additionalMessage.isNotEmpty()) {
                Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG)
                    .show()
                placeholderButton.isVisible = true
            } else {
                placeholderButton.isVisible = false
            }
        } else {
            placeholder.isVisible = false
            recycler.isVisible = true
        }
    }

    private fun remoteRequest(){
        iTunesService.search(inputEditText.text.toString()).enqueue(object : Callback<ITunesResponce> {
            override fun onResponse(
                call: Call<ITunesResponce>,
                response: Response<ITunesResponce>
            ) {
                if(response.code() == 200) {
                    songs.clear()
                    story.clear()
                    story.addAll(searchHistory.readHistory())
                    if(response.body()?.results?.isNotEmpty() == true) {
                        songs.addAll(response.body()?.results!!)
                        recycler.adapter = adapter
                        adapter.notifyDataSetChanged()
                        showMessage("", null, "")
                    } else {
                        showMessage(getString(R.string.nothing_was_found), getDrawable(R.drawable.res_not_ex), "")
                    }
                } else {
                    showMessage(getString(R.string.no_internet), getDrawable(R.drawable.no_internet), response.code().toString())
                }
            }

            override fun onFailure(call: Call<ITunesResponce>, t: Throwable) {
                showMessage(getString(R.string.no_internet), getDrawable(R.drawable.no_internet), t.message.toString())
            }
        })
    }

    companion object {
        const val SEARCH_EDIT_TEXT = "SearchEditText"
    }
}