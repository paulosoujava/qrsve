package com.soujava.mydoctor.presenter.screens.start


sealed interface UiStateMain {
    data object Initial : UiStateMain
    data object Loading : UiStateMain
    data class Success(val outputText: String) : UiStateMain
    data class Error(val errorMessage: String) : UiStateMain
}