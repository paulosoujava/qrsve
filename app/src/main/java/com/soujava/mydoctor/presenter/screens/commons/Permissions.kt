package com.soujava.mydoctor.presenter.screens.commons

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.soujava.mydoctor.presenter.ui.theme.white
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Permissions(
    context: android.content.Context,
    onResult: (Boolean) -> Unit
) {
    val cameraPermissionState =
        rememberPermissionState(permission = android.Manifest.permission.CAMERA)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        coroutineScope.launch {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    val close = remember {
        mutableStateOf(true)
    }

    onResult(cameraPermissionState.status.isGranted)

    if (!cameraPermissionState.status.isGranted && close.value) {
        if (close.value)
            Dialog(onDismissRequest = {
                close.value = false
            }) {
                Column(
                    modifier = Modifier
                        .size(300.dp)
                        .background(white, RoundedCornerShape(12.dp)),

                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier.padding(top = 15.dp)) {
                        AppText(types = Types.SUBTITLE, text = "Permissão Negada")
                    }

                    HorizontalDivider(
                        modifier = Modifier
                            .padding(
                                top = 14.dp,
                                bottom = 14.dp
                            )
                    )
                    AppText(
                        types = Types.SUBTITLE, text = "Para que o aplicativo funcione" +
                                " corretamente, por favor, permita o acesso a camera.",
                        modifier = Modifier.padding(20.dp)
                    )

                    HorizontalDivider(
                        modifier = Modifier
                            .padding(
                                top = 14.dp,
                                bottom = 34.dp
                            )
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppDefaultButton(label = "Conceder permissão") {
                            coroutineScope.launch {
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                val uri = Uri.fromParts("package", context.packageName, null)
                                intent.data = uri
                                ContextCompat.startActivity(context, intent, null)
                            }
                        }
                    }

                }

            }

    }
}