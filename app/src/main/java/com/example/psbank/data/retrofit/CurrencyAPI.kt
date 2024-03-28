package com.example.psbank.data.retrofit

import com.example.psbank.data.model.Currency
import retrofit2.Call
import retrofit2.http.GET


interface CurrencyAPI {
    @GET("/daily_json.js")
    fun getCurrencies(): Call<Currency>
}

