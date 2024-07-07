package com.soujava.mydoctor.presenter.screens.medicalPrescription.component

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Camera
import android.util.Log
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.soujava.mydoctor.core.takePhotoAndSaveToFile
import com.soujava.mydoctor.presenter.screens.commons.AppText
import com.soujava.mydoctor.presenter.screens.commons.BottomButtonCard
import com.soujava.mydoctor.presenter.screens.commons.CameraPreview
import com.soujava.mydoctor.presenter.screens.commons.Types
import com.soujava.mydoctor.presenter.screens.history.Events
import com.soujava.mydoctor.presenter.screens.history.HistoryViewModel
import com.soujava.mydoctor.presenter.screens.medicalPrescription.EventsMedicalPrescription
import com.soujava.mydoctor.presenter.screens.medicalPrescription.MedicalPrescriptionViewModel
import com.soujava.mydoctor.presenter.ui.theme.black
import com.soujava.mydoctor.presenter.ui.theme.red
import com.soujava.mydoctor.presenter.ui.theme.white


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CameraMedicalPrescription(
    controller: LifecycleCameraController,
    context: Context,
    viewModel: MedicalPrescriptionViewModel
) {

    val state = viewModel.state.collectAsState()
    Scaffold(
        bottomBar = {

            BottomButtonCard(
                labelBtn = "Procesar",
                isLoading = state.value.events == EventsMedicalPrescription.LOADING,
                enabled = state.value.paths.isNotEmpty()
            ) {
                viewModel.process()
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier.padding(top = 80.dp, bottom = 150.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            item {
                Row(
                    modifier = Modifier
                        .padding(top = 20.dp, end = 10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppText(
                        types = Types.SMALL,
                        text = "Num.: ${state.value.paths.size}"
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

