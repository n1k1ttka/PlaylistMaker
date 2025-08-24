package com.example.playlistmaker.ui.media.fragment

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.playlistmaker.Presentation.model.media.MediaPagerAdapter
import com.example.playlistmaker.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

@Composable
fun MediaPlayerScreen(host: Fragment) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.white_black))
            .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
    ) {
        Text(
            text = stringResource(R.string.mediateka),
            fontSize = 22.sp,
            fontFamily = FontFamily(Font(R.font.ys_text_medium)),
            color = colorResource(R.color.black_white),
            modifier = Modifier.padding(start = 16.dp, top = 14.dp, bottom = 16.dp)
        )

        AndroidView(
            modifier = Modifier
                .fillMaxSize(),
            factory = {
                buildTabsWithPager(
                    context = context,
                    host = host
                )
            }
        )
    }
}

private fun buildTabsWithPager(
    context: Context,
    host: Fragment
): View {
    val container = LinearLayout(context).apply {
        orientation = LinearLayout.VERTICAL
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        setBackgroundColor(context.getColor(R.color.white_black))
    }

    val tabLayout = TabLayout(context).apply {
        setBackgroundColor(context.getColor(R.color.white_black))
        setSelectedTabIndicatorColor(context.getColor(R.color.black_white))
        setTabTextColors(
            context.getColor(R.color.black_white),
            context.getColor(R.color.black_white)
        )
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
    }

    val viewPager = ViewPager2(context).apply {
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            0,
            1f
        )
        adapter = MediaPagerAdapter(host.childFragmentManager, host.viewLifecycleOwner.lifecycle)
    }

    container.addView(tabLayout)
    container.addView(viewPager)

    val mediator = TabLayoutMediator(tabLayout, viewPager) { tab, position ->
        tab.text = when (position) {
            0 -> context.getString(R.string.favorites)
            else -> context.getString(R.string.playlists)
        }
    }
    mediator.attach()

    container.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(v: View) = Unit
        override fun onViewDetachedFromWindow(v: View) {
            try { mediator.detach() } catch (_: Throwable) { }
        }
    })
    return container
}
