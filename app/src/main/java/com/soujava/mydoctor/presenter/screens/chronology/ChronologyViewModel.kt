package com.soujava.mydoctor.presenter.screens.chronology

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soujava.mydoctor.core.PromptGemini
import com.soujava.mydoctor.presenter.screens.history.Events
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

enum class ChronologyType {
    LOADING,
    SUCCESS,
    REGULAR,
    ERROR,
    CAMERA
}

data class ChronologyState(
    val paths: List<String> = emptyList(),
    val events: ChronologyType = ChronologyType.REGULAR,
    val error: String? = null,
    val success: String? = null
)

class ChronologyViewModel : ViewModel() {
    private val _state = MutableStateFlow(ChronologyState())
    val state = _state.asStateFlow()


    fun openCamera() {
        _state.update {
            it.copy(events = ChronologyType.CAMERA)
        }
    }

    fun addImageWithPath(path: File) {
        val newList = _state.value.paths.toMutableList()
        newList.add(path.absolutePath)
        _state.update {
            it.copy(paths = newList)
        }
    }

    fun removePath(path: String) {
        val newList = _state.value.paths.toMutableList()
        newList.remove(path)
        _state.value = _state.value.copy(paths = newList)
        File(path).delete()
    }

    fun process() {
        _state.update {
            it.copy(events = ChronologyType.LOADING)
        }
        viewModelScope.launch {
            val response = PromptGemini.generateImages(
                prompt = """
                    O uso desta função será feita por um MÉDICO, então auxilie ele
                    na cronologia da doença, não é necessario advertir aconselhando um médico
                    pois o usuário que esta solicitando a informação É UM MEDICO e é de responsabilidade
                    dele o diagnostico, você só ira auxiliar
                    Com base nas imagens extraia os textos e faça uma cronologia da doença em forma de resumo
                    dizendo se houve melhora ou piora,  nas imagens tem as datas que foram feito os exames,
                    e a descirção do contexto da doença 
                    ao analizar leve em consideraçao as datas
                """.trimIndent(),
                paths = _state.value.paths
            )

            response.text?.let { data ->
                _state.update {
                    it.copy(events = ChronologyType.SUCCESS, success = data)
                }

            } ?: run {
                _state.update {
                    it.copy(events = ChronologyType.ERROR, error = "Deu ruim")
                }
            }
        }
    }
}