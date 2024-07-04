package com.soujava.mydoctor.presenter.screens.triage


import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.soujava.mydoctor.core.generateRandomKey
import com.soujava.mydoctor.core.getFormattedDate
import com.soujava.mydoctor.domain.models.Triage
import com.soujava.mydoctor.presenter.graph.QRCODE_SCREEN
import com.soujava.mydoctor.presenter.graph.START_SCREEN
import com.soujava.mydoctor.presenter.ui.theme.AppFont
import com.soujava.mydoctor.presenter.ui.theme.black
import com.soujava.mydoctor.presenter.ui.theme.green
import com.soujava.mydoctor.presenter.ui.theme.lightGray
import com.soujava.mydoctor.presenter.ui.theme.red
import com.soujava.mydoctor.presenter.ui.theme.white
import com.soujava.mydoctor.presenter.utilPresenters.DefaultPage
import generateQRCodeBitmap
import org.koin.androidx.compose.koinViewModel


@Composable
fun TriageStepTwoScreen(navController: NavHostController) {
    Triage.patient.code = generateRandomKey()
    val bitmap = generateQRCodeBitmap(Triage.patient.code, 200)

    DefaultPage(
        onBack = {
            navController.navigate(START_SCREEN){
                popUpTo(START_SCREEN){
                    inclusive = true
                }
            }
        }
    ) {
        val viewModel: TriageViewModel = koinViewModel()
        val state = viewModel.state.collectAsState()

        val launcher =
            rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            if (state.value != TriageViewModelStateLoading) {
                Text(
                    text = "Compartilhe com um agente de saude, ao se consultar",
                    fontSize = 20.sp,
                    fontFamily = AppFont.bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(350.dp)
                )
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "QR Code",
                    modifier = Modifier.size(400.dp)
                )
            }

            when (state.value) {
                TriageViewModelStateLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            color = black,
                            modifier = Modifier.size(44.dp),
                            strokeWidth = 2.dp
                        )
                    }
                }

                TriageViewModelStateSuccess -> {
                    Text(
                        text = "Sucesso",
                        fontSize = 24.sp,
                        fontFamily = AppFont.bold,
                        color = green
                    )
                    TextButton(onClick = {
                        navController.navigate(START_SCREEN) {
                            popUpTo(START_SCREEN) {
                                inclusive = true
                            }
                        }
                    }) {
                        Text(text = "Voltar")
                    }
                }

                TriageViewModelStateError -> {
                    Text(text = "Erro ao salvar")
                }

                else -> {
                    Text(
                        text = "Por favor confira os dados abaixo:",
                        fontSize = 17.sp,
                        fontFamily = AppFont.bold,
                    )
                    Column(
                        modifier = Modifier
                            .padding(10.dp)
                            .background(lightGray, shape = RoundedCornerShape(12.dp))
                            .padding(10.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center
                    ) {
                        SimpleFont(text = "Seu resumo:", font = AppFont.italic)
                        Spacer(modifier = Modifier.height(10.dp))
                        SimpleFont("Resumo:\n\t${Triage.patient.content}")

                    }

                    Text(
                        text = "Para registrar a sua triagem\nClique no botão abaixo",
                        fontSize = 17.sp,
                        fontFamily = AppFont.bold,
                    )
                    Button(
                        onClick = {
                            Triage.patient.createAt = getFormattedDate()
                            viewModel.saveTriage(Triage)
                        },
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth()
                            .height(60.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = red,
                            contentColor = white
                        )
                    ) {
                        Text(text = "Registrar")
                    }

                }
            }

            Column(
                modifier = Modifier
                    .padding(top = 25.dp, start = 25.dp, end = 25.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(text = "Opções:", fontSize = 17.sp, fontFamily = AppFont.light)
                HorizontalDivider(
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                        .width(150.dp)
                )
                TextButton(onClick = {
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
                    Text(text = "Compartilhar clicando aqui!", fontFamily = AppFont.italic)
                }
                HorizontalDivider(
                    modifier = Modifier.padding(
                        bottom = 20.dp,
                        start = 13.dp,
                        end = 13.dp,
                        top = 15.dp
                    )
                )
                TextButton(onClick = {
                    navController.navigate(QRCODE_SCREEN)
                }) {
                    Text(text = "Acessar os QRCodes clicando aqui!", fontFamily = AppFont.italic)
                }
                HorizontalDivider(
                    modifier = Modifier.padding(
                        bottom = 20.dp,
                        start = 13.dp,
                        end = 13.dp,
                        top = 15.dp
                    )
                )
                TextButton(onClick = {
                    navController.navigate(START_SCREEN) {
                        popUpTo(START_SCREEN) {
                            inclusive = true
                        }
                    }
                }) {
                    Text(text = "Voltar par aa HOME, clique Aqui!", fontFamily = AppFont.italic)
                }
            }


        }
    }
}

@Composable
fun SimpleFont(text: String, font: FontFamily = AppFont.regular) {
    Text(text = text, fontSize = 17.sp, fontFamily = font)
}