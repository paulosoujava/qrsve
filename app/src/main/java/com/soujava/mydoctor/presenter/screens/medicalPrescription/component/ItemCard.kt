package com.soujava.mydoctor.presenter.screens.medicalPrescription.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.soujava.mydoctor.domain.models.MedicalPrescription
import com.soujava.mydoctor.presenter.screens.commons.AppDefaultButton
import com.soujava.mydoctor.presenter.screens.commons.AppSpace
import com.soujava.mydoctor.presenter.screens.commons.AppText
import com.soujava.mydoctor.presenter.screens.commons.SpaceType
import com.soujava.mydoctor.presenter.screens.commons.Types
import com.soujava.mydoctor.presenter.ui.theme.black
import com.soujava.mydoctor.presenter.ui.theme.red
import com.soujava.mydoctor.presenter.ui.theme.white

@Composable
fun ItemCard(
    medicalPrescription: MedicalPrescription,
    height: Int = 280,
) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .size(410.dp, height.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = black.copy(alpha = 0.5f)
        ),
        onClick = {  }) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppText(types = Types.REGULAR, text = medicalPrescription.nameOfMedications, color = white)
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
                AppText(types = Types.REGULAR, text = "Tomar ${medicalPrescription.qtdPerDay}x por dia", color = white)
                AppSpace(SpaceType.SMALL)
                AppText(types = Types.SMALL, text = medicalPrescription.description, color = white)
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
                        text = "Tomar por ${medicalPrescription.duration} ",
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
                    AppText(
                        types = Types.SMALL,
                        text = "Iniciado em ${medicalPrescription.dateBegin}\ntermina em ${medicalPrescription.dateEnd}",
                        color = white
                    )
                }
            }
        }
    }
}