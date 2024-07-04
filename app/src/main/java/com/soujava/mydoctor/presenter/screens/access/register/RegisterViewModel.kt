package com.soujava.mydoctor.presenter.screens.access.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.soujava.mydoctor.core.isEmailValid
import com.soujava.mydoctor.core.isPasswordValid
import com.soujava.mydoctor.domain.contract.IAuthentication
import com.soujava.mydoctor.presenter.graph.START_SCREEN
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class LoginUI(
    var email: String = "",
    var password: String = "",
    var isLoading: Boolean = false,
    val hasError: Boolean? = null,
    val message: String = ""
)

class RegisterViewModel(
    private val repository: IAuthentication
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUI())
    val state = _state.asStateFlow()

    fun register(
        email: String,
        password: String,
        repPassword: String,
        navHostController: NavHostController
    ) {

        if (isEmailValid(email).not()) {
            _state.update {
                it.copy(
                    hasError = true,
                    message = "Email inválido"
                )
            }

            return
        }
        if (password != repPassword) {
            _state.update {
                it.copy(
                    hasError = true,
                    message = "As senhas devem ser iguais"
                )
            }
            return
        }
        if (!isPasswordValid(password)) {
            _state.update {
                it.copy(
                    hasError = true,
                    message = "Senha inválida, a senha deve conter letras e números e no mínimo 5 caracteres"
                )
            }
            return
        }

        _state.update {
            it.copy(
                isLoading = true
            )
        }
        repository.register(email, password, onResult = { hasError, message ->
            _state.update {
                it.copy(
                    isLoading = false,
                    hasError = hasError,
                    message = message
                )
            }
            viewModelScope.launch {
                delay(6000)
                resetError()
                navHostController.navigate(START_SCREEN) {
                    popUpTo(START_SCREEN) {
                        inclusive = true
                    }
                }
            }
        })
    }

    fun resetError() {
        _state.update {
            it.copy(
                message = "",
                hasError = null
            )
        }
    }
}