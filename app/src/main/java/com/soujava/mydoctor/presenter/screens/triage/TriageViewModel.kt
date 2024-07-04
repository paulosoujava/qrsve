package com.soujava.mydoctor.presenter.screens.triage

import androidx.lifecycle.ViewModel
import com.soujava.mydoctor.domain.contract.IExternalRepository
import com.soujava.mydoctor.domain.contract.ILocalRepository
import com.soujava.mydoctor.domain.models.Triage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


sealed class TriageViewModelState
data object TriageViewModelStateLoading : TriageViewModelState()
data object TriageViewModelStateSuccess : TriageViewModelState()
data object TriageViewModelStateError : TriageViewModelState()
data object TriageViewModelStateIdle : TriageViewModelState()


class TriageViewModel(
    private val repository: IExternalRepository,
    private val session: ILocalRepository
) : ViewModel() {

    private val _state = MutableStateFlow<TriageViewModelState>(TriageViewModelStateIdle)
    val state: StateFlow<TriageViewModelState> = _state.asStateFlow()


    fun saveTriage(triage: Triage) {
        _state.value = TriageViewModelStateLoading
        val profile = session.getProfile()
        if (profile != null) {
            triage.patient.uid = profile.userAuth?.uid ?: ""
            if (triage.patient.uid.isNotEmpty()) {
                profile.userAuth?.let {
                    repository.saveInStore(
                        triage.patient,
                    ) { data ->
                        if (data) {
                            _state.value = TriageViewModelStateSuccess
                            Triage.clearPatient()
                        } else {
                            _state.value = TriageViewModelStateError
                        }
                    }
                }
            }
        }
    }
}