package com.soujava.mydoctor.presenter.screens.commons

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soujava.mydoctor.presenter.graph.PROFILE_SCREEN
import com.soujava.mydoctor.presenter.graph.REGISTER_SCREEN
import com.soujava.mydoctor.presenter.ui.theme.AppFont
import com.soujava.mydoctor.presenter.ui.theme.black
import com.soujava.mydoctor.presenter.ui.theme.lightGray
import com.soujava.mydoctor.presenter.ui.theme.white


@Composable
fun AppIconButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    tint: Color = black,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint
        )
    }
}

@Composable
fun BottomButtonCard(
    labelBtn: String,
    isLoading: Boolean,
    enabled: Boolean,
    linkContent: @Composable () -> Unit = {},
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp
                )
            )
            .background(
                black, shape = RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp
                )
            )
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AppSpace(SpaceType.MEDIUM)
        LoadingButton(
            labelBtn = labelBtn,
            isLoading = isLoading,
            enabled = enabled,
            onClick = onClick
        )
        if (linkContent != null) {
            AppSpace(SpaceType.MEDIUM)
            linkContent()
        }
        AppSpace(SpaceType.MEDIUM)
    }
}


@Composable
fun AppTextButtonSmall(
    text: String,
    colorFont: Color = black,
    onClick: () -> Unit, modifier: Modifier = Modifier
) {
    return TextButton(
        modifier = modifier,
        onClick = onClick
    ) {
        AppText(text = text, types = Types.SMALL, color = colorFont)

    }
}

@Composable
fun LoadingButton(
    labelBtn: String,
    isLoading: Boolean,
    enabled: Boolean,
    containerColor: Color = white,
    circularColor: Color = black,
    textColor: Color = black,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            disabledContainerColor = lightGray,
        ),
        shape = RoundedCornerShape(if (isLoading) 50.dp else 10.dp),
        enabled = enabled,
        modifier = Modifier
            .height(55.dp)
            .takeIf {
                isLoading
            } ?: Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        content = {
            if (isLoading) {
                CircularProgressIndicator(
                    color = circularColor,
                    modifier = Modifier.size(44.dp),
                    strokeWidth = 3.dp
                )
            } else
                AppText(
                    types = Types.BUTTON,
                    text = labelBtn,
                    color = if (enabled) textColor else black.copy(alpha = 0.5f)
                )

        }
    )
}

@Composable
fun AppDefaultButton(
    modifier: Modifier = Modifier,
    label: String,
    color: Color = white,
    containerColor: Color = black,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
        )

    ) {
        AppText(types = Types.REGULAR, text = label, color = color)
    }

}