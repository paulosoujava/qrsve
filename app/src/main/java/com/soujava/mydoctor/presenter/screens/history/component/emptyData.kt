package com.soujava.mydoctor.presenter.screens.history.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.soujava.mydoctor.presenter.screens.commons.AppText
import com.soujava.mydoctor.presenter.screens.commons.Types
import com.soujava.mydoctor.presenter.ui.theme.darkGray

@Composable
fun EmptyData(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .padding(start = 30.dp, end = 20.dp, bottom = 20.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        AppText(
            types = Types.REGULAR,
            color = darkGray,
            text = "Aqui você adiciona o seu exames em um banco de dados para que fiquem acessiveis quando você " +
                    "desejar."
        )
    }
}