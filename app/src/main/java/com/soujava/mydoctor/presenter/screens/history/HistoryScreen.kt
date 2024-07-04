package com.soujava.mydoctor.presenter.screens.history

import android.annotation.SuppressLint
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.animation.Animatable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.soujava.mydoctor.core.takePhoto
import com.soujava.mydoctor.presenter.screens.commons.AppDefaultButton
import com.soujava.mydoctor.presenter.screens.commons.AppIconButton
import com.soujava.mydoctor.presenter.screens.commons.AppSpace
import com.soujava.mydoctor.presenter.screens.commons.AppText
import com.soujava.mydoctor.presenter.screens.commons.AppTextButtonSmall
import com.soujava.mydoctor.presenter.screens.commons.LoadingButton
import com.soujava.mydoctor.presenter.screens.commons.SpaceType
import com.soujava.mydoctor.presenter.screens.commons.Types
import com.soujava.mydoctor.presenter.screens.commons.CameraPreview
import com.soujava.mydoctor.presenter.screens.history.component.Camera
import com.soujava.mydoctor.presenter.screens.history.component.CountPage
import com.soujava.mydoctor.presenter.screens.history.component.EmptyData
import com.soujava.mydoctor.presenter.screens.history.component.ListHistory

import com.soujava.mydoctor.presenter.ui.theme.black
import com.soujava.mydoctor.presenter.ui.theme.darkGray
import com.soujava.mydoctor.presenter.ui.theme.red
import com.soujava.mydoctor.presenter.ui.theme.white
import org.koin.androidx.compose.koinViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavHostController) {

    val context = LocalContext.current
    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE or
                        CameraController.VIDEO_CAPTURE
            )
        }
    }

    val viewModel: HistoryViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()


    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = {
                            if (state.events != Events.CAMERA)
                                navController.popBackStack()
                            else
                                viewModel.actionRegular()
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

                            AnimatedVisibility(visible = state.events != Events.CAMERA) {
                                IconButton(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .background(black, shape = CircleShape)
                                        .clip(CircleShape),
                                    onClick = {
                                        viewModel.actionCamera()
                                    }) {
                                    Icon(
                                        Icons.Outlined.PhotoCamera,
                                        tint = white,
                                        contentDescription = "Camera Icon"
                                    )
                                }
                            }

                        }


                    })
                HorizontalDivider()
            }
        }
    ) {

        when (state.events) {
            Events.CAMERA -> Camera(
                controller = controller,
                context = context,
                viewModel = viewModel
            )

            Events.LOADING -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            Events.PAGES -> CountPage()

            Events.EMPTY -> EmptyData()

            Events.ERROR -> Error(state.error)

            Events.REGULAR ->  ListHistory(state.listHistory)
        }

    }
}