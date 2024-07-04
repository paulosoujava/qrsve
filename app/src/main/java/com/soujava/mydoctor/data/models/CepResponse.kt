package com.soujava.mydoctor.data.models

import com.google.gson.Gson
import com.soujava.mydoctor.domain.models.Cep

data class CepResponse(
    val cep: String,
    val logradouro: String,
    val complemento: String,
    val bairro: String,
    val localidade: String,
    val uf: String,
    val ibge: String,
    val gia: String,
    val ddd: String,
    val siafi: String
) {
    companion object {
        fun fromJson(jsonString: String): CepResponse {
            val json = jsonString.trim()
            return Gson().fromJson(json, CepResponse::class.java)
        }

        fun create(
            cepResponse: CepResponse
        ): Cep {
            if (cepResponse.cep == null) {
                return Cep(
                    cep = "",
                    neiborhood = "",
                    complement = "",
                    address = "",
                    city = "",
                    uf = "",
                    ddd = "",
                    number = ""
                )
            }
            return Cep(
                cep = cepResponse.cep,
                neiborhood = cepResponse.logradouro,
                complement = "",
                address = cepResponse.bairro,
                city = cepResponse.localidade,
                uf = cepResponse.uf,
                ddd = cepResponse.ddd,
                number = ""
            )
        }
    }

}