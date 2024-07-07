package com.soujava.mydoctor.presenter.screens.medicalPrescription

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soujava.mydoctor.core.PromptGemini
import com.soujava.mydoctor.core.parseJsonMedicalPrescriptionList
import com.soujava.mydoctor.domain.contract.IExternalRepository
import com.soujava.mydoctor.domain.models.MedicalPrescription
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

enum class EventsMedicalPrescription {
    LOADING,
    ERROR,
    SUCCESS,
    REGULAR,
    CAMERA
}

data class MedicalPrescriptionState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val listMedicalPrescription: List<MedicalPrescription> = emptyList(),
    val events: EventsMedicalPrescription = EventsMedicalPrescription.REGULAR,
    val success: Boolean = false,
    val paths: List<String> = emptyList(),
)

class MedicalPrescriptionViewModel(
    private val repository: IExternalRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MedicalPrescriptionState())
    val state: StateFlow<MedicalPrescriptionState> = _state.asStateFlow()


    fun process() {
        event(EventsMedicalPrescription.LOADING)
        viewModelScope.launch {
            val response = PromptGemini.generateImages(
                prompt = """
                    Extrai todo o texto das imagens e gere um json  baseado na entidade abaixo
                      val nameOfMedications: String,
                        val duration: String,
                        val qtdPerDay: Int,
                        val doctor: String,
                        val crm: String,
                        val dateBegin: String,
                        val dateEnd: String,
                        val status: String,
                        val description: String,
                        onde o nameOfMedications tem que ser o nome da medicação
                        o duration tem que ser o tempo de duração da medicação
                        o qtdPerDay tem que ser a quantidade de medicação por dia
                        o doctor tem que ser o nome do doutor
                        o crm tem que ser o CRM do doutor
                        o dateBegin sempre será vazia
                        o dateEnd sempre sera vazia
                        o status  sempre será não iniciado
                        o description tem que ser a descrição da medicação se houver, caso nao haja, deve ser vazio
                        sempre retorne uma lista de objetos [{},{}], mesmo que detecte apenas uma medicação
                        NÃO ADICIONE ISTO ```json ``` pois eu vou receber um json e mapear para o objeto acima
                """.trimIndent(),
                paths = _state.value.paths
            )
            Log.d("TAG", "process: ${response.text}")
            response.text?.let { data ->
                _state.update {
                    it.copy(
                        listMedicalPrescription = parseJsonMedicalPrescriptionList(data),
                        events = EventsMedicalPrescription.SUCCESS
                    )
                }
        } ?: run {
            event(EventsMedicalPrescription.ERROR, "Deu ruim code [13.1]")
        }

    }
}


fun event(events: EventsMedicalPrescription, error: String? = null) {
    _state.update {
        when (events) {
            EventsMedicalPrescription.CAMERA -> {
                it.copy(events = EventsMedicalPrescription.CAMERA)
            }

            EventsMedicalPrescription.REGULAR -> {
                it.copy(events = EventsMedicalPrescription.REGULAR)
            }

            EventsMedicalPrescription.ERROR -> {
                it.copy(
                    events = EventsMedicalPrescription.ERROR,
                    error = error
                )
            }

            EventsMedicalPrescription.LOADING -> {
                it.copy(events = EventsMedicalPrescription.LOADING)
            }

            EventsMedicalPrescription.SUCCESS -> {
                it.copy(events = EventsMedicalPrescription.SUCCESS)

            }
        }
    }
}

    fun removePath(image: String) {
        val newList = _state.value.paths.toMutableList()
        newList.remove(image)
        _state.value = _state.value.copy(paths = newList)

    }

    fun addImageWithPath(path: File) {
        val newList = _state.value.paths.toMutableList()
        newList.add(path.absolutePath)
        _state.update {
            it.copy(paths = newList)
        }
    }


}