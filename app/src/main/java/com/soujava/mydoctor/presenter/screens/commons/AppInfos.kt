package com.soujava.mydoctor.presenter.screens.commons

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.soujava.mydoctor.R
import com.soujava.mydoctor.presenter.ui.theme.black
import com.soujava.mydoctor.presenter.ui.theme.green
import com.soujava.mydoctor.presenter.ui.theme.greenLight
import com.soujava.mydoctor.presenter.ui.theme.lightGray
import com.soujava.mydoctor.presenter.ui.theme.red
import com.soujava.mydoctor.presenter.ui.theme.redDark
import com.soujava.mydoctor.presenter.ui.theme.white
import kotlinx.coroutines.delay

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun FirstAccess() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(black), contentAlignment = Alignment.Center
    ) {

        //InfoFirstTime(){}
        WarningTop(
            text = "ops tente mais tarde",
            isError = true,
            onClick = {}
        )

    }


}

@Composable
fun InfoFirstTime(onClick: () -> Unit) {
    Box {
        Column(
            modifier = Modifier
                .width(350.dp)
                .padding(top = 50.dp)
                .background(white, RoundedCornerShape(10.dp))
                .padding(10.dp)
        ) {
            AppText(types = Types.TITLE, text = "Obaaa\n\nSeja bem vindo")
            HorizontalDivider(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 30.dp),
                color = lightGray
            )
            AppText(
                types = Types.SUBTITLE, text = "Neste seu primeiro acesso " +
                        "precisamos que você atualize os dados pessoais, para que o sistema funcione corretamente"
            )
            Box(
                modifier = Modifier
                    .padding(top = 30.dp)
                    .fillMaxWidth(), contentAlignment = Alignment.Center
            ) {
                AppTextButtonSmall(text = "Clique aqui para atualizar", onClick = onClick)
            }

        }
        Image(
            painter = painterResource(id = R.drawable.ballon),
            contentDescription = null,
            modifier = Modifier
                .size(120.dp)
                .offset(x = 250.dp, y = (-10).dp)
        )
    }
}


@Composable
fun Info(
    modifier: Modifier = Modifier,
    hasError: Boolean?, message: String?,
    showLinear: Boolean = false,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = 10.dp,
                end = 10.dp
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    when (hasError) {
                        null -> Color.Transparent
                        true -> red
                        else -> green
                    },
                    RoundedCornerShape(10.dp)
                ),
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .width(350.dp)
                        .padding(10.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppText(
                        types = Types.SMALL,
                        text = message ?: "ops tente mais tarde",
                        color = when (hasError) {
                            null -> Color.Transparent
                            true -> white
                            else -> black
                        }
                    )

                }
                if (showLinear)
                    Box(
                        modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        LinearDeterminateIndicator()
                    }
            }


        }
        IconButton(
            modifier = Modifier.offset(
                x = 330.dp,
                y = (-10).dp
            ),
            onClick = onClick,
            content = {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = null,
                    tint = when (hasError) {
                        null -> Color.Transparent
                        true -> white
                        else -> black
                    }
                )
            })

    }
}

@Composable
fun WarningTop(
    text: String = "",
    isError: Boolean = false,
    onClick: () -> Unit
) {


    AnimatedVisibility(
        visible = text.isNotEmpty(),
        enter = slideInHorizontally(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp)
                .clip(RoundedCornerShape(10.dp))
                .fillMaxWidth()
        ) {

            Row(
                modifier = Modifier
                    .background(if (isError) redDark else greenLight),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Spacer(modifier = Modifier.width(5.dp))
                Icon(
                    if (isError) Icons.Outlined.Warning else Icons.Outlined.Check,
                    contentDescription = null,
                    tint = lightGray
                )
                AppText(
                    types = Types.SMALL,
                    color = lightGray,
                    text = text,
                    modifier = Modifier
                        .width(250.dp)
                )
                IconButton(
                    onClick = onClick,
                ) {
                    Icon(Icons.Outlined.Close, contentDescription = null, tint = white)
                }
                Spacer(modifier = Modifier.width(10.dp))
            }
        }
    }
}