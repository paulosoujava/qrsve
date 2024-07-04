package com.soujava.mydoctor.presenter.screens.commons

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soujava.mydoctor.presenter.ui.theme.AppFont
import com.soujava.mydoctor.presenter.ui.theme.black

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(onClick : () -> Unit) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onClick) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBackIos, contentDescription = null,
                    modifier = Modifier.size(14.dp)
                )
            }
        },
        title = {
            AppText(types = Types.SMALL, text = "Voltar")
        }
    )

}