package com.soujava.mydoctor.presenter

import android.app.Application
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.soujava.mydoctor.core.generateRandomKey
import com.soujava.mydoctor.data.di.appModule
import com.soujava.mydoctor.data.repositories.AuthImpl
import com.soujava.mydoctor.domain.contract.IAuthentication
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }

//        val presenter : IAuthentication by inject()

    }
}
