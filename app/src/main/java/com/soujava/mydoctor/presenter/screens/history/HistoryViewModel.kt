package com.soujava.mydoctor.presenter.screens.history

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soujava.mydoctor.core.PromptGemini
import com.soujava.mydoctor.core.getFormattedDate

import com.soujava.mydoctor.core.parseJsonHistoryData
import com.soujava.mydoctor.domain.contract.IExternalRepository
import com.soujava.mydoctor.domain.models.History
import com.soujava.mydoctor.presenter.screens.chronology.ChronologyType
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File


enum class Events {
    CAMERA, LOADING, PAGES, EMPTY, ERROR, REGULAR
}

data class HistoryUI(
    val content: String = "",
    val error: String = "",
    val paths: List<String> = emptyList(),
    val listHistory: List<History> = emptyList(),
    val events: Events = Events.LOADING
)

class HistoryViewModel(
    private val repository: IExternalRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HistoryUI())
    val state = _state.asStateFlow()

    /*   private val _bitmaps = MutableStateFlow<List<Bitmap>>(emptyList())
       val bitmaps = _bitmaps.asStateFlow()*/

    init {
        readHistory()
    }

    fun actionCamera() {
        _state.update {
            it.copy(events = Events.CAMERA)
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


    private fun readHistory() {
        viewModelScope.launch {
            repository.getHistoryInStore() { data ->
                Log.d("TAG", "readHistory: $data")
                _state.update {
                    it.copy(
                        events = if (data.isEmpty()) Events.EMPTY else Events.REGULAR,
                        listHistory = data
                    )
                }
            }
        }
    }


    fun actionRegular() {
        readHistory()
    }

    fun process() {
        _state.update {
            it.copy(events = Events.LOADING)
        }
        viewModelScope.launch {
            val response = PromptGemini.generateImages(
                prompt = """
                    Extrai todo o texto das imagens e gere um json  baseado na entidade abaixo
                     data class Data(
                         val nameOfDoctor: String,
                         val crm: String,
                         val nameOfPatient: String,
                         val dateThisExam: String,
                         val resume: String,
                     )
                     onde nameOfDoctor é o nome do Doutor Doutora ou Dr.(a) ou Dr ou DR 
                     crm é o número de registro do medico ou medica
                     nameOfPatient é o nome do paciente, deve estar como Nome ou solicitante
                     dateThisExam é a data do exame, todo documento tem uma data procure ela
                     resume  todo o texto que não  se encaixou acima
                     nao inclua os ``` e o nome Json no inicio da resposta  ou [ e no final ] somente { json }
                     caso seja mais de uma imagem  no json deve ser UNICO e não uma lista dados como:
                      val nameOfDoctor: String,
                         val crm: String,
                         val nameOfPatient: String,
                         val dateThisExam: String,
                         NÃO SE REPETEM
                         o resumo em caso de 2 ou mais imagens, cocatene ao final seguindo o numero de paginas do documento
                """.trimIndent(),
                paths = _state.value.paths
            )
Log.d("TAG", "process: ${response.text}")
            response.text?.let { data ->
                repository.saveHistoryInStore(parseJsonHistoryData(data)) { result ->
                    if (result) {
                        _state.update {
                            it.copy(
                                events = Events.REGULAR,
                            )
                        }
                        readHistory()
                    } else {
                        _state.update {
                            it.copy(events = Events.ERROR, error = "Deu ruim code [12]")
                        }
                    }
                }
            } ?: run {
                _state.update {
                    it.copy(events = Events.ERROR, error = "Deu ruim")
                }
            }

        }
    }

}