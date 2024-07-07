package com.soujava.mydoctor.presenter.screens.medicalPrescription

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity


class OverlayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RememberMedication{
                finish()
            }
        }
    }
}