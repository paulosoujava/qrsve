package com.soujava.mydoctor.presenter.graph

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.soujava.mydoctor.domain.contract.ILocalRepository


import com.soujava.mydoctor.presenter.screens.access.login.LoginScreen
import com.soujava.mydoctor.presenter.screens.access.register.RegisterScreen
import com.soujava.mydoctor.presenter.screens.chronology.ChronologyScreen
import com.soujava.mydoctor.presenter.screens.commons.Permissions
import com.soujava.mydoctor.presenter.screens.history.HistoryScreen
import com.soujava.mydoctor.presenter.screens.genericMedication.GenericMedicationScreen
import com.soujava.mydoctor.presenter.screens.medicalPrescription.MedicalPrescriptionScreen


import com.soujava.mydoctor.presenter.screens.profile.ProfileScreen
import com.soujava.mydoctor.presenter.screens.qrcodes.ListQrcodeScreen
import com.soujava.mydoctor.presenter.screens.scanner.AnalyzeScreen
import com.soujava.mydoctor.presenter.screens.scanner.ScannerScreen
import com.soujava.mydoctor.presenter.screens.search.SearchScreen
import com.soujava.mydoctor.presenter.screens.start.StartPageScreen
import com.soujava.mydoctor.presenter.screens.triage.TriageOneScreen
import com.soujava.mydoctor.presenter.screens.triage.TriageStepTwoScreen
import com.soujava.mydoctor.presenter.screens.videoCall.VideoCallScree
import com.soujava.mydoctor.presenter.ui.theme.MyDoctorTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            enableEdgeToEdge()
            val navController = rememberNavController()

            val session: ILocalRepository by inject()

            MyDoctorTheme {
                Surface {

                    val permissionOk = remember {
                        mutableStateOf(false)
                    }

                    Permissions(context = this) {
                        permissionOk.value = it
                    }

                    NavHost(
                        navController = navController,
                        startDestination = if (session.getProfile() != null) START_SCREEN else LOGIN_SCREEN
                    ) {

                        composable(VIDEO_CALL) {
                            VideoCallScree(navController = navController)
                        }
                        composable(MEDICAL_PRESCRIPTION_SCREEN) {
                            MedicalPrescriptionScreen(
                                navController = navController,
                                permissionOk = permissionOk)
                        }

                        composable(PROFILE_SCREEN) {
                            ProfileScreen(
                                navController = navController
                            )
                        }
                        composable(SEARCH_SCREEN) {
                            SearchScreen(navController = navController)
                        }
                        composable(CHRONOLOGY_SCREEN) {
                            ChronologyScreen(
                                navController = navController,
                                permissionOk = permissionOk
                            )
                        }
                        composable(LOGIN_SCREEN) {
                            LoginScreen(navController = navController)
                        }
                        composable(START_SCREEN) {
                            StartPageScreen(
                                application = application,
                                navController = navController
                            )
                        }
                        composable(ANALYZE_SCREEN) {
                            AnalyzeScreen(navController = navController)
                        }
                        composable(REGISTER_SCREEN) {
                            RegisterScreen(navHostController = navController)
                        }
                        composable(OTHER_APP_SCREEN) {
                            GenericMedicationScreen(
                                navController = navController, permissionOk = permissionOk
                            )
                        }
                        composable(TRIAGE_ONE_SCREEN) {
                            TriageOneScreen(
                                navController = navController
                            )
                        }
                        composable(QRCODE_SCANNER_SCREEN) {
                            ScannerScreen(
                                context = this@MainActivity,
                                navController = navController, permissionOk = permissionOk
                            )
                        }
                        composable(TRIAGE_TWO_SCREEN) {
                            TriageStepTwoScreen(
                                navController = navController
                            )
                        }
                        composable(HISTORY_SCREEN) {
                            HistoryScreen(
                                navController = navController, permissionOk = permissionOk
                            )
                        }
                        composable(QRCODE_SCREEN) {
                            ListQrcodeScreen(
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }



}

