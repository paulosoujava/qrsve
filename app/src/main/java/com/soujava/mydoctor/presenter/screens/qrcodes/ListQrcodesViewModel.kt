package com.soujava.mydoctor.presenter.screens.qrcodes

import androidx.lifecycle.ViewModel
import com.soujava.mydoctor.domain.contract.IExternalRepository
import com.soujava.mydoctor.domain.contract.ILocalRepository
import com.soujava.mydoctor.domain.models.Patient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

sealed class ListQrcodesState {
    data object Loading : ListQrcodesState()
    data class Success(val qrcodes: List<Patient>) : ListQrcodesState()
    data class Error(val error: String) : ListQrcodesState()
}

class ListQrcodesViewModel(
    private val repository: IExternalRepository,
    private val session: ILocalRepository
) : ViewModel() {

    private val _state = MutableStateFlow<ListQrcodesState>(ListQrcodesState.Loading)
    val state: StateFlow<ListQrcodesState> = _state.asStateFlow()


    init {
        _state.update { ListQrcodesState.Loading }
        getTriage()
    }

    private fun getTriage() {
        val profile = session.getProfile()
        if(profile != null){
            profile.userAuth?.let {
                it?.uid?.let { it1 ->
                    repository.getInStore(
                        it1,
                        onError = { error ->
                            _state.update { ListQrcodesState.Error(error) }
                        },
                    ){ data->
                        _state.update { ListQrcodesState.Success(data) }
                    }
                }
            }
        }
    }
}