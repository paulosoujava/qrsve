package com.soujava.mydoctor.presenter.screens.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.soujava.mydoctor.core.PromptGemini
import com.soujava.mydoctor.presenter.graph.TRIAGE_ONE_SCREEN
import com.soujava.mydoctor.presenter.screens.start.UiStateMain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SearchUI(
    val isLoading: Boolean = false,
    val prompt: String? = null,
    val error: String? = null,
)

class SearchViewModel : ViewModel() {

    private val _state = MutableStateFlow(SearchUI())
    val state = _state.asStateFlow()

    fun indications(text: String) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = PromptGemini.generateText(
                    prompt = """ 
                        Com base no relato abaixo me indique um proficional 
                        para o tratamento, segue o relato:
                        $text
                        Sugira o que achar relevante ao relato, para quebra de linha use o \ n
                        capriche nos emojis e seja bem gentil
                """.trimIndent()
                )
                response.text?.let { outputContent ->
                    Log.d("TAG", "searchKeys: $outputContent")

                    _state.update {
                        it.copy(
                            isLoading = false,
                            prompt = outputContent
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = true, error = e.localizedMessage) }
                Log.d("TAG", "Erro ${e.localizedMessage}")

            }
        }
    }
}