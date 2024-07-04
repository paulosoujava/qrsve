package com.soujava.mydoctor.presenter.graph

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.soujava.mydoctor.domain.contract.ILocalRepository

import com.soujava.mydoctor.presenter.screens.access.login.LoginScreen
import com.soujava.mydoctor.presenter.screens.access.register.RegisterScreen
import com.soujava.mydoctor.presenter.screens.chronology.ChronologyScreen
import com.soujava.mydoctor.presenter.screens.history.HistoryScreen
import com.soujava.mydoctor.presenter.screens.genericMedication.GenericMedicationScreen


import com.soujava.mydoctor.presenter.screens.profile.ProfileScreen
import com.soujava.mydoctor.presenter.screens.qrcodes.ListQrcodeScreen
import com.soujava.mydoctor.presenter.screens.scanner.AnalyzeScreen
import com.soujava.mydoctor.presenter.screens.scanner.ScannerScreen
import com.soujava.mydoctor.presenter.screens.search.SearchScreen
import com.soujava.mydoctor.presenter.screens.start.StartPageScreen
import com.soujava.mydoctor.presenter.screens.triage.TriageOneScreen
import com.soujava.mydoctor.presenter.screens.triage.TriageStepTwoScreen
import com.soujava.mydoctor.presenter.ui.theme.MyDoctorTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            enableEdgeToEdge()
            val navController = rememberNavController()


            val session : ILocalRepository by inject()

            MyDoctorTheme {
                Surface {
                    NavHost(
                        navController = navController,
                        startDestination =  if(session.getProfile() !=null) START_SCREEN else LOGIN_SCREEN
                    ) {
                        composable(PROFILE_SCREEN) {
                            ProfileScreen(
                                navController = navController
                            )
                        }
                        composable(SEARCH_SCREEN) {
                            SearchScreen( navController = navController)
                        }
                        composable(CHRONOLOGY_SCREEN) {
                            ChronologyScreen( navController = navController)
                        }
                        composable(LOGIN_SCREEN) {
                            LoginScreen( navController = navController)
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
                                navController = navController
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
                                navController = navController
                            )
                        }
                        composable(TRIAGE_TWO_SCREEN) {
                            TriageStepTwoScreen(
                                navController = navController
                            )
                        }
                        composable(HISTORY_SCREEN) {
                            HistoryScreen(
                                navController = navController
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

val jsonString = """
        [
          {
            "type": "ONE_LINE_TEXT_FIELD",
            "label": "Onde dói a sua cabeça?"
          },
          {
            "type": "SLIDER",
            "label": "Intensidade da dor (0-10)",
            "min": 0,
            "max": 10
          },
          {
            "type": "RADIO",
            "label": "Tipo de dor:",
            "options": [
              "Latejante",
              "Em pressão",
              "Em pontada",
              "Contínua",
              "Outro"
            ]
          },
           {
            "type": "RADIO_GROUP",
            "label": "Tipo de dor:",
            "options": [
              "Latejante",
              "Em pressão",
              "Em pontada",
              "Contínua",
              "Outro"
            ]
          },
          {
            "type": "CHECKBOX",
            "label": "Você sente algum dos sintomas abaixo?",
            "options": [
              "Náusea",
              "Vômito",
              "Sensibilidade à luz",
              "Sensibilidade ao som",
              "Tontura",
              "Visão turva"
            ]
          },
          {
            "type": "MORE_LINE_TEXT_FIELD",
            "label": "Há algo mais que você gostaria de adicionar?"
          }
        ]
    """
