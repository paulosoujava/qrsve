package com.soujava.mydoctor.presenter.screens.search

import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.sharp.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.soujava.mydoctor.core.getFormattedDate
import com.soujava.mydoctor.domain.models.Triage
import com.soujava.mydoctor.presenter.screens.commons.AppText
import com.soujava.mydoctor.presenter.screens.commons.TextArea
import com.soujava.mydoctor.presenter.screens.commons.Types
import com.soujava.mydoctor.presenter.screens.start.UiStateMain
import com.soujava.mydoctor.presenter.ui.theme.AppFont
import com.soujava.mydoctor.presenter.ui.theme.black
import com.soujava.mydoctor.presenter.ui.theme.lightGray
import com.soujava.mydoctor.presenter.ui.theme.red
import com.soujava.mydoctor.presenter.ui.theme.white
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavHostController
) {
    var typed = remember { mutableStateOf("") }
    val focusRequesterAllergy = remember { FocusRequester() }
    var hasError by remember { mutableStateOf(false) }
    val viewMode: SearchViewModel = koinViewModel()
    val state = viewMode.state.collectAsState()

    val keyboard = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1 = focusRequesterAllergy) {
        focusRequesterAllergy.requestFocus()
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = {
                            navController.popBackStack()
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    title = {
                        Text(text = "Indicação")

                    })
                HorizontalDivider()
            }

        }
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(top = 110.dp, bottom = it.calculateBottomPadding())
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Fale  um pouco sobre a dor para que possamos indicar um especialiasta.",
                color = black,
                fontFamily = AppFont.bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 30.dp, end = 30.dp)
            )
            TextArea(
                modifier = Modifier
                    .padding(start = 30.dp, end = 30.dp)
                    .focusRequester(focusRequesterAllergy),
                input = typed,
                label = "Diga-me o que sentes..",
                placeholder = "digite aqui...",
                hasError = hasError,
            )

            AnimatedVisibility(visible = hasError) {
                AppText(types = Types.SUBTITLE, text = "Preencha o campo acima",
                    modifier = Modifier
                        .padding(start = 35.dp)
                        .fillMaxWidth(),
                )

            }
            Row(
                modifier = Modifier
                    .padding(
                        top = 10.dp,
                        end = 30.dp
                    )
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FloatingActionButton(
                    containerColor = black,
                    onClick = {
                        viewMode.indications(
                            typed.value
                        )
                        keyboard?.hide()
                    }) {
                    if (state.value.isLoading) {
                        CircularProgressIndicator(
                            color = white,
                            modifier = Modifier.size(44.dp),
                            strokeWidth = 1.dp
                        )
                    } else
                        Icon(
                            Icons.AutoMirrored.Sharp.KeyboardArrowRight,
                            contentDescription = null,
                            tint = white
                        )
                }
            }

            androidx.compose.animation.AnimatedVisibility(!state.value.prompt.isNullOrBlank()) {

                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    AppText(types = Types.SUBTITLE, text = "Resposta")
                    AppText(types = Types.REGULAR, text = state.value.prompt ?: "")
                }
            }

        }
    }
}
