package com.soujava.mydoctor.presenter.screens.triage

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.KeyboardArrowRight
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.sharp.KeyboardArrowRight
import androidx.compose.material.icons.sharp.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.soujava.mydoctor.core.PromptGemini
import com.soujava.mydoctor.core.parseJson
import com.soujava.mydoctor.domain.models.Triage
import com.soujava.mydoctor.presenter.graph.START_SCREEN
import com.soujava.mydoctor.presenter.graph.TRIAGE_ONE_SCREEN
import com.soujava.mydoctor.presenter.graph.TRIAGE_TWO_SCREEN
import com.soujava.mydoctor.presenter.ui.theme.AppFont
import com.soujava.mydoctor.presenter.ui.theme.black
import com.soujava.mydoctor.presenter.ui.theme.lightGray
import com.soujava.mydoctor.presenter.ui.theme.red
import com.soujava.mydoctor.presenter.ui.theme.white
import com.soujava.mydoctor.presenter.utilPresenters.DefaultPage


@Composable
fun TriageOneScreen(navController: NavHostController) {
    val jsonString = PromptGemini.returnText
    val jsonTriage = parseJson(jsonString)

    Log.d("TAG", "TriageOneScreen: ENTREI")

    val textFieldStates =
        remember { mutableStateListOf<String>().apply { repeat(jsonTriage.items.size) { add("") } } }
    val sliderValue = remember { mutableFloatStateOf(0f) }


    val checkboxItems =
        remember(jsonTriage.items) { jsonTriage.items.filter { it.type == "CHECKBOX" } }

    val listOfCheckboxStates = remember(checkboxItems) {
        mutableStateListOf<List<Boolean>>().apply {
            checkboxItems.forEach { item ->
                item.options?.let { add(it.map { false }) }
            }
        }
    }
    val sliderItems =
        remember(jsonTriage.items) { jsonTriage.items.filter { it.type == "SLIDER" } }

    val listOfSliderStates = remember(sliderItems) {
        mutableStateListOf<List<Float>>().apply {
            sliderItems.forEach { item ->
                item.options?.let { add(it.map { 0f }) }
            }
        }
    }

    val radioGroupItems =
        remember(jsonTriage.items) { jsonTriage.items.filter { it.type == "RADIO_GROUP" } }
    val selectedRadioOptions = remember(radioGroupItems) {
        mutableStateListOf<String>().apply {
            repeat(radioGroupItems.size) { add("") }
        }
    }

    DefaultPage(
        onBack = {
          navController.popBackStack()
        }
    ) {

        if (jsonTriage.items.isEmpty() && jsonTriage.messageToEmptyJson.isNotEmpty()) {
            Text(text = jsonTriage.messageToEmptyJson)
        } else {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                jsonTriage.items.forEachIndexed { index, item ->
                    when (item.type) {
                        "ONE_LINE_TEXT_FIELD" -> {
                            OutlinedTextField(
                                modifier = Modifier
                                    .padding(top = 12.dp)
                                    .fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType =
                                    if (item.typeField.lowercase()
                                            .contains("t")
                                    ) KeyboardType.Text else KeyboardType.Number
                                ),
                                value = textFieldStates[index],
                                label = { Text(text = item.label) },
                                onValueChange = { textFieldStates[index] = it }
                            )
                        }

                        "CHECKBOX" -> {
                            val checkboxIndex = checkboxItems.indexOf(item)
                            Column(
                                modifier = Modifier
                                    .padding(top = 24.dp)
                                    .fillMaxWidth()
                            ) {
                                HeaderTitle(text = item.label)
                                item.options?.forEachIndexed { optionIndex, option ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Checkbox(
                                            checked = listOfCheckboxStates[checkboxIndex][optionIndex],
                                            onCheckedChange = { newValue ->
                                                val updatedList =
                                                    listOfCheckboxStates[checkboxIndex].toMutableList()
                                                updatedList[optionIndex] = newValue
                                                listOfCheckboxStates[checkboxIndex] =
                                                    updatedList
                                            }
                                        )
                                        Text(
                                            text = option,
                                            fontSize = 17.sp,
                                            fontFamily = AppFont.regular
                                        )
                                    }
                                }
                            }
                        }

                        "RADIO_GROUP" -> {
                            val radioIndex = radioGroupItems.indexOf(item)
                            Column(
                                modifier = Modifier
                                    .padding(top = 24.dp)
                                    .fillMaxWidth()
                            ) {
                                HeaderTitle(text = item.label)
                                item.options?.forEach { option ->
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        RadioButton(
                                            selected = option == selectedRadioOptions[radioIndex],
                                            onClick = {
                                                selectedRadioOptions[radioIndex] = option
                                            }
                                        )
                                        Text(
                                            text = option,
                                            fontSize = 17.sp,
                                            fontFamily = AppFont.regular
                                        )
                                    }
                                }
                            }
                        }

                        "MORE_LINE_TEXT_FIELD" -> {
                            Spacer(modifier = Modifier.height(20.dp))
                            HeaderTitle(text = item.label)
                            OutlinedTextField(
                                value = textFieldStates[index],
                                modifier = Modifier
                                    .height(190.dp)
                                    .fillMaxWidth()
                                    .background(white, RoundedCornerShape(16.dp))
                                    .border(1.dp, black, RoundedCornerShape(16.dp))
                                    .padding(12.dp),
                                onValueChange = { textFieldStates[index] = it },
                                placeholder = {
                                    Text(
                                        text = "Digite aqui...",
                                        color = lightGray
                                    )
                                },
                                label = {
                                    Text(
                                        text = "Digite aqui...",
                                        color = lightGray
                                    )
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color.Transparent,
                                    unfocusedBorderColor = Color.Transparent,
                                    errorBorderColor = Color.Transparent
                                )
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FloatingActionButton(
                        containerColor = black,
                        onClick = {
                            val myContent = buildString {
                                jsonTriage.items.forEachIndexed { index, item ->
                                    when (item.type) {
                                        "ONE_LINE_TEXT_FIELD" -> appendLine("Pergunta:\n\t${item.label}\nResposta:\n\t${textFieldStates[index]}")

                                        "CHECKBOX" -> {
                                            val selectedOptions =
                                                listOfCheckboxStates[checkboxItems.indexOf(item)]
                                                    .mapIndexedNotNull { optionIndex, isSelected ->
                                                        if (isSelected) item.options?.get(
                                                            optionIndex
                                                        ) else null
                                                    }
                                            if (selectedOptions.isNotEmpty()) {
                                                appendLine(
                                                    "Pergunta:\n\t${item.label}\nResposta:\n\t${
                                                        selectedOptions.joinToString(
                                                            ", "
                                                        )
                                                    }"
                                                )
                                            }
                                        }
                                        "RADIO_GROUP" -> {
                                            val selectedOption =
                                                selectedRadioOptions[radioGroupItems.indexOf(
                                                    item
                                                )]
                                            if (selectedOption.isNotEmpty()) appendLine("Pergunta:\n\t${item.label}\nResposta:\n\t$selectedOption")
                                        }

                                        "MORE_LINE_TEXT_FIELD" -> appendLine("Pergunta:\n\t${item.label}\nResposta:\n\t${textFieldStates[index]}")
                                    }
                                    appendLine()
                                }
                            }
                            Triage.patient.content = myContent
                            navController.navigate(TRIAGE_TWO_SCREEN)
                        }) {
                        Icon(
                            Icons.AutoMirrored.Sharp.KeyboardArrowRight,
                            contentDescription = null,
                            tint = white
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun HeaderTitle(text: String) {
    Text(
        text = text,
        fontSize = 18.sp,
        fontFamily = AppFont.bold
    )

}