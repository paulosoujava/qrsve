package com.soujava.mydoctor.domain.contract

import com.soujava.mydoctor.domain.models.Profile

interface IAuthentication {
    fun authenticate(
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    )
    fun register(
        email: String,
        password: String,
        onResult: (Boolean, String) -> Unit
    )
    fun forgetPassword(email: String, onResult: (Boolean, String) -> Unit)

    fun logout()
}