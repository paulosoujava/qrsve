package com.soujava.mydoctor.presenter.screens.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.NavigateNext
import androidx.compose.material.icons.rounded.NavigateNext
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.soujava.mydoctor.presenter.screens.commons.AppSpace
import com.soujava.mydoctor.presenter.screens.commons.AppText
import com.soujava.mydoctor.presenter.screens.commons.NumberField
import com.soujava.mydoctor.presenter.screens.commons.SpaceType
import com.soujava.mydoctor.presenter.screens.commons.TextField
import com.soujava.mydoctor.presenter.screens.commons.Types
import com.soujava.mydoctor.presenter.ui.theme.black
import com.soujava.mydoctor.presenter.ui.theme.white
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.rememberMessageBarState
import java.lang.Exception


@Composable
fun PersonalDataTab(
    isSave: Boolean,
    state: State<ProfileUI>,
    name: MutableState<String>,
    cpf: MutableState<String>,
    phone: MutableState<String>,
    onNext: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(key1 = focusRequester) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier
            .padding(top = 20.dp)
            .padding(8.dp)
            .fillMaxWidth()
            .padding(10.dp),
        horizontalAlignment = Alignment.Start,
    ) {


        AppSpace(SpaceType.MEDIUM)
        TextField(
            modifier = Modifier.focusRequester(focusRequester),
            input = name,
            label = "Nome completo",
            placeholder = "Digite seu nome",
            imeAction = ImeAction.Next
        )
        AppSpace(SpaceType.MEDIUM)
        NumberField(
            input = cpf,
            label = "Cpf",
            placeholder = "Digite seu cpf",
            imeAction = ImeAction.Next,
            onDone = {
                if (name.value.isNotEmpty() && cpf.value.isNotEmpty() && phone.value.isNotEmpty()) {
                    onNext()
                }
            }
        )
        AppSpace(SpaceType.MEDIUM)
        NumberField(
            input = phone,
            label = "Celular",
            placeholder = "Digite seu número",
            imeAction = ImeAction.Done,
            onDone = {
                if (name.value.isNotEmpty() && cpf.value.isNotEmpty() && phone.value.isNotEmpty()) {
                    onNext()
                }
            }
        )
        AppSpace(SpaceType.MEDIUM)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 10.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedVisibility(
                visible = name.value.isNotEmpty() && cpf.value.isNotEmpty() && phone.value.isNotEmpty() ||
                        isSave
            ) {
                FloatingActionButton(
                    onClick = onNext,
                    containerColor = black,
                    contentColor = white
                ) {

                    if (state.value.isLoading) {
                        CircularProgressIndicator(
                            color = white,
                            modifier = Modifier.padding(10.dp),
                            strokeWidth = 1.dp
                        )
                    } else
                        Icon(
                            if (isSave) Icons.Rounded.Save else Icons.AutoMirrored.Rounded.NavigateNext,
                            contentDescription = null
                        )
                }
            }
        }
    }
}
