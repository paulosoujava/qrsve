package com.soujava.mydoctor.presenter.screens.history.component

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.soujava.mydoctor.domain.models.History
import com.soujava.mydoctor.presenter.screens.commons.AppText
import com.soujava.mydoctor.presenter.screens.commons.Types
import com.soujava.mydoctor.presenter.ui.theme.black
import com.soujava.mydoctor.presenter.ui.theme.lightGray


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListHistory(groupedPatients: List<History>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 90.dp)
    ) {

        val patientsByCrm = groupedPatients.groupBy { it.data.crm }

        patientsByCrm.forEach { (crm, histories) ->
            stickyHeader {
                AppText(types = Types.REGULAR,
                        text = "$crm",
                    color = lightGray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(black)
                        .padding(8.dp)
                )
            }

            items(histories) { history ->
                val show = remember { mutableStateOf(false) }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        AppText(types = Types.SMALL, text = "Criado em: ${history.createdAt}")
                        AppText(types = Types.SMALL,  text ="${history.data.nameOfDoctor}")
                        AppText(types = Types.SMALL, text ="Data do exame: ${history.data.dateThisExam}")
                        AppText(types = Types.SMALL, text ="Solicitante: ${history.data.nameOfPatient}")
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, bottom = 8.dp)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically) {
                            TextButton(onClick = {
                                show.value = !show.value
                            }) {
                                Text(
                                     if(show.value) "Esconder" else "ver mais",
                                    color = black,
                                    textAlign = TextAlign.End,
                                )
                            }
                        }

                       AnimatedVisibility(visible = show.value) {
                           AppText(types = Types.SMALL, text =" ${history.data.resume}")
                       }
                    }
                }
            }
        }
    }
}