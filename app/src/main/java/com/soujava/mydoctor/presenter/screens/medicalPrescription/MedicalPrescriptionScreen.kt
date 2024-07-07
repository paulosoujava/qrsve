package com.soujava.mydoctor.presenter.screens.medicalPrescription

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.TipsAndUpdates
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.soujava.mydoctor.core.getFormattedDate
import com.soujava.mydoctor.presenter.screens.commons.AppSpace
import com.soujava.mydoctor.presenter.screens.commons.AppText
import com.soujava.mydoctor.presenter.screens.commons.EmptyData
import com.soujava.mydoctor.presenter.screens.commons.Loading
import com.soujava.mydoctor.presenter.screens.commons.SpaceType
import com.soujava.mydoctor.presenter.screens.commons.TextArea
import com.soujava.mydoctor.presenter.screens.commons.TextField
import com.soujava.mydoctor.presenter.screens.commons.Types
import com.soujava.mydoctor.presenter.screens.history.Events
import com.soujava.mydoctor.presenter.screens.medicalPrescription.component.CameraMedicalPrescription
import com.soujava.mydoctor.presenter.ui.theme.black
import com.soujava.mydoctor.presenter.ui.theme.blue
import com.soujava.mydoctor.presenter.ui.theme.white
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@Preview
@Composable
private fun Show() {
    Column(
        modifier = Modifier
            .padding(top = 60.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppText(
            types = Types.SMALL,
            text = "Encontramos estas informações por favor verifique se esta correto"
        )

        val crm = remember { mutableStateOf("") }
        val doctor = remember { mutableStateOf("") }
        val duration = remember { mutableStateOf("") }
        val qtdPerDay = remember { mutableStateOf("") }
        val nameOfMedications = remember { mutableStateOf("") }
        val description = remember { mutableStateOf("") }
        val initDate = remember { mutableStateOf("") }

        TextField(input = doctor, label = "DR/a", placeholder = "DR/a")
        AppSpace(SpaceType.SMALL)
        TextField(input = crm, label = "CRM", placeholder = "CRM")
        AppSpace(SpaceType.SMALL)
        TextField(
            input = nameOfMedications,
            label = "Nome do medicamento",
            placeholder = "Nome do medicamento"
        )
        AppSpace(SpaceType.SMALL)
        TextField(
            input = qtdPerDay,
            label = "Quantidade por dia",
            placeholder = "Quantidade por dia"
        )
        AppSpace(SpaceType.SMALL)
        TextField(
            input = duration,
            label = "Duração deste remédio",
            placeholder = "Duração deste remédio"
        )
        AppSpace(SpaceType.SMALL)
        TextArea(
            input = description,
            label = "Descrição",
            placeholder = "Descrição",
            modifier = Modifier.padding(
                start = 20.dp,
                end = 20.dp,
            )
        )
        AppSpace(SpaceType.SMALL)
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = {
                initDate.value = getFormattedDate()
            }) {
                AppText(text = "Hoje mesmo", types = Types.SMALL, color = blue)
            }
        }

        TextField(
            input = initDate,
            label = "Quando vou começar a tomar este remédio?",
            placeholder = "digite a data de inicio"
        )
        AppSpace(SpaceType.LARGE)
        HorizontalDivider()
        AppSpace(SpaceType.LARGE)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicalPrescriptionScreen(
    navController: NavHostController,
    permissionOk: MutableState<Boolean>
) {
    val context = LocalContext.current
    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE or
                        CameraController.VIDEO_CAPTURE
            )
        }
    }

    val viewModel: MedicalPrescriptionViewModel = koinViewModel()
    val state = viewModel.state.collectAsState()

    val canOverlay = remember { mutableStateOf(Settings.canDrawOverlays(context)) }
    var showPopUp = remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        canOverlay.value = Settings.canDrawOverlays(context)
    }
    val scope = rememberCoroutineScope()

    val intent = remember {
        Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:${context.packageName}")
        )
    }


    if (showPopUp.value) {

        Dialog(
            onDismissRequest = { showPopUp.value = false }) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .background(white, shape = RoundedCornerShape(10.dp))
                    .padding(10.dp)
            ) {
                AppText(
                    types = Types.REGULAR,
                    text = "Precisamos de permissão para sobrepor o app"
                )

                HorizontalDivider(
                    modifier = Modifier
                        .padding(top = 20.dp, bottom = 20.dp)
                )

                AppText(
                    types = Types.SMALL,
                    text = "Para que você possa usar esta funcionalidade, vamos precisar que você conceda a permissão, para" +
                            "    que possamos exibir os lembretes dos remedios que você adicionous ao historico."
                )
                HorizontalDivider(
                    modifier = Modifier
                        .padding(top = 20.dp, bottom = 20.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = {
                        showPopUp.value = false
                        launcher.launch(intent)
                    }) {
                        AppText(text = "Permitir", types = Types.REGULAR, color = blue)
                    }
                }
            }
        }
    }
    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = {
                            if (state.value.events != EventsMedicalPrescription.CAMERA)
                                navController.popBackStack()
                            else
                                viewModel.event(EventsMedicalPrescription.REGULAR)
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    title = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AppText(types = Types.REGULAR, text = "Histórico de exames")

                            AnimatedVisibility(visible = state.value.events != EventsMedicalPrescription.CAMERA) {
                                IconButton(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .background(black, shape = CircleShape)
                                        .clip(CircleShape),
                                    onClick = {
                                        if (canOverlay.value) {
                                            viewModel.event(EventsMedicalPrescription.CAMERA)
                                        } else {
                                            showPopUp.value = true
                                        }
                                    }) {
                                    Icon(
                                        Icons.Outlined.PhotoCamera,
                                        tint = white,
                                        contentDescription = "Camera Icon"
                                    )
                                }
                            }
                            IconButton(
                                modifier = Modifier
                                    .size(30.dp)
                                    .background(black, shape = CircleShape)
                                    .clip(CircleShape),
                                onClick = {
                                    if (canOverlay.value) {
                                        scope.launch {
                                            delay(4000)
                                            Log.d(
                                                "OverlayDebug",
                                                "canOverlay value: ${canOverlay.value}"
                                            )
                                            val intent = Intent(context, OverlayActivity::class.java)
                                            context.startActivity(intent)
                                        }

                                    } else {
                                        Log.d(
                                            "OverlayDebug",
                                            "canOverlay value: ${canOverlay.value}"
                                        )
                                    }
                                }) {
                                Icon(
                                    Icons.Outlined.TipsAndUpdates,
                                    tint = white,
                                    contentDescription = "Camera Icon"
                                )
                            }
                        }
                    })
                HorizontalDivider()
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(bottom = it.calculateBottomPadding())
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            when (state.value.events) {
                EventsMedicalPrescription.CAMERA -> {
                    if (permissionOk.value) {
                        CameraMedicalPrescription(
                            controller = controller,
                            context = context,
                            viewModel = viewModel
                        )
                    }
                }

                EventsMedicalPrescription.REGULAR -> {
                    EmptyData("Aqui você adiciona o sua receita e cria alertas.")
                }

                EventsMedicalPrescription.ERROR -> {
                    EmptyData(state.value.error ?: "Ops tente mais tarde")
                }

                EventsMedicalPrescription.LOADING -> {
                    Loading()
                }

                EventsMedicalPrescription.SUCCESS -> {
                    if (state.value.listMedicalPrescription.isEmpty())
                        EmptyData("Aqui você adiciona o sua receita e cria alertas.")
                    else {
                        Column(
                            modifier = Modifier
                                .padding(top = 60.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
                                .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AppText(
                                types = Types.SMALL,
                                text = "Encontramos estas informações por favor verifique se esta correto"
                            )
                            state.value.listMedicalPrescription.forEach { item ->
                                val crm = remember { mutableStateOf(item.crm) }
                                val doctor = remember { mutableStateOf(item.doctor) }
                                val duration = remember { mutableStateOf(item.duration) }
                                val qtdPerDay =
                                    remember { mutableStateOf(item.qtdPerDay.toString()) }
                                val nameOfMedications =
                                    remember { mutableStateOf(item.nameOfMedications) }
                                val description = remember { mutableStateOf(item.description) }
                                val initDate = remember { mutableStateOf("") }

                                TextField(input = doctor, label = "DR/a", placeholder = "DR/a")
                                AppSpace(SpaceType.SMALL)
                                TextField(input = crm, label = "CRM", placeholder = "CRM")
                                AppSpace(SpaceType.SMALL)
                                TextField(
                                    input = nameOfMedications,
                                    label = "Nome do medicamento",
                                    placeholder = "Nome do medicamento"
                                )
                                AppSpace(SpaceType.SMALL)
                                TextField(
                                    input = qtdPerDay,
                                    label = "Quantidade por dia",
                                    placeholder = "Quantidade por dia"
                                )
                                AppSpace(SpaceType.SMALL)
                                TextField(
                                    input = duration,
                                    label = "Duração deste remédio",
                                    placeholder = "Duração deste remédio"
                                )
                                AppSpace(SpaceType.SMALL)
                                TextArea(
                                    input = description,
                                    label = "Descrição",
                                    placeholder = "Descrição",
                                    modifier = Modifier.padding(
                                        start = 20.dp,
                                        end = 20.dp,
                                    )
                                )
                                AppSpace(SpaceType.SMALL)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    TextButton(onClick = {
                                        initDate.value = getFormattedDate()
                                    }) {
                                        AppText(
                                            text = "Hoje mesmo",
                                            types = Types.SMALL,
                                            color = blue
                                        )
                                    }
                                }
                                TextField(
                                    input = initDate,
                                    label = "Quando vou começar a tomar este remédio?",
                                    placeholder = "digite a data de inicio"
                                )
                                AppSpace(SpaceType.LARGE)
                                HorizontalDivider()
                                AppSpace(SpaceType.LARGE)
                            }
                        }
                    }
                }
            }
        }
    }
}