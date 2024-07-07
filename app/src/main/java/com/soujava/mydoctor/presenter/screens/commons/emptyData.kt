package com.soujava.mydoctor.presenter.screens.commons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.soujava.mydoctor.presenter.ui.theme.darkGray

@Composable
fun EmptyData(
    message: String
) {
    Box(
        modifier = Modifier
            .padding(start = 30.dp, end = 20.dp, bottom = 20.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        AppText(
            types = Types.REGULAR,
            color = darkGray,
            text = message
        )
    }
}