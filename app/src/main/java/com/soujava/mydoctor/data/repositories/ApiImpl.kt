package com.soujava.mydoctor.data.repositories

import com.soujava.mydoctor.data.models.CepResponse
import com.soujava.mydoctor.domain.contract.IApi
import com.soujava.mydoctor.domain.models.Cep
import okhttp3.*
import java.io.IOException

class ApiImpl : IApi {
    override fun fetchCep(cep: String, callback: (Result<Cep>) -> Unit) {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://viacep.com.br/ws/$cep/json/")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                if (response.isSuccessful && !body.isNullOrBlank()) {
                    val cepResponse = CepResponse.fromJson(body)

                    callback(Result.success(CepResponse.create(cepResponse)))
                } else {
                    callback(Result.failure(IOException("Error fetching data")))
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                callback(Result.failure(e))
            }
        })
    }
}