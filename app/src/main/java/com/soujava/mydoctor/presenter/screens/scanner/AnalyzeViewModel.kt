package com.soujava.mydoctor.presenter.screens.scanner

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soujava.mydoctor.core.PromptGemini
import com.soujava.mydoctor.domain.contract.ILocalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class AnalyzeUi(
    val content: String ="",
    val error: String = "",
    val loading: Boolean = false
)

class AnalyzeViewModel(
    private val repository: ILocalRepository
): ViewModel() {


    private val _state = MutableStateFlow(AnalyzeUi())
    val state = _state.asStateFlow()



    fun analytics() {
        _state.update {
            it.copy(loading = true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val info = repository.getProfile()
                if(info == null) {
                    _state.update {
                        it.copy(loading = false, error = "Error")
                    }
                    return@launch
                }
                val response = PromptGemini.generateText(
                    prompt = """ 
                        Com base no
                         ${ScannerScreen.analyze}
                        Alergia: ${info.clincal?.hasAllergy}
                        Medicamento para alergina: ${info.clincal?.allergyContent}
                        Faz uso de medicamentos: ${info.clincal?.hasMedication}
                        Alergia: ${info.clincal?.medication}
                        nome do paciente: ${info.name}
                          conceda um analise para que auxilie o medico
                """.trimIndent()
                )
                response.text?.let { outputContent ->
                    _state.update {
                        it.copy(loading = false, content = outputContent)
                    }
                }
            } catch (e: Exception) {
                Log.d("TAG", "Erro ${e.localizedMessage}")
                _state.update {
                    it.copy(loading = false, error = e.localizedMessage ?: "")
                }
            }
        }
    }
}