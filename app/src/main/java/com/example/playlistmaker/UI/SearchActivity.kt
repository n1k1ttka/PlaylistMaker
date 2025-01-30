package com.example.playlistmaker.UI

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Domain.Track
import com.example.playlistmaker.Presentation.STORYSIZE
import com.example.playlistmaker.Domain.Creator
import com.example.playlistmaker.Domain.api.TrackInteractor
import com.example.playlistmaker.Presentation.StoryAdapter
import com.example.playlistmaker.Presentation.TrackAdapter
import com.example.playlistmaker.R

class SearchActivity : AppCompatActivity() {

    private lateinit var trackInteractor: TrackInteractor

    private lateinit var inputEditText: EditText
    private var textValue: String = ""
    private lateinit var recycler: RecyclerView
    private lateinit var placeholder: ViewGroup
    private lateinit var placeholderImage: ImageView
    private lateinit var placeholderMessage: TextView
    private lateinit var placeholderButton: Button

    private val songs = mutableListOf<Track>()
    private val story = mutableListOf<Track>()
    private lateinit var adapter: TrackAdapter
    private lateinit var storyAdapter: StoryAdapter
    private var progressBar: ProgressBar? = null
    private var isClickAllowed = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        trackInteractor = Creator.provideTrackInteractor(this)

        story.addAll(trackInteractor.loadListenedTracks())

        adapter = TrackAdapter(songs) { item ->
            if (clickDebounce()) {
                var inStory = false
                if (story.contains(item)) {
                    story.remove(item)
                    story.add(0, item)
                    inStory = true
                }
                if (!inStory) {
                    if (story.size < STORYSIZE) {
                        story.add(0, item)
                    } else {
                        story.removeAt(9)
                        story.add(0, item)
                    }
                }
                val intent = Intent(this, MediaActivity::class.java)
                intent.putExtra("track", item)
                this.startActivity(intent)
            }
        }
        storyAdapter = StoryAdapter(story) { item ->
            if (clickDebounce()) {
                val intent = Intent(this, MediaActivity::class.java)
                intent.putExtra("track", item)
                this.startActivity(intent)
            }
        }

        recycler = findViewById(R.id.track_recycler_view)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = storyAdapter

        val backBttn = findViewById<ImageView>(R.id.back)
        val backClickListener: View.OnClickListener = View.OnClickListener {
            trackInteractor.saveListenedTracks(story)
            finish()
        }
        backBttn.setOnClickListener(backClickListener)

        val clearButton = findViewById<ImageButton>(R.id.clear_button)
        val clearHistoryBttn = findViewById<Button>(R.id.clear_history)
        val historyHint = findViewById<TextView>(R.id.history_hint)
        inputEditText = findViewById(R.id.searching)
        placeholder = findViewById(R.id.placeholderMessage)
        placeholderImage = findViewById(R.id.placeholderImage)
        placeholderMessage = findViewById(R.id.error_text)
        placeholderButton = findViewById(R.id.refresh)
        progressBar = findViewById(R.id.progressBar)

        clearButton.isVisible = false
        if (story.size != 0) {
            clearHistoryBttn.isVisible = true
            historyHint.isVisible = true
            recycler.isVisible = true
        }

        clearButton.setOnClickListener {
            trackInteractor.saveListenedTracks(story)
            inputEditText.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(clearButton.windowToken, 0)
            if (trackInteractor.loadListenedTracks().isNotEmpty()) {
                story.clear()
                story.addAll(trackInteractor.loadListenedTracks())
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
                    searchDebounce()
                } else handler.removeCallbacks(searchRunnable)
                clearButton.visibility = clearButtonVisibility(s)
                clearHistoryBttn.isVisible = inputEditText.hasFocus() && inputEditText.text.isEmpty() && trackInteractor.loadListenedTracks().isNotEmpty()
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
                    trackInteractor.saveListenedTracks(story)
                    if (trackInteractor.loadListenedTracks().isNotEmpty()) {
                        story.clear()
                        story.addAll(trackInteractor.loadListenedTracks())
                        recycler.adapter = storyAdapter
                        clearHistoryBttn.isVisible = true
                        historyHint.isVisible = true
                        recycler.isVisible = true
                        placeholder.isVisible = false
                        storyAdapter.notifyDataSetChanged()
                    } else recycler.isVisible = false
                } else {
                    searchDebounce()
                }
            }
            false
        }
        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            clearHistoryBttn.isVisible = hasFocus && inputEditText.text.isEmpty() && trackInteractor.loadListenedTracks().size != 0
            historyHint.isVisible = clearHistoryBttn.isVisible
            placeholder.isVisible = false
            recycler.adapter = storyAdapter
            storyAdapter.notifyDataSetChanged()
        }

        placeholderButton.setOnClickListener{
            searchDebounce()
        }

        clearHistoryBttn.setOnClickListener{
            trackInteractor.saveListenedTracks(listOf())
            recycler.isVisible = false
            clearHistoryBttn.isVisible = false
            historyHint.isVisible = clearHistoryBttn.isVisible
        }
    }

    val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { remoteRequest() }

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
            story.addAll(trackInteractor.loadListenedTracks())
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

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, DEBOUNCE_DELAY)
        }
        return current
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, DEBOUNCE_DELAY)
    }

    private fun remoteRequest(){
        progressBar?.isVisible = true
        trackInteractor.loadTracks(inputEditText.text.toString(), object : TrackInteractor.TracksConsumer {
            override fun consume(tracks: List<Track>?) {
                runOnUiThread {
                    progressBar?.isVisible = false
                    if (tracks != null) {
                        songs.clear()
                        if (tracks.size > 0) {
                            songs.addAll(tracks)
                            recycler.adapter = adapter
                            adapter.notifyDataSetChanged()
                            showMessage("", null, "")
                        } else showMessage(getString(R.string.nothing_was_found), getDrawable(R.drawable.res_not_ex), "responce have 0 size")
                    } else showMessage(getString(R.string.no_internet), getDrawable(R.drawable.no_internet), "no responce")
                }
            }

        })
    }

    companion object {
        const val SEARCH_EDIT_TEXT = "SearchEditText"
        private const val DEBOUNCE_DELAY = 2000L
    }
}