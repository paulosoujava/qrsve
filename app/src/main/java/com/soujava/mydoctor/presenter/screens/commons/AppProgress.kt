package com.soujava.mydoctor.presenter.screens.commons

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.soujava.mydoctor.presenter.ui.theme.black
import com.soujava.mydoctor.presenter.ui.theme.red
import com.soujava.mydoctor.presenter.ui.theme.white
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun Show(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LinearDeterminateIndicator()
    }
}

@Composable
fun LinearDeterminateIndicator() {
    var currentProgress by remember { mutableStateOf(0f) }
    var loading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope() // Create a coroutine scope

    LaunchedEffect(key1 = Unit) {
        scope.launch {
            loadProgress { progress ->
                currentProgress = progress
            }
            loading = false // Reset loading when the coroutine finishes
        }

    }
    if (loading) {
        LinearProgressIndicator(
            progress = { currentProgress },
            color = red,
            modifier = Modifier.fillMaxWidth(),
        )
    }

}

/** Iterate the progress value */
suspend fun loadProgress(updateProgress: (Float) -> Unit) {
    for (i in 1..100) {
        updateProgress(i.toFloat() / 100)
        delay(100)
    }
}

