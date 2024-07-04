package com.soujava.mydoctor.presenter.screens.scanner

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.soujava.mydoctor.core.formatText
import com.soujava.mydoctor.presenter.screens.commons.AppText
import com.soujava.mydoctor.presenter.screens.commons.Types
import com.soujava.mydoctor.presenter.ui.theme.AppFont
import com.soujava.mydoctor.presenter.ui.theme.black
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyzeScreen(
    navController: NavHostController
) {

    val viewModel: AnalyzeViewModel = koinViewModel()
    val state = viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.analytics()
    }
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            Icons.Outlined.ArrowBackIosNew,
                            contentDescription = null
                        )
                    }
                },
                title = {
                    Text(
                        text = "Scanner",
                        fontFamily = AppFont.bold,
                        fontSize = 24.sp
                    )
                })
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(start = 16.dp, end = 16.dp, top = 25.dp)
                    .fillMaxSize()
            ) {

                if (state.value.content != "") {
                    Text(text = state.value.content)
                }

                if (state.value.error != "") {
                    AppText(types = Types.REGULAR, text = state.value.error)
                }
            }
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (state.value.loading)
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        color = black,
                        strokeWidth = 2.dp
                    )
            }
        }

    }
}