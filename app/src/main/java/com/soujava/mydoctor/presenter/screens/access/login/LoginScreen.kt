package com.soujava.mydoctor.presenter.screens.access.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.soujava.mydoctor.presenter.graph.REGISTER_SCREEN
import com.soujava.mydoctor.presenter.screens.commons.AppSpace
import com.soujava.mydoctor.presenter.screens.commons.AppText
import com.soujava.mydoctor.presenter.screens.commons.AppTextButtonSmall
import com.soujava.mydoctor.presenter.screens.commons.AppTopBar
import com.soujava.mydoctor.presenter.screens.commons.Email
import com.soujava.mydoctor.presenter.screens.commons.Info
import com.soujava.mydoctor.presenter.screens.commons.LoadingButton
import com.soujava.mydoctor.presenter.screens.commons.Password
import com.soujava.mydoctor.presenter.screens.commons.SpaceType
import com.soujava.mydoctor.presenter.screens.commons.Types
import com.soujava.mydoctor.presenter.ui.theme.AppFont
import com.soujava.mydoctor.presenter.ui.theme.black
import com.soujava.mydoctor.presenter.ui.theme.green
import com.soujava.mydoctor.presenter.ui.theme.lightGray
import com.soujava.mydoctor.presenter.ui.theme.red
import com.soujava.mydoctor.presenter.ui.theme.white
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController) {

    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val showForget = remember { mutableStateOf(false) }
    val viewModel: LoginViewModel = koinViewModel()
    val state = viewModel.state.collectAsState()
    val label = if (showForget.value) "Esqueci a senha" else "Login"
    val labelBtn = if (showForget.value) "Recuperar" else "Acessar"

    Scaffold(
        topBar = {
            AnimatedVisibility(
                visible = showForget.value,
                enter = slideInVertically(),
                exit = slideOutVertically()
            ) {
                AppTopBar {
                    showForget.value = !showForget.value
                }
            }

        },
        bottomBar = {
            val isOk =
                email.value.isNotEmpty() && password.value.isNotEmpty() && !state.value.isLoading
            Column(
                modifier = Modifier
                    .shadow(
                        elevation = 10.dp,
                        shape = RoundedCornerShape(
                            topStart = 20.dp,
                            topEnd = 20.dp
                        )
                    )
                    .background(
                        black, shape = RoundedCornerShape(
                            topStart = 20.dp,
                            topEnd = 20.dp
                        )
                    )
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                AppSpace(SpaceType.MEDIUM)
                LoadingButton(
                    labelBtn = labelBtn,
                    isLoading = state.value.isLoading,
                    enabled = isOk
                ) {
                    if (isOk) {
                        if (showForget.value)
                            viewModel.forgetPassword(email.value)
                        else
                            viewModel.doLogin(email.value, password.value, navController)
                    }
                }
                AppSpace(SpaceType.MEDIUM)
                AnimatedVisibility(visible = !showForget.value) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        AppTextButtonSmall(
                            text = "Cria uma nova conta.",
                            colorFont = white,
                            onClick = { navController.navigate(REGISTER_SCREEN) })
                    }
                }
                AppSpace(SpaceType.MEDIUM)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(
                    top = if (showForget.value) 120.dp else 90.dp,
                    bottom = padding.calculateBottomPadding()
                )
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .padding(start = 20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppText(types = Types.TITLE, text = label)
            }
            AppSpace(SpaceType.MEDIUM)
            AnimatedVisibility(visible = state.value.hasError != null && !state.value.message.isNullOrBlank()) {
                Info(
                    hasError = state.value.hasError,
                    message = state.value.message ?: "ops tente mais tarde"
                ) {
                    viewModel.resetError()
                }
            }

            Email(email = email)
            if (!showForget.value) {
                AppSpace(SpaceType.MEDIUM)
                Password(password = password)
                AppSpace(SpaceType.MEDIUM)

                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.TopEnd
                ) {
                    AppTextButtonSmall(
                        text = "Esqueceu a senha?",
                        modifier = Modifier.padding(end = 10.dp),
                        onClick = { showForget.value = true })

                }
            }
            AppSpace(SpaceType.LARGE)
        }

    }
}