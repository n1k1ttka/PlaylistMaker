package com.example.playlistmaker.ui.settings.layout

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val isDarkTheme by viewModel.themeState.observeAsState(initial = false)
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.white_black))
    ) {
        // Заголовок
        Text(
            text = stringResource(R.string.settings),
            color = colorResource(R.color.black_white),
            fontFamily = FontFamily(Font(R.font.ys_text_medium)),
            fontSize = 22.sp,
            modifier = Modifier
                .padding(top = 14.dp, start = 16.dp, bottom = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.dark_theme),
                color = colorResource(R.color.black_white),
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.ys_text_regular))
            )
            Switch(
                checked = isDarkTheme,
                onCheckedChange = { viewModel.toggleTheme(it) },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = colorResource(R.color.blue),
                    uncheckedThumbColor = colorResource(R.color.gray),
                    checkedTrackColor = colorResource(R.color.light_blue),
                    uncheckedTrackColor = colorResource(R.color.yp_light_gray)
                )
            )

        }

        // Кнопки
        SettingsButton(
            label = stringResource(R.string.share),
            icon = R.drawable.ic_share,
            onClick = { context.startActivity(viewModel.getShareIntent()) }
        )
        SettingsButton(
            label = stringResource(R.string.support),
            icon = R.drawable.ic_support,
            onClick = { context.startActivity(viewModel.getSupportIntent()) }
        )
        SettingsButton(
            label = stringResource(R.string.contract),
            icon = R.drawable.ic_arrow,
            onClick = { context.startActivity(viewModel.getContractIntent()) }
        )
    }
}

@Composable
fun SettingsButton(
    label: String,
    @DrawableRes icon: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = colorResource(R.color.black_white),
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.ys_text_regular))
        )
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = colorResource(R.color.black_white)
        )
    }
}