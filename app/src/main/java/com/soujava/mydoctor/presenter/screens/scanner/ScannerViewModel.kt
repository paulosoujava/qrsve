package com.soujava.mydoctor.presenter.screens.scanner

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soujava.mydoctor.core.PromptGemini
import com.soujava.mydoctor.domain.contract.IExternalRepository
import com.soujava.mydoctor.domain.models.Patient
import com.soujava.mydoctor.presenter.graph.TRIAGE_ONE_SCREEN
import com.soujava.mydoctor.presenter.screens.start.UiStateMain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class ScannerState {
    data object Loading : ScannerState()
    data object Idle : ScannerState()
    data class Success(val qrcodes: Patient) : ScannerState()
    data class Error(val error: String) : ScannerState()
}

class ScannerViewModel(
    private val repository: IExternalRepository
): ViewModel() {

    private val _state = MutableStateFlow<ScannerState>(ScannerState.Idle)
    val state: StateFlow<ScannerState> = _state.asStateFlow()


    fun scanner(code:String) {
        _state.update { ScannerState.Loading }
        repository.getCode(
            code = code,
            onError = { error ->
                _state.update { ScannerState.Error(error) }
            }, onResult = { data->
                _state.update { ScannerState.Success(data) }
            }
        )
    }


}