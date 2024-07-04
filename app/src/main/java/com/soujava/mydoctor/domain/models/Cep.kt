package com.soujava.mydoctor.domain.models


data class Cep(
    val cep: String,
    val address: String,
    val complement: String,
    val neiborhood: String,
    val city: String,
    val uf: String,
    val ddd: String,
    val number: String,
)

