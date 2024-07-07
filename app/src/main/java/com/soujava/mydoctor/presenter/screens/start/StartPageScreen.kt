package com.soujava.mydoctor.presenter.screens.start

import android.app.Application
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.automirrored.sharp.KeyboardArrowRight
import androidx.compose.material.icons.outlined.AttachFile
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Camera
import androidx.compose.material.icons.outlined.DocumentScanner
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.HistoryToggleOff
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.QrCode
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.soujava.mydoctor.core.getFormattedDate
import com.soujava.mydoctor.domain.models.Triage
import com.soujava.mydoctor.presenter.graph.CHRONOLOGY_SCREEN
import com.soujava.mydoctor.presenter.graph.HISTORY_SCREEN
import com.soujava.mydoctor.presenter.graph.MEDICAL_PRESCRIPTION_SCREEN

import com.soujava.mydoctor.presenter.graph.OTHER_APP_SCREEN
import com.soujava.mydoctor.presenter.graph.PROFILE_SCREEN
import com.soujava.mydoctor.presenter.graph.QRCODE_SCANNER_SCREEN
import com.soujava.mydoctor.presenter.graph.QRCODE_SCREEN
import com.soujava.mydoctor.presenter.graph.SEARCH_SCREEN
import com.soujava.mydoctor.presenter.graph.VIDEO_CALL
import com.soujava.mydoctor.presenter.screens.commons.AppDefaultButton
import com.soujava.mydoctor.presenter.screens.commons.AppIconButton
import com.soujava.mydoctor.presenter.screens.commons.AppSpace
import com.soujava.mydoctor.presenter.screens.commons.AppText
import com.soujava.mydoctor.presenter.screens.commons.AppTextButtonSmall
import com.soujava.mydoctor.presenter.screens.commons.InfoFirstTime
import com.soujava.mydoctor.presenter.screens.commons.SpaceType
import com.soujava.mydoctor.presenter.screens.commons.TextArea
import com.soujava.mydoctor.presenter.screens.commons.Types
import com.soujava.mydoctor.presenter.ui.theme.AppFont
import com.soujava.mydoctor.presenter.ui.theme.black
import com.soujava.mydoctor.presenter.ui.theme.green
import com.soujava.mydoctor.presenter.ui.theme.lightGray
import com.soujava.mydoctor.presenter.ui.theme.red
import com.soujava.mydoctor.presenter.ui.theme.white
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartPageScreen(
    application: Application,
    navController: NavHostController
) {

    /*  val voiceToTextParser by lazy {
          VoiceToTextParser(application)
      }*/


    val viewModel: StartViewModel = koinViewModel()

    /*var canRecord by remember { mutableStateOf(false) }

    val recordAudioLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            canRecord = isGranted
        }
    )

    LaunchedEffect(key1 = recordAudioLauncher) {
        recordAudioLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
    }*/

    // val state by voiceToTextParser.state.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    var typed = remember { mutableStateOf("") }
    var hasError by remember { mutableStateOf(false) }

    val profile = viewModel.getProfile()
    val flagShow = remember { mutableStateOf(false) }


    val icons = mapOf(
        QRCODE_SCREEN to Icons.Outlined.QrCode,
        SEARCH_SCREEN to Icons.Outlined.Search,
        PROFILE_SCREEN to Icons.Outlined.Person,
        HISTORY_SCREEN to Icons.Outlined.History,

    )
    val icons2 = mapOf(
        CHRONOLOGY_SCREEN to Icons.Outlined.HistoryToggleOff,
        VIDEO_CALL to Icons.Outlined.Call,
        MEDICAL_PRESCRIPTION_SCREEN to Icons.Outlined.AttachFile,
    )

    val scope = rememberCoroutineScope()
    val keyboard = LocalSoftwareKeyboardController.current
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden,
            skipHiddenState = false,
        )
    )


    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        containerColor = white,
        contentColor = white,
        sheetContainerColor = black,
        sheetContent = {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
                    .background(black, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            ) {
                AppText(
                    text = "Sair do app",
                    types = Types.TITLE,
                    color = white
                )
                HorizontalDivider(
                    modifier = Modifier
                        .padding(
                            top = 10.dp,
                            end = 30.dp,
                            bottom = 30.dp
                        )
                )
                Box(modifier = Modifier.padding(20.dp), contentAlignment = Alignment.Center) {
                    AppText(
                        text = "Desejas realmente sair do app, finalizando a sessão?",
                        types = Types.SUBTITLE,
                        color = white
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppTextButtonSmall(
                        text = "NÃO", onClick = {
                            scope.launch {
                                scaffoldState.bottomSheetState.hide()
                            }
                        },
                        colorFont = black,
                        modifier = Modifier
                            .width(100.dp)
                            .background(
                                color = green,
                                shape = RoundedCornerShape(10.dp)
                            )
                    )
                    AppTextButtonSmall(
                        text = "SIM", onClick = {
                            viewModel.logout(
                                navController = navController
                            )
                        },
                        colorFont = white,
                        modifier = Modifier
                            .width(100.dp)
                            .background(
                                color = red,
                                shape = RoundedCornerShape(10.dp)
                            )
                    )
                }
                AppSpace(SpaceType.LARGE)
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {

                if (profile != null) {
                    Column(
                        modifier = Modifier
                            .padding(top = 60.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top =20.dp, start = 12.dp, end = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Text(
                                text = "QRSave",
                                color = black,
                                fontSize = 24.sp,
                                fontFamily = AppFont.bold,
                                modifier = Modifier.weight(2f)
                            )

                                AppIconButton(icon = Icons.Outlined.DocumentScanner) {
                                    navController.navigate(QRCODE_SCANNER_SCREEN)
                                }

                        }

                        HorizontalDivider(
                            modifier = Modifier
                                .padding(
                                    top = 4.dp,
                                    start = 30.dp,
                                    end = 30.dp,
                                    bottom = 30.dp
                                ),
                            color = lightGray
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            icons.forEach { (screen, icon) ->
                                AppIconButton(icon = icon) {
                                    navController.navigate(screen)
                                }
                            }

                        }
                        Row(
                            modifier = Modifier.padding(top = 20.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            icons2.forEach { (screen, icon) ->
                                AppIconButton(icon = icon) {
                                    navController.navigate(screen)
                                }
                            }

                            AppIconButton(icon = Icons.AutoMirrored.Outlined.ExitToApp) {
                                scope.launch {
                                    scaffoldState.bottomSheetState.expand()
                                }
                            }
                        }
                        Column(
                            modifier = Modifier
                                .padding(top = 60.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AppText(
                                    modifier = Modifier.width(150.dp),
                                    types = Types.TITLE, text = "Triage"
                                )

                            }

                            AppText(
                                types = Types.SUBTITLE,
                                text = "${getFormattedDate()}",
                                color = black.copy(alpha = 0.5f)
                            )
                            TextArea(
                                modifier = Modifier.padding(start = 30.dp, end = 30.dp),
                                hasError = hasError,
                                input = typed,
                                label = "Diga-me o que sentes",
                                placeholder = "de o máximo de informaçõs qeu puder",
                                imeAction = ImeAction.Done,
                                onDone = {
                                    if (typed.value.isNotEmpty()) {
                                        Triage.patient.initialResume = typed.value
                                        viewModel.searchKeys(
                                            typed.value,
                                            navController,
                                        )
                                        keyboard?.hide()
                                    } else {
                                        hasError = true
                                    }
                                }
                            )



                            AnimatedVisibility(visible = hasError) {
                                Text(
                                    text = "Preencha o campo acima",
                                    color = red,
                                    fontFamily = AppFont.bold,
                                    fontSize = 13.sp,
                                    modifier = Modifier
                                        .padding(start = 35.dp)
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.Start
                                )

                            }

                            Row(
                                modifier = Modifier
                                    .padding(
                                        top = 10.dp,
                                        end = 30.dp
                                    )
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                FloatingActionButton(
                                    containerColor = black,
                                    onClick = {
                                        if (uiState !is UiStateMain.Loading)
                                            if (typed.value.isNotEmpty()) {
                                                Triage.patient.initialResume = typed.value
                                                viewModel.searchKeys(
                                                    typed.value,
                                                    navController,
                                                )
                                                keyboard?.hide()
                                            } else {
                                                hasError = true
                                            }
                                    }) {
                                    if (uiState is UiStateMain.Loading) {
                                        CircularProgressIndicator(
                                            color = white,
                                            modifier = Modifier.size(26.dp),
                                            strokeWidth = 2.dp
                                        )
                                    } else
                                        Icon(
                                            Icons.AutoMirrored.Sharp.KeyboardArrowRight,
                                            contentDescription = null,
                                            tint = white
                                        )
                                }
                            }
                        }

                    }

                } else {
                    AppText(
                        types = Types.TITLE,
                        text = "Ops, obtivemos um erro drasticooo, tente mais tarde."
                    )
                }

            }
            if (profile?.name.isNullOrBlank() && !flagShow.value)
                Box(
                    modifier = Modifier
                        .background(black.copy(alpha = 0.9f))
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    InfoFirstTime {
                        navController.navigate(PROFILE_SCREEN)
                    }
                }

        }
    }
}


