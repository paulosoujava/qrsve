package com.soujava.mydoctor.presenter.screens.chronology

import android.util.Log
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.soujava.mydoctor.core.takePhotoAndSaveToFile
import com.soujava.mydoctor.presenter.screens.commons.AppText
import com.soujava.mydoctor.presenter.screens.commons.BottomButtonCard
import com.soujava.mydoctor.presenter.screens.commons.CameraPreview
import com.soujava.mydoctor.presenter.screens.commons.Types
import com.soujava.mydoctor.presenter.ui.theme.black
import com.soujava.mydoctor.presenter.ui.theme.red
import com.soujava.mydoctor.presenter.ui.theme.white
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChronologyScreen(
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
    controller.isPinchToZoomEnabled = true


    val viewModel = koinViewModel<ChronologyViewModel>()
    val state = viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .background(white)
                    .fillMaxWidth()
                    .padding(
                        top = 20.dp,
                    )
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

                        Column{
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AppText(types = Types.REGULAR, text = "Cronologia de exames")
                                if (state.value.events != ChronologyType.CAMERA)
                                    AnimatedVisibility(visible = true) {
                                        IconButton(
                                            modifier = Modifier
                                                .size(30.dp)
                                                .background(black, shape = CircleShape)
                                                .clip(CircleShape),
                                            onClick = {
                                                viewModel.openCamera()
                                            }) {
                                            Icon(
                                                Icons.Outlined.PhotoCamera,
                                                tint = white,
                                                contentDescription = "Camera Icon"
                                            )
                                        }
                                    }
                            }
                            HorizontalDivider(
                                modifier = Modifier
                                    .padding(
                                        top = 9.dp,
                                    )
                            )
                        }
                    })
            }
        },
        bottomBar = {
            if (state.value.events == ChronologyType.CAMERA)
                BottomButtonCard(
                    labelBtn = "Procesar",
                    isLoading = state.value.events == ChronologyType.LOADING,
                    enabled = state.value.paths.isNotEmpty()
                ) {
                    viewModel.process()
                }
        }
    ) {

        when (state.value.events) {
            ChronologyType.LOADING -> {
                Container {
                    CircularProgressIndicator()
                }

            }

            ChronologyType.SUCCESS -> {
                Container(
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    AppText(
                        types = Types.REGULAR,
                        text = state.value.success ?: ""
                    )
                }
            }

            ChronologyType.REGULAR -> {
                Container {
                    AppText(
                        types = Types.REGULAR,
                        text = "Clique no icone acima e envie os documento que você deseja analisar.",
                        color = black.copy(alpha = 0.5f),
                        modifier = Modifier.padding( start = 20.dp, end = 20.dp)
                    )
                }
            }

            ChronologyType.ERROR -> {
                Container {
                    AppText(
                        types = Types.REGULAR,
                        text = state.value.error ?: "",
                        color = black.copy(alpha = 0.5f),
                        modifier = Modifier.padding(top = 200.dp, start = 20.dp, end = 20.dp)
                    )
                }
            }

            ChronologyType.CAMERA -> {
                if (permissionOk.value)
                    LazyColumn(
                        modifier = Modifier.padding(it),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        item {
                            Row(
                                modifier = Modifier
                                    .padding(top = 20.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AppText(
                                    types = Types.SMALL,
                                    text = "Total de documentos: ${state.value.paths.size}"
                                )
                            }

                        }
                        item {
                            LazyRow(
                                horizontalArrangement = Arrangement.Start,
                                modifier = Modifier
                                    .padding(10.dp)
                                    .fillMaxWidth()
                            ) {

                                items(state.value.paths) { image ->
                                    Box(
                                        modifier = Modifier.padding(8.dp),
                                        contentAlignment = Alignment.TopEnd
                                    ) {
                                        Image(
                                            painter = rememberAsyncImagePainter(model = image),
                                            contentDescription = "Captured Image",
                                            contentScale = androidx.compose.ui.layout.ContentScale.FillBounds,
                                            modifier = Modifier
                                                .background(
                                                    white,
                                                    shape = RoundedCornerShape(10.dp)
                                                )
                                                .border(
                                                    1.dp,
                                                    black,
                                                    shape = RoundedCornerShape(10.dp)
                                                )
                                                .clip(RoundedCornerShape(10.dp))
                                                .size(80.dp)
                                        )

                                        IconButton(
                                            modifier = Modifier.offset(
                                                x = 15.dp,
                                                y = (-15).dp
                                            ),
                                            onClick = {
                                                viewModel.removePath(image)
                                            }
                                        ) {
                                            Icon(
                                                Icons.Default.Close,
                                                contentDescription = null,
                                                tint = red
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        item {
                            Box(
                                modifier = Modifier
                                    .padding(top = 80.dp)
                                    .padding(start = 80.dp, end = 80.dp)
                                    .fillMaxWidth(), contentAlignment = Alignment.Center
                            ) {
                                CameraPreview(
                                    controller = controller,
                                    modifier = Modifier.size(280.dp)
                                )
                            }

                        }
                        item {
                            Button(onClick = {
                                takePhotoAndSaveToFile(
                                    controller = controller,
                                    context = context,
                                    onPhotoSaved = { path ->
                                        viewModel.addImageWithPath(path)
                                    },
                                    onError = {
                                        Log.e("Camera", "Error taking photo: $it")
                                    }
                                )
                            }) {
                                Text(text = "Tirar foto")
                            }
                        }
                    }
            }
        }

    }
}

@Composable
fun Container(
    modifier: Modifier = Modifier,
    component: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .padding(top = 120.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) { component() }
}