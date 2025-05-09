package com.example.playlistmaker.UI.search.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.Domain.Track
import com.example.playlistmaker.Presentation.model.search.StoryAdapter
import com.example.playlistmaker.Presentation.model.search.TrackAdapter
import com.example.playlistmaker.Presentation.state.TrackListState
import com.example.playlistmaker.R
import com.example.playlistmaker.UI.player.activity.MediaActivity
import com.example.playlistmaker.UI.search.view_model.SearchViewModel
import com.example.playlistmaker.databinding.ActivitySearchBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private val viewModel by viewModel<SearchViewModel>()

    private lateinit var adapter: TrackAdapter
    private lateinit var storyAdapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        adapter = TrackAdapter(emptyList(), viewModel::onTrackClick)
        storyAdapter = StoryAdapter(emptyList(), viewModel::triggerEvent)

        binding.trackRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.trackRecyclerView.adapter = storyAdapter

        viewModel.getState().observe(this) { state ->
            when (state) {
                is TrackListState.Loading -> showLoading()
                is TrackListState.Content -> showContent(state.tracks)
                is TrackListState.Story -> showStory(state.story)
                is TrackListState.ZeroContent -> showNothingWasFound()
                is TrackListState.Error -> showError(state.errorMessage)
            }
        }

        binding.back.setOnClickListener {
            viewModel.saveListenedTracks()
            finish()
        }

        binding.searching.addTextChangedListener(
            onTextChanged = { text: CharSequence?, start: Int, before: Int, count: Int ->
                viewModel.cancelSearch()
                binding.clearButton.visibility = clearButtonVisibility(text)
                viewModel.searchDebounce(text.toString())
            }
        )

        binding.clearButton.setOnClickListener {
            binding.searching.setText("")
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.clearButton.windowToken, 0)
            viewModel.searchDebounce(binding.searching.text.toString())
        }

        binding.searching.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.searchDebounce(binding.searching.text.toString())
            }
            false
        }

        binding.searching.setOnFocusChangeListener { _, hasFocus ->
            val isStoryVisible = (viewModel.getState().value as? TrackListState.Story)?.story?.isNotEmpty() == true
            binding.clearHistory.isVisible = hasFocus && binding.searching.text.isEmpty() && isStoryVisible
            binding.historyHint.isVisible = binding.clearHistory.isVisible
            binding.placeholderMessage.isVisible = false
        }

        binding.refresh.setOnClickListener {
            viewModel.cancelSearch()
            viewModel.remoteRequest(binding.searching.text.toString())
        }

        binding.clearHistory.setOnClickListener {
            viewModel.clearListenedTracks()
            binding.trackRecyclerView.isVisible = false
            binding.clearHistory.isVisible = false
            binding.historyHint.isVisible = binding.clearHistory.isVisible
        }

        viewModel.getTrackClickEvent().observe(this) { item ->
            MediaActivity.show(this, item)
        }

        lifecycleScope.launch {
            viewModel.getListenedTrackClickEvent().collect { item ->
                MediaActivity.show(this@SearchActivity, item)
            }
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun showLoading() {
        binding.progressBar.isVisible = true
        binding.trackRecyclerView.isVisible = false
        binding.placeholderMessage.isVisible = false
        binding.historyHint.isVisible = false
        binding.clearHistory.isVisible = false
    }

    private fun showContent(tracks: List<Track>) {

        adapter.updateData(tracks)
        binding.trackRecyclerView.adapter = adapter

        binding.historyHint.isVisible = false
        binding.clearHistory.isVisible = false
        showList()
    }

    private fun showStory(story: List<Track>) {

        storyAdapter.updateData(story)
        binding.trackRecyclerView.adapter = storyAdapter

        binding.historyHint.isVisible = story.isNotEmpty()
        binding.clearHistory.isVisible = story.isNotEmpty()
        showList()
    }

    private fun showList() {
        binding.progressBar.isVisible = false
        binding.trackRecyclerView.isVisible = true
        binding.placeholderMessage.isVisible = false
    }

    private fun showError(message: String) {
        binding.progressBar.isVisible = false
        binding.trackRecyclerView.isVisible = false
        binding.historyHint.isVisible = false
        binding.clearHistory.isVisible = false
        binding.placeholderMessage.isVisible = true
        binding.placeholderImage.setImageResource(R.drawable.no_internet)
        binding.errorText.setText(R.string.no_internet)
        binding.refresh.isVisible = true
        showMessage(message)
    }

    private fun showNothingWasFound() {
        binding.progressBar.isVisible = false
        binding.trackRecyclerView.isVisible = false
        binding.historyHint.isVisible = false
        binding.clearHistory.isVisible = false
        binding.placeholderMessage.isVisible = true
        binding.placeholderImage.setImageResource(R.drawable.res_not_ex)
        binding.errorText.setText(R.string.nothing_was_found)
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    companion object {
        fun show(context: Context) {
            val intent = Intent(context, SearchActivity::class.java)
            context.startActivity(intent)
        }
    }
}