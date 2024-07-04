package com.soujava.mydoctor.presenter.screens.scanner

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.Camera
import androidx.compose.material.icons.outlined.DocumentScanner
import androidx.compose.material.icons.outlined.GeneratingTokens
import androidx.compose.material.icons.outlined.QrCode
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.soujava.mydoctor.core.startQRScanner
import com.soujava.mydoctor.presenter.graph.ANALYZE_SCREEN
import com.soujava.mydoctor.presenter.graph.OTHER_APP_SCREEN
import com.soujava.mydoctor.presenter.graph.QRCODE_SCANNER_SCREEN
import com.soujava.mydoctor.presenter.screens.commons.AppIconButton
import com.soujava.mydoctor.presenter.screens.commons.BottomButtonCard
import com.soujava.mydoctor.presenter.screens.commons.LoadingButton
import com.soujava.mydoctor.presenter.ui.theme.AppFont
import com.soujava.mydoctor.presenter.ui.theme.black
import com.soujava.mydoctor.presenter.ui.theme.lightGray
import com.soujava.mydoctor.presenter.ui.theme.white
import org.koin.androidx.compose.koinViewModel

object ScannerScreen {
    var analyze: String = ""
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannerScreen(
    context: Activity,
    navController: NavHostController,
) {
    val viewModel: ScannerViewModel = koinViewModel()
    val state = viewModel.state.collectAsState()

    var code = remember { mutableStateOf("") }

    val qrScanLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val intentResult: IntentResult =
                IntentIntegrator.parseActivityResult(result.resultCode, result.data)
            if (intentResult.contents != null) {
                // O QR code foi lido com sucesso
                code.value = intentResult.contents
            }
        }
    val iconsDoctor = mapOf(
        OTHER_APP_SCREEN to Icons.Outlined.Camera,
    )

    Scaffold(
        modifier = Modifier.padding(top = 20.dp),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Outlined.ArrowBackIosNew,
                            contentDescription = null
                        )
                    }
                },
                title = {
                    Column(
                        modifier = Modifier
                            .padding(top =30.dp, start = 12.dp, end = 12.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Doctor dashboard",
                                color = black,
                                fontSize = 17.sp,
                                fontFamily = AppFont.bold,
                                modifier = Modifier.weight(2f)
                            )
                            iconsDoctor.forEach { (screen, icon) ->
                                AppIconButton(icon = icon) {
                                    navController.navigate(screen)
                                }
                            }
                        }
                        HorizontalDivider(
                            modifier = Modifier
                                .padding(
                                    top = 4.dp,
                                    bottom = 30.dp
                                ),
                            color = lightGray
                        )
                    }
                })
        },
        bottomBar = {
            if (state.value is ScannerState.Success)
                BottomButtonCard(
                    labelBtn = "Análise",
                    isLoading = state.value is ScannerState.Loading,
                    enabled = state.value !is ScannerState.Loading
                ) {
                    navController.navigate(ANALYZE_SCREEN)
                }
        }
    ) {

        when (state.value) {
            is ScannerState.Idle -> {
                Column(
                    modifier = Modifier
                        .padding(top = 96.dp)
                        .padding(it)
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Clique no botão abaixo para escanear o QR Code",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        fontFamily = AppFont.light,
                        modifier = Modifier.width(200.dp)
                    )
                    Spacer(modifier = Modifier.height(22.dp))
                    Button(
                        modifier = Modifier
                            .padding(16.dp)
                            .size(140.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = black,
                            contentColor = white
                        ),
                        onClick = {
                            startQRScanner(context, qrScanLauncher)
                        }) {
                        Icon(
                            Icons.Outlined.QrCodeScanner,
                            contentDescription = null,
                            modifier = Modifier.size(140.dp)
                        )
                    }


                    Row(
                        modifier = Modifier
                            .padding(top = 56.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(
                            modifier = Modifier.width(120.dp),
                            thickness = 1.dp,
                            color = black
                        )
                        Text(
                            text = "OU",
                            color = black,
                            fontFamily = AppFont.bold,
                            textAlign = TextAlign.Center,
                            fontSize = 24.sp,
                            modifier = Modifier
                                .weight(2f)
                                .padding(
                                    start = 8.dp,
                                    end = 8.dp
                                )
                        )
                        HorizontalDivider(
                            modifier = Modifier.width(120.dp),
                            thickness = 1.dp,
                            color = black
                        )
                    }
                    OutlinedTextField(
                        label = {
                            Text(text = "Code")
                        },
                        placeholder = {
                            Text(text = "digite o código")
                        },
                        value = code.value,
                        onValueChange = {
                            code.value
                        })
                    Box(
                        modifier = Modifier
                            .padding(top = 30.dp, end = 30.dp)
                            .fillMaxWidth(), contentAlignment = Alignment.TopEnd
                    ) {
                        FloatingActionButton(
                            containerColor = black,
                            contentColor = white,
                            onClick = {
                                viewModel.scanner(code.value)
                            }) {
                            Icon(Icons.Outlined.GeneratingTokens, contentDescription = null)
                        }
                    }


                }
            }

            is ScannerState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        color = black,
                        strokeWidth = 2.dp
                    )
                }
            }

            is ScannerState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = (state.value as ScannerState.Error).error)
                }
            }

            is ScannerState.Success -> {
                val item = (state.value as ScannerState.Success).qrcodes
                ScannerScreen.analyze = item.content
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(
                        modifier = Modifier
                            .padding(top = 120.dp, bottom = 180.dp, start = 20.dp, end = 20.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = item.content,
                            fontSize = 12.sp,
                            fontFamily = AppFont.light,
                            textAlign = TextAlign.Start,
                        )
                    }
                }
            }
        }
    }
}

