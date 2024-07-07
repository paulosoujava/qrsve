package com.soujava.mydoctor.data.di

import com.soujava.mydoctor.data.repositories.ApiImpl
import com.soujava.mydoctor.data.repositories.AuthImpl
import com.soujava.mydoctor.data.repositories.FirestoreImpl
import com.soujava.mydoctor.data.repositories.SessionPreferenceImpl
import com.soujava.mydoctor.domain.contract.IApi
import com.soujava.mydoctor.domain.contract.IAuthentication
import com.soujava.mydoctor.domain.contract.IExternalRepository
import com.soujava.mydoctor.domain.contract.ILocalRepository
import com.soujava.mydoctor.presenter.screens.access.login.LoginViewModel
import com.soujava.mydoctor.presenter.screens.access.register.RegisterViewModel
import com.soujava.mydoctor.presenter.screens.chronology.ChronologyViewModel
import com.soujava.mydoctor.presenter.screens.genericMedication.MedicationViewModel
import com.soujava.mydoctor.presenter.screens.history.HistoryViewModel
import com.soujava.mydoctor.presenter.screens.medicalPrescription.MedicalPrescriptionViewModel
import com.soujava.mydoctor.presenter.screens.profile.ProfileViewModel
import com.soujava.mydoctor.presenter.screens.qrcodes.ListQrcodesViewModel
import com.soujava.mydoctor.presenter.screens.scanner.AnalyzeViewModel
import com.soujava.mydoctor.presenter.screens.scanner.ScannerViewModel
import com.soujava.mydoctor.presenter.screens.search.SearchViewModel
import com.soujava.mydoctor.presenter.screens.start.StartViewModel
import com.soujava.mydoctor.presenter.screens.triage.TriageViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    factory<IExternalRepository> { FirestoreImpl() }
    factory<IAuthentication> { AuthImpl(get(), get()) }
    factory<ILocalRepository> { SessionPreferenceImpl(get()) }
    factory<IApi> { ApiImpl() }


    viewModel {
        ChronologyViewModel()
    }

    viewModelOf(::StartViewModel)
    viewModelOf(::TriageViewModel)
    viewModelOf(::ListQrcodesViewModel)
    viewModelOf(::ScannerViewModel)
    viewModelOf(::RegisterViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::MedicationViewModel)
    viewModelOf(::AnalyzeViewModel)
    viewModelOf(::HistoryViewModel)
    viewModelOf(::MedicalPrescriptionViewModel)
}