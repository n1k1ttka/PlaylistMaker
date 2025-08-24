package com.example.playlistmaker.ui.search.layout

import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.res.Configuration
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation.Companion.keyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.example.playlistmaker.Presentation.model.ParcelableTrack
import com.example.playlistmaker.Presentation.state.TrackListState
import com.example.playlistmaker.Presentation.utils.dpToPx
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onTrackClick: (ParcelableTrack) -> Unit,
    onStoryClick: (ParcelableTrack) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val text by viewModel.query.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    Scaffold(
        containerColor = colorResource(R.color.white_black),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.search),
                        color = colorResource(R.color.black_white),
                        fontFamily = FontFamily(Font(R.font.ys_text_medium)),
                        fontSize = 22.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.white_black)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = text,
                onValueChange = { newText ->
                    viewModel.setQuery(newText)
                    viewModel.cancelSearch()
                    viewModel.searchDebounce(newText)
                },
                placeholder = {
                    Text(
                        text = stringResource(R.string.search),
                        color = colorResource(R.color.hint_color),
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.ys_text_regular))
                    )
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(12.dp)),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        viewModel.searchDebounce(text)
                        focusManager.clearFocus()
                    }
                ),
                trailingIcon = {
                    if (text.isNotEmpty()) {
                        IconButton(onClick = {
                            viewModel.setQuery("")
                            viewModel.cancelSearch()
                            viewModel.searchDebounce("")
                        }) {
                            Icon(Icons.Default.Close, "Очистить", tint = colorResource(R.color.gray))
                        }
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    focusedContainerColor = colorResource(R.color.yp_light_gray),
                    unfocusedContainerColor = colorResource(R.color.yp_light_gray)
                )
            )

            when (state) {
                is TrackListState.Loading -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator(modifier = Modifier.size(44.dp), color = Color(0xFF3772E7), strokeWidth = 4.dp) }

                is TrackListState.Content -> TrackList(
                    tracks = (state as TrackListState.Content).tracks,
                    trackAction = { track ->
                        viewModel.onTrackClick(track)
                        onTrackClick(track)
                    }
                )

                is TrackListState.Story -> TrackList(
                    tracks = (state as TrackListState.Story).story,
                    trackAction = { track -> onStoryClick(track) },
                    buttonState = (state as TrackListState.Story).story.isNotEmpty(),
                    buttonAction = { viewModel.clearListenedTracks() }
                )

                is TrackListState.Error -> {
                    PlaceholderMessage(
                        imageRes = R.drawable.no_internet,
                        errorText = stringResource(R.string.no_internet),
                        buttonState = true,
                        buttonAction = { viewModel.remoteRequest(text) }
                    )
                    Toast.makeText(context, (state as TrackListState.Error).errorMessage, Toast.LENGTH_LONG).show()
                }

                TrackListState.ZeroContent -> PlaceholderMessage(
                    imageRes = R.drawable.res_not_ex,
                    errorText = stringResource(R.string.nothing_was_found)
                )

                null -> {}
            }
        }
    }
}

@Composable
fun TrackList(
    tracks: List<ParcelableTrack>,
    trackAction: (ParcelableTrack) -> Unit,
    buttonState: Boolean = false,
    buttonAction: () -> Unit = {}
){
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(tracks){track -> MyTrackView(track, trackAction) }
    }
    if(buttonState) Button(
        onClick = buttonAction,
        modifier = Modifier
            .wrapContentSize()
            .padding(top = 24.dp)
            .clip(RoundedCornerShape(54.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.black_white),
            contentColor = colorResource(R.color.white_black)
        )
    ) { Text(text = stringResource(R.string.clear_history), fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.ys_text_medium))) }
}

@Composable
fun MyTrackView(track: ParcelableTrack, onClick: (ParcelableTrack) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(track) }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(track.artworkUrl100)
                .crossfade(true)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .transformations(RoundedCornersTransformation(2.dpToPx(LocalContext.current).toFloat()))
                .build(),
            contentDescription = null,
            modifier = Modifier.size(45.dp).clip(RoundedCornerShape(2.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.width(8.dp))
        Column(
            modifier = Modifier.weight(1f).padding(end = 12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = track.trackName, fontFamily = FontFamily(Font(R.font.ys_text_regular)),
                fontSize = 16.sp, color = colorResource(R.color.black_white),
                maxLines = 1, overflow = TextOverflow.Ellipsis)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = track.artistName, fontFamily = FontFamily(Font(R.font.ys_text_regular)),
                    fontSize = 11.sp, color = colorResource(R.color.music),
                    modifier = Modifier.weight(1f),
                    maxLines = 1, overflow = TextOverflow.Ellipsis)
                Icon(painter = painterResource(id = R.drawable.ic_interpoint),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp).padding(horizontal = 4.dp),
                    tint = colorResource(R.color.music))
                Text(text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis),
                    fontFamily = FontFamily(Font(R.font.ys_text_regular)),
                    fontSize = 11.sp,
                    color = colorResource(R.color.music))
            }
        }
        Icon(painter = painterResource(id = R.drawable.ic_track_arrow),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = colorResource(R.color.music))
    }
}

@Composable
fun PlaceholderMessage(imageRes: Int, errorText: String, buttonState: Boolean = false, buttonAction: () -> Unit = {}) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 102.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = painterResource(imageRes), contentDescription = null, modifier = Modifier.wrapContentSize())
        Text(text = errorText, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 8.dp).wrapContentSize())
        if(buttonState) Button(onClick = buttonAction,
            modifier = Modifier.wrapContentSize().padding(top = 24.dp).clip(RoundedCornerShape(54.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.black_white), contentColor = colorResource(R.color.white_black))) {
            Text(text = stringResource(R.string.refresh))
        }
    }
}


@Preview(showSystemUi = true, showBackground = true,
    uiMode = Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun SearchScreenPreview(){
    //SearchScreen()
}