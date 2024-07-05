package com.soujava.mydoctor.presenter.screens.videoCall

import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.ChangeCircle
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PhoneDisabled
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soujava.mydoctor.R
import com.soujava.mydoctor.presenter.screens.commons.AppIconButton
import com.soujava.mydoctor.presenter.screens.commons.CameraPreview
import com.soujava.mydoctor.presenter.ui.theme.black
import com.soujava.mydoctor.presenter.ui.theme.blue
import com.soujava.mydoctor.presenter.ui.theme.red
import com.soujava.mydoctor.presenter.ui.theme.white

@Composable
fun VideoCallScree(
    navController: NavHostController
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

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .background(white, shape = RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp)),
            painter = painterResource(id = R.drawable.doctor),
            contentDescription = null
        )
        Box(
            modifier = Modifier
                .padding(top = 90.dp, end = 10.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.TopEnd
        ) {
            CameraPreview(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .size(200.dp, 280.dp),
                controller = controller,
            )
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Row(
                modifier = Modifier
                    .padding(bottom = 50.dp)
                    .background(black.copy(alpha = 0.5f))
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AppIconButton(
                    tint = white,
                    icon = Icons.Default.Mic,
                    modifier = Modifier.background(blue, shape = RoundedCornerShape(50.dp))
                ) {}
                AppIconButton(
                    tint = white,
                    icon = Icons.Default.Camera,
                    modifier = Modifier.background(blue, shape = RoundedCornerShape(50.dp))
                ) {}
                AppIconButton(
                    tint = white,
                    icon = Icons.Default.ChangeCircle,
                    modifier = Modifier.background(blue, shape = RoundedCornerShape(50.dp))
                ) {}
                AppIconButton(
                    tint = white,
                    icon = Icons.Default.PhoneDisabled,
                    modifier = Modifier.background(red, shape = RoundedCornerShape(50.dp))
                ) {}
            }

        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopStart
        ) {
            AppIconButton(
                tint = white,
                icon = Icons.Default.ArrowBackIosNew,
                modifier = Modifier
                    .padding(top = 60.dp, start = 10.dp)
                    .background(
                    black.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(50.dp)
                )
            ) {
                navController.popBackStack()
            }
        }
    }
}