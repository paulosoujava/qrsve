package com.soujava.mydoctor.presenter.screens.genericMedication


import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.soujava.mydoctor.core.takePhoto

import com.soujava.mydoctor.presenter.screens.commons.AppDefaultButton
import com.soujava.mydoctor.presenter.screens.commons.AppSpace
import com.soujava.mydoctor.presenter.screens.commons.AppText
import com.soujava.mydoctor.presenter.screens.commons.AppTextButtonSmall
import com.soujava.mydoctor.presenter.screens.commons.CameraPreview
import com.soujava.mydoctor.presenter.screens.commons.LoadingButton
import com.soujava.mydoctor.presenter.screens.commons.Permissions
import com.soujava.mydoctor.presenter.screens.commons.SpaceType
import com.soujava.mydoctor.presenter.screens.commons.Types
import com.soujava.mydoctor.presenter.ui.theme.AppFont

import com.soujava.mydoctor.presenter.ui.theme.black
import com.soujava.mydoctor.presenter.ui.theme.darkGray
import com.soujava.mydoctor.presenter.ui.theme.red
import com.soujava.mydoctor.presenter.ui.theme.white
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun GenericMedicationScreen(navController: NavHostController) {

    val context = LocalContext.current
    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE or
                        CameraController.VIDEO_CAPTURE
            )
        }
    }

    val viewModel: MedicationViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()
    val bitMaps = viewModel.bitmaps.collectAsState()
    var showCamera by remember {
        mutableStateOf(false)
    }

    val permissionOk = remember {
        mutableStateOf(false)
    }

    Permissions(context = context){
        permissionOk.value = it
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .background(white)
                    .padding(top = 20.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = {
                            navController.popBackStack()
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    title = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 12.dp, end = 12.dp, top = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Cosulta de genéricos",
                                    color = black,
                                    fontSize = 16.sp,
                                    fontFamily = AppFont.bold,
                                    modifier = Modifier.weight(2f)
                                )

                                IconButton(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .background(black, shape = CircleShape)
                                        .clip(CircleShape),
                                    onClick = {
                                        showCamera = true
                                    }) {
                                    Icon(
                                        Icons.Outlined.PhotoCamera,
                                        tint = white,
                                        contentDescription = "Camera Icon"
                                    )
                                }
                            }
                            HorizontalDivider(
                                modifier = Modifier
                                    .padding(
                                        top = 14.dp,
                                    ),
                            )
                        }
                    })
            }
        }
    ) {
        Box {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(
                        start = 20.dp,
                        top = 160.dp,
                        end = 20.dp,
                        bottom = it.calculateBottomPadding()
                    )
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                if (permissionOk.value) {
                    if (showCamera && !state.loading) {
                        CameraPreview(
                            modifier = Modifier
                                .size(300.dp, 350.dp)
                                .clip(RoundedCornerShape(10.dp)),
                            controller = controller,
                        )

                        AppDefaultButton(label = "Tirar foto") {
                            takePhoto(
                                controller = controller,
                                context = context,
                                onPhotoTaken = {
                                    showCamera = false
                                    viewModel.onTakePhoto(it)
                                }
                            )
                        }
                    }
                }

                if (bitMaps.value.isNotEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(model = bitMaps.value[0]),
                        contentDescription = "Captured Image",
                        contentScale = androidx.compose.ui.layout.ContentScale.FillBounds,
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .background(white, shape = RoundedCornerShape(20.dp))
                            .border(1.dp, black, shape = RoundedCornerShape(20.dp))
                            .clip(RoundedCornerShape(20.dp))
                            .size(280.dp)
                    )
                    AppSpace(SpaceType.MEDIUM)
                    LoadingButton(
                        labelBtn = "Verificar",
                        isLoading = state.loading,
                        enabled = true,
                        containerColor = red,
                        textColor = white,
                        circularColor = white,
                        onClick = {
                            viewModel.process(
                                bitMaps.value[0]
                            )
                        })
                    AppSpace(SpaceType.MEDIUM)
                    AppTextButtonSmall(text = "Tirar outra foto", onClick = {
                        showCamera = true
                        viewModel.remove(bitMaps.value[0])
                    })
                }


                AnimatedVisibility(visible = state.content.isNotEmpty()) {
                    AppText(types = Types.REGULAR, text = state.content)
                }


            }
        }
        if (state.content.isEmpty() && !state.loading && !showCamera && bitMaps.value.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(start = 30.dp, end = 20.dp, bottom = 20.dp)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                AppText(
                    types = Types.REGULAR,
                    color = darkGray,
                    text = "Clique no icone da camera acima, e envie a imagem do medicamento " +
                            "que deseja consultar"
                )
            }
        }
    }
}
