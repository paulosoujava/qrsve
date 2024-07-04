package com.soujava.mydoctor.presenter.utilPresenters

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.soujava.mydoctor.presenter.screens.triage.HeaderTitle
import com.soujava.mydoctor.presenter.ui.theme.AppFont
import com.soujava.mydoctor.presenter.ui.theme.accentColor
import com.soujava.mydoctor.presenter.ui.theme.black
import com.soujava.mydoctor.presenter.ui.theme.darkPrimaryColor
import com.soujava.mydoctor.presenter.ui.theme.white

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultPage(
    onBack: () -> Unit ,
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .padding(top = 60.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.Outlined.ArrowBackIosNew,
                        contentDescription = null,
                        tint = black
                    )
                }
                Text(text = "Voltar",
                    color = black,
                    fontFamily = AppFont.bold,
                    fontSize = 24.sp,
                    modifier = Modifier.clickable {
                        onBack()
                    })
            }
        }
    ) {
        Column(
            modifier = Modifier.padding(20.dp).padding(it)
        ) {
            content()
        }
    }
}