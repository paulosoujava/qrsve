package com.soujava.mydoctor.presenter.screens.access.register

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soujava.mydoctor.presenter.screens.commons.AppSpace
import com.soujava.mydoctor.presenter.screens.commons.AppText
import com.soujava.mydoctor.presenter.screens.commons.AppTopBar
import com.soujava.mydoctor.presenter.screens.commons.Email
import com.soujava.mydoctor.presenter.screens.commons.Info
import com.soujava.mydoctor.presenter.screens.commons.LinearDeterminateIndicator
import com.soujava.mydoctor.presenter.screens.commons.LoadingButton

import com.soujava.mydoctor.presenter.screens.commons.Password
import com.soujava.mydoctor.presenter.screens.commons.SpaceType
import com.soujava.mydoctor.presenter.screens.commons.Types
import com.soujava.mydoctor.presenter.ui.theme.black
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun RegisterScreen(
    navHostController: NavHostController
) {

    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val repPassword = remember { mutableStateOf("") }
    val viewModel: RegisterViewModel = koinViewModel()
    val state = viewModel.state.collectAsState()

    Scaffold(
        modifier = Modifier,
        topBar = {
            AppTopBar {
                navHostController.popBackStack()
            }
        },
        bottomBar = {
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
                val isOk =
                    email.value.isNotEmpty() && password.value.isNotEmpty() && repPassword.value.isNotEmpty() && !state.value.isLoading
                LoadingButton(
                    labelBtn = "Cadastrar",
                    isLoading = state.value.isLoading,
                    enabled = isOk
                ) {
                    if (isOk) {
                        viewModel.register(email.value, password.value, repPassword.value, navHostController)
                    }
                }
                AppSpace(SpaceType.LARGE)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .padding(start = 20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppText(types = Types.TITLE, text = "Cadastro")
            }

            AppSpace(SpaceType.LARGE)

            AnimatedVisibility(visible = state.value.hasError != null && state.value.message.isNotEmpty()) {
                Info(
                    hasError = state.value.hasError,
                    message = state.value.message ?: "ops tente mais tarde",
                    showLinear = state.value.hasError != null &&  !state.value.hasError!!
                ) {
                    viewModel.resetError()
                }
            }
            AppSpace(SpaceType.MEDIUM)
            Email(email = email)
            AppSpace(SpaceType.MEDIUM)
            Password(password = password)
            AppSpace(SpaceType.MEDIUM)
            Password(password = repPassword)

        }

    }
}