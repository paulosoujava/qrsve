package com.soujava.mydoctor.presenter.screens.access.login

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.soujava.mydoctor.core.isEmailValid
import com.soujava.mydoctor.core.isPasswordValid
import com.soujava.mydoctor.domain.contract.IAuthentication
import com.soujava.mydoctor.presenter.graph.START_SCREEN
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.logger.MESSAGE


data class LoginUI(
    var isLoading: Boolean = false,
    val hasError: Boolean? = null,
    val message: String? = null
)

class LoginViewModel(
    private val repository: IAuthentication
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUI())
    val state = _state.asStateFlow()

    fun doLogin(email: String, password: String, navController: NavController) {

        if (isEmailValid(email).not()) {
            _state.update {
                it.copy(
                    hasError = true,
                    message = "Email inválido"
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
        repository.authenticate(email, password, onResult = { hasProfile, message ->
            if (hasProfile) {
                navController.navigate(START_SCREEN)
                _state.update { it.copy(isLoading = false) }
            } else

                _state.update {
                    it.copy(
                        isLoading = false,
                        hasError = !hasProfile,
                        message = message
                    )
                }
        })
    }

    fun resetError() {
        _state.update {
            it.copy(
                message = null,
                hasError = null
            )
        }
    }

    fun forgetPassword(email: String) {
        repository.forgetPassword(email){
            hasError, message ->
            _state.update {
                it.copy(
                    hasError = hasError,
                    message = message
                )
            }
        }
    }
}