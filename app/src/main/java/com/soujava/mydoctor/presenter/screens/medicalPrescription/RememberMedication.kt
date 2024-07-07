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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.soujava.mydoctor.presenter.screens.commons.AppDefaultButton
import com.soujava.mydoctor.presenter.screens.commons.AppSpace
import com.soujava.mydoctor.presenter.screens.commons.AppText
import com.soujava.mydoctor.presenter.screens.commons.SpaceType
import com.soujava.mydoctor.presenter.screens.commons.Types
import com.soujava.mydoctor.presenter.screens.medicalPrescription.component.ItemCard
import com.soujava.mydoctor.presenter.ui.theme.black
import com.soujava.mydoctor.presenter.ui.theme.blue
import com.soujava.mydoctor.presenter.ui.theme.red
import com.soujava.mydoctor.presenter.ui.theme.white
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun RememberMedication(
    onFinish: () -> Unit = {}
) {

    val viewModel: MedicalPrescriptionViewModel = koinViewModel()
    val state = viewModel.state.collectAsState()
    LaunchedEffect(key1 = viewModel) {
        viewModel.getMedications()
    }
    Scaffold {
        LazyColumn(
            modifier = Modifier.padding(it.calculateBottomPadding(), top = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            stickyHeader {
                Column(
                    modifier = Modifier.background(white)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppText(types = Types.SUBTITLE, text = "LEMBRETE DE MEDICAMENTO")
                        AppSpace(  SpaceType.MEDIUM)
                        IconButton(onClick = { onFinish() }) {
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = null
                            )
                        }
                    }

                    HorizontalDivider()

                }
            }
            items(state.value.listMedicalPrescription) { item ->
                ItemCard(medicalPrescription = item)

            }
        }
    }
}