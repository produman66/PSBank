package com.example.psbank.data.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Retrofit {

    val api: CurrencyAPI by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://www.cbr-xml-daily.ru")
            .build()
            .create(CurrencyAPI::class.java)
    }
}