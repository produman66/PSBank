package com.example.psbank.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.psbank.data.model.Currency
import com.example.psbank.data.retrofit.Retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ViewModelClass : ViewModel() {

    private var allLiveData = MutableLiveData<Currency>()

    private var loadingLiveData = MutableLiveData<Boolean>()
    private var errorLiveData = MutableLiveData<Pair<Int, String?>>()

    fun getCurrency(){

        loadingLiveData.value = true

        Retrofit.api.getCurrencies().enqueue(object : Callback<Currency> {
            override fun onResponse(call: Call<Currency>, response: Response<Currency>) {


                if (response.isSuccessful) {
                    allLiveData.value = response.body()
                    errorLiveData.value = Pair(response.code(), null)
                    Log.i("json", "Cool: ${response.body()}")
                } else {
                    errorLiveData.value = Pair(response.code(), "Error loading data")
                    Log.e("json", "Error response: ${response.code()}, ${response.message()}")
                }
                loadingLiveData.value = false

            }

            override fun onFailure(call: Call<Currency>, t: Throwable) {
                Log.d("json", t.message.toString())
                loadingLiveData.value = false
                errorLiveData.value = Pair(0, t.message)
            }
        })
    }

    fun observeAllLiveData(): LiveData<Currency> {
        return allLiveData
    }

    fun observeErrorLiveData(): LiveData<Pair<Int, String?>> {
        return errorLiveData
    }

    fun observeLoadingLiveData(): LiveData<Boolean> {
        return loadingLiveData
    }



}