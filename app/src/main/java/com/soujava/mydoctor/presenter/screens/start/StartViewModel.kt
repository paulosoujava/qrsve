package com.soujava.mydoctor.presenter.screens.start

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.soujava.mydoctor.core.PromptGemini
import com.soujava.mydoctor.domain.contract.ILocalRepository
import com.soujava.mydoctor.presenter.graph.LOGIN_SCREEN
import com.soujava.mydoctor.presenter.graph.TRIAGE_ONE_SCREEN
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StartViewModel(
    private val repository: ILocalRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiStateMain> = MutableStateFlow(UiStateMain.Initial)
    val uiState: StateFlow<UiStateMain> = _uiState.asStateFlow()

    fun getProfile() = repository.getProfile()


    fun searchKeys(
        spokenText: String,
        navController: NavHostController,
    ) {
        _uiState.value = UiStateMain.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {


                val response = PromptGemini.generateText(
                    prompt = """ 
                        Primeira REGRA:
                        não gere nenhum código o retorno deve ser exclusivamente um json
                        sem ```e o nome Json no inicio da resposta
                        Segunda REGRA:
                        Quando o type decidido for ONE_LINE_TEXT_FIELD  diga qual o typeField sera:
                        Númerico ou texto
                        Terceira REGRA:
                        A diferença entre ONE_LINE_TEXT_FIELD e MORE_LINE_TEXT_FIELD é que o
                        one line é um edit text com apenas uma linha, já o MORE_LINE_TEXT_FIELD é 
                        várias linhas, e sempre do typeField texto.
                        Quarta REGRA:
                        Caso a palavra dada não tenha relação com doençar retorne adicione 
                        uma resposta de que o app foi feito para axiliar em uma triagem , modifique a mensagem se necessário 
                        use o campo  messageToEmptyJson, mas somente se detectar que o que foi enviado para
                        analise não seja relativo a doenças, dores, sintomas, medicina...
                        Quinta REGRA:
                        se o campo messageToEmptyJson for vazio, o json tem que items tem que estar vazio
                        SEXTA REGRA:
                        PARA OPÇOES DE SIM E NAO USE O RADIO GROUP
                        PARA OUTRAS OPÇÕES QUE NÃO SEJA SIM OU NAO USE O CHECKBOX OU SLIDER OU ONE_LINE_TEXT_FIELD OU O MORE_LINE_TEXT_FIELD
                        
                       AS REGRAS DEVEM SEREM SEGUIDAS COM PRIORIDADE e ESTE FORMULARIO
                       TEM O INTUITO DE AJUDAR O MEDICO  Não crie perguntas para alergia, ou se usa medicamentos pois eu ja perguntei,
                       foque nas regras e prioridades
                         
                    Dado este texto $spokenText
                    com base nas enums:
                    enum class PageType {
                        ONE_LINE_TEXT_FIELD,
                        MORE_LINE_TEXT_FIELD,
                        CHECKBOX,
                        RADIO_GROUP
                    }
                    e na entidade 
                    data class JsonItem(
                            val type: String,
                            val typeField: String,
                            val label: String,
                            val options: List<String>? = null"
                        )
                        gere um json com o modelo abaixo
                        data class JsonTriage(
                            val items: List<JsonItem>,
                            val hiddenToDoctor: String="",
                            val messageToEmptyJson: String = "
                        )
                        quando o item for CHECKBOX ou RADIO_GROUP
                        deverá gerar as opções na lista de string options,
                        o campo hiddenToDoctor se julgar necessário pode retornar links
                        de artigos referente ao tema
                        
                   
                """.trimIndent()
                )
                response.text?.let { outputContent ->
                    Log.d("TAG", "searchKeys: $outputContent")
                    _uiState.value = UiStateMain.Success(outputContent)
                    PromptGemini.returnText = outputContent
                    viewModelScope.launch {
                        navController.navigate(TRIAGE_ONE_SCREEN)
                    }

                }


            } catch (e: Exception) {
                Log.d("TAG", "Erro ${e.localizedMessage}")
                _uiState.value = UiStateMain.Error(e.localizedMessage ?: "")
            }
        }
    }

    fun logout(navController: NavHostController) {
        repository.clear()
        navController.navigate(LOGIN_SCREEN) {
            popUpTo(LOGIN_SCREEN) {
                inclusive = true
            }
        }
    }

}


