package com.soujava.mydoctor.presenter.screens.medicalPrescription

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.soujava.mydoctor.presenter.screens.commons.AppDefaultButton
import com.soujava.mydoctor.presenter.screens.commons.AppSpace
import com.soujava.mydoctor.presenter.screens.commons.AppText
import com.soujava.mydoctor.presenter.screens.commons.SpaceType
import com.soujava.mydoctor.presenter.screens.commons.Types
import com.soujava.mydoctor.presenter.ui.theme.black
import com.soujava.mydoctor.presenter.ui.theme.blue
import com.soujava.mydoctor.presenter.ui.theme.red
import com.soujava.mydoctor.presenter.ui.theme.white

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun RememberMedication(
    onFinish: () -> Unit = {}
) {

    Scaffold {
        LazyColumn(
            modifier = Modifier.padding(it.calculateBottomPadding(), top = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            stickyHeader {
                Column(
                    modifier = Modifier.background(white)
                ) {
                    AppText(types = Types.REGULAR, text = "LEMBRETE DE MEDICAMENTO")
                    HorizontalDivider()

                }
            }
            items(10){
                Card(
                    modifier = Modifier
                        .padding(10.dp)
                        .size(360.dp, 280.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = black.copy(alpha = 0.5f)
                    ),
                    onClick = { /*TODO*/ }) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AppText(types = Types.REGULAR, text = "Nome do remédio", color = white)
                        HorizontalDivider(
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(white.copy(alpha = 0.5f))
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalAlignment = Alignment.Start,

                            ) {
                            AppText(types = Types.REGULAR, text = "Quantidade a tomar", color = white)
                            AppSpace(SpaceType.SMALL)
                            AppText(types = Types.REGULAR, text = "Como tomar", color = white)
                            AppSpace(SpaceType.SMALL)
                            HorizontalDivider(
                                modifier = Modifier
                                    .padding(top = 10.dp)
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(white.copy(alpha = 0.5f))
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.End
                            ) {
                                AppText(
                                    types = Types.SMALL,
                                    text = "Quantos dias para terminar",
                                    color = white
                                )
                            }
                            HorizontalDivider(
                                modifier = Modifier
                                    .padding(top = 10.dp)
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(white.copy(alpha = 0.5f))
                            )
                            AppSpace(SpaceType.SMALL)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.End
                            ) {
                                AppDefaultButton(
                                    label = "Já tomei",
                                    color = white,
                                    containerColor = red
                                ) {
                                    onFinish()
                                }
                            }
                        }
                    }
                }
            }


        }
    }
}