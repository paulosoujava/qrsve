package com.soujava.mydoctor.domain.contract

import com.soujava.mydoctor.domain.models.Cep

interface IApi {
    fun fetchCep(cep: String, callback: (Result<Cep>) -> Unit)
}