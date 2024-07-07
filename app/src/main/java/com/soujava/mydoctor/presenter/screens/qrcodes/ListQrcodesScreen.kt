package com.soujava.mydoctor.presenter.screens.qrcodes

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Whatsapp
import androidx.compose.material.icons.outlined.ArrowForwardIos
import androidx.compose.material.icons.outlined.QrCode
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.soujava.mydoctor.core.copyToClipboard
import com.soujava.mydoctor.presenter.ui.theme.AppFont
import com.soujava.mydoctor.presenter.ui.theme.black
import com.soujava.mydoctor.presenter.ui.theme.darkPrimaryColor
import com.soujava.mydoctor.presenter.ui.theme.lightGray
import com.soujava.mydoctor.presenter.ui.theme.white
import com.soujava.mydoctor.presenter.utilPresenters.DefaultPage
import generateQRCodeBitmap
import org.koin.androidx.compose.koinViewModel


@Composable
fun ListQrcodeScreen(
    navController: NavHostController
) {
    DefaultPage(
        onBack = {
            navController.popBackStack()
        }
    ) {
        val viewModel: ListQrcodesViewModel = koinViewModel()
        val state = viewModel.state.collectAsState()

        val launcher =
            rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

        when (state.value) {
            is ListQrcodesState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Error: ${(state.value as ListQrcodesState.Error).error}")
                    Button(onClick = {
                        navController.popBackStack()
                    }) {
                        Text(text = "Home")
                    }
                }
            }
            is ListQrcodesState.Success -> {
                val qrcodes = (state.value as ListQrcodesState.Success).qrcodes
                if (qrcodes.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Nenhum Qrcode encontrado")
                    }
                } else
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        items((state.value as ListQrcodesState.Success).qrcodes) { item ->
                            val show = remember { mutableStateOf(false) }
                            Column(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .background(
                                        lightGray,
                                        shape = RoundedCornerShape(6.dp)
                                    )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .background(
                                            lightGray,
                                            shape = RoundedCornerShape(6.dp)
                                        )
                                        .fillMaxWidth()
                                        .padding(20.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        Icons.Outlined.QrCode,
                                        contentDescription = null,
                                        modifier = Modifier.size(34.dp)
                                    )
                                    Column(modifier = Modifier
                                        .clickable { show.value = !show.value }
                                        .weight(.9f)) {
                                        Text(
                                            text = item.initialResume,
                                            fontSize = 14.sp,
                                            fontFamily = AppFont.bold,
                                            textAlign = TextAlign.Start,

                                            )
                                        Text(
                                            text = item.createAt,
                                            fontSize = 12.sp,
                                            fontFamily = AppFont.light,
                                            textAlign = TextAlign.Start,
                                        )

                                    }

                                    Image(
                                        if (show.value) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp),
                                    )
                                }
                                AnimatedVisibility(visible = show.value) {
                                    val bitmap = generateQRCodeBitmap(item.code, 200)
                                    Column(
                                        modifier = Modifier.padding(10.dp)
                                    ) {

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ){
                                            IconButton(onClick = {
                                                val intent = Intent(Intent.ACTION_SEND)
                                                intent.type = "text/plain"
                                                intent.putExtra(
                                                    Intent.EXTRA_TEXT, "Compartilhe este código com um agente de saude\n" +
                                                            "123123"
                                                )
                                                intent.`package` = "com.whatsapp" // Especifica o pacote do WhatsApp

                                                // Lança a intent para compartilhamento
                                                launcher.launch(Intent.createChooser(intent, "Compartilhar via"))

                                            }) {
                                                Icon(
                                                    Icons.Filled.Whatsapp,
                                                    contentDescription = null
                                                )
                                            }
                                            Text(
                                                text = "Seu Código: ${item.code}",
                                                fontSize = 13.sp,
                                                fontFamily = AppFont.bold,
                                                modifier = Modifier.padding(start = 10.dp).clickable {
                                                    copyToClipboard(
                                                        context = navController.context,
                                                        text = item.uid
                                                    )
                                                }   )
                                        }
                                        HorizontalDivider(
                                            modifier = Modifier
                                                .padding(bottom = 20.dp, top = 20.dp)
                                        )

                                        Text(
                                            text = item.content,
                                            fontSize = 12.sp,
                                            fontFamily = AppFont.light,
                                            textAlign = TextAlign.Start,
                                        )
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Image(
                                                bitmap = bitmap.asImageBitmap(),
                                                contentDescription = "QR Code",
                                                modifier = Modifier.size(200.dp)
                                            )
                                        }
                                                                            }
                                }
                            }
                        }
                    }
            }
            is ListQrcodesState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

                    CircularProgressIndicator(
                        color = black,
                        modifier = Modifier.size(44.dp),
                        strokeWidth = 2.dp
                    )
                }
            }
        }
    }
}