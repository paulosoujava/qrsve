package com.soujava.mydoctor.presenter.screens.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.NavigateNext
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soujava.mydoctor.presenter.screens.commons.AppSpace
import com.soujava.mydoctor.presenter.screens.commons.AppText
import com.soujava.mydoctor.presenter.screens.commons.SpaceType
import com.soujava.mydoctor.presenter.screens.commons.TextArea
import com.soujava.mydoctor.presenter.screens.commons.Types
import com.soujava.mydoctor.presenter.ui.theme.AppFont
import com.soujava.mydoctor.presenter.ui.theme.black
import com.soujava.mydoctor.presenter.ui.theme.lightGray
import com.soujava.mydoctor.presenter.ui.theme.white
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.rememberMessageBarState
import java.lang.Exception


@Composable
fun ClinicalDataTab(
    isSave:Boolean,
    state: State<ProfileUI>,
    selectedOptionMedication: MutableState<String>,
    moreInfoMedication: MutableState<String>,
    selectedOptionAllergy: MutableState<String>,
    moreInfoAllergy: MutableState<String>,
    onSaveFullProfile: () -> Unit
) {


    val radioGroupItem = listOf("Sim", "Não")
    val focusRequesterMedication = remember { FocusRequester() }
    val focusRequesterAllergy = remember { FocusRequester() }

    if(selectedOptionMedication.value == "Sim") {
        LaunchedEffect(key1 = focusRequesterMedication) {
            focusRequesterMedication.requestFocus()
        }
    }
    if (selectedOptionAllergy.value == "Sim") {
        LaunchedEffect(key1 = focusRequesterAllergy) {
            focusRequesterAllergy.requestFocus()
        }
    }

    Column(
        modifier = Modifier
            .padding(top = 20.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth()
            .padding(10.dp),
        horizontalAlignment = Alignment.Start,

        ) {

        AppSpace(SpaceType.MEDIUM)
        AppText(types = Types.SMALL, text = "Faz uso de alguma medicação?")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround

        ) {
            radioGroupItem?.let { options ->
                options.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            selected = option == selectedOptionMedication.value,
                            onClick = {
                                selectedOptionMedication.value = option
                                if(option == "Não")
                                    moreInfoMedication.value = ""
                            },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colorScheme.primary,  // Cor do RadioButton selecionado
                                unselectedColor = Color.Gray  // Cor do RadioButton não selecionado
                            )
                        )
                        AppText(types = Types.TEXT, text = option)
                    }

                }
            }
        }

        HorizontalDivider(
            color = lightGray,
            thickness = 1.dp,
            modifier = Modifier.padding(vertical = 36.dp)
        )
        AnimatedVisibility(visible = selectedOptionMedication.value == "Sim") {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                AppText(
                    types = Types.SMALL,
                    text = "Digite as medicações de uso continuo abaixo"
                )
                TextArea(
                    modifier = Modifier.focusRequester(focusRequesterMedication),
                    input = moreInfoMedication,
                    label = "Tem algo mais para acrescentar",
                    placeholder = "digite aqui..."
                )
            }
        }
        AppSpace(SpaceType.MEDIUM)
        AppText(types = Types.SMALL, text = "Algum tipo de alergia?")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround

        ) {
            radioGroupItem?.let { options ->
                options.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            selected = option == selectedOptionAllergy.value,
                            onClick = {
                                selectedOptionAllergy.value = option
                                if(option == "Não")
                                    moreInfoAllergy.value = ""
                            },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colorScheme.primary,  // Cor do RadioButton selecionado
                                unselectedColor = Color.Gray  // Cor do RadioButton não selecionado
                            )
                        )
                        Text(
                            text = option,
                            fontSize = 17.sp,
                            fontFamily = AppFont.regular,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        AnimatedVisibility(visible = selectedOptionAllergy.value == "Sim") {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                AppText(
                    types = Types.SMALL,
                    text = "Digite quais alergias você tem abaixo"
                )
                TextArea(
                    modifier = Modifier.focusRequester(focusRequesterAllergy),
                    input = moreInfoAllergy,
                    label = "Tem algo mais para acrescentar",
                    placeholder = "digite aqui..."
                )
            }
        }
        AppSpace(SpaceType.MEDIUM)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedVisibility(
                visible = selectedOptionAllergy.value.isNotEmpty() && selectedOptionMedication.value.isNotEmpty() &&
                        (selectedOptionMedication.value == "Sim" && moreInfoMedication.value.isNotEmpty() ) &&
                        (selectedOptionAllergy.value == "Sim" && moreInfoAllergy.value.isNotEmpty())  ||
                        (selectedOptionMedication.value == "Não" && moreInfoMedication.value.isEmpty() ) ||
                        (selectedOptionAllergy.value == "Não" && moreInfoAllergy.value.isEmpty() )

            ) {
                FloatingActionButton(
                    onClick = onSaveFullProfile,
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
                        if(isSave) Icons.Rounded.Save else Icons.AutoMirrored.Rounded.NavigateNext,
                        contentDescription = null
                    )
                }
            }
        }
    }
}