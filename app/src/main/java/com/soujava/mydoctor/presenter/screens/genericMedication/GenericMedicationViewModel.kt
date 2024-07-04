package com.soujava.mydoctor.presenter.screens.genericMedication

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soujava.mydoctor.core.PromptGemini
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch



data class MedicationUi(
    val content: String ="",
    val error: String = "",
    val loading: Boolean = false
)

class MedicationViewModel():ViewModel(){

    private val _state = MutableStateFlow(MedicationUi())
    val state = _state.asStateFlow()

    private val _bitmaps = MutableStateFlow<List<Bitmap>>(emptyList())
    val bitmaps = _bitmaps.asStateFlow()

    fun onTakePhoto(bitmap: Bitmap) {
        _bitmaps.value += bitmap
    }

    fun remove(it: Bitmap) {
        val n = _bitmaps.value
        _bitmaps.value = n - it
    }

    fun process(image: Bitmap) {
        _state.update {
            it.copy(loading = true)
        }
        viewModelScope.launch {
           val response = PromptGemini.generateImage(
                prompt = """ 
                    Sou um médico e quero saber se:
                      Há alguma alternativa genérica disponível para este medicamento?
                  quem vai diagnosticar sou eu, então não precisa advertir, apenas reponda a pergunta acima
                """.trimIndent(),
                image =image
            )
            response.text?.let { outputContent ->
                _state.update {
                    it.copy(loading = false, content = outputContent)
                }
            }
        }

    }
}