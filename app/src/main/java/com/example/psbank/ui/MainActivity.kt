package com.example.psbank.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.psbank.R
import com.example.psbank.databinding.ActivityMainBinding
import com.example.psbank.viewModel.ViewModelClass
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val mainVM: ViewModelClass by viewModels()
    private val mainAdapter = MainAdapter()

    private val handler = Handler(Looper.getMainLooper())
    private val updateInterval: Long = 30 * 1000

    private var screenNotInitialized = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkInternet(binding)

        binding.btnError.setOnClickListener { checkInternet(binding) }

        handler.postDelayed(object : Runnable {
            override fun run() {
                initViewModel()
                handler.postDelayed(this, updateInterval)
            }
        }, updateInterval)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    private fun setupUI(){
        initAdapter()
        initViewModel()
    }


    private fun initViewModel() {

        mainVM.getCurrency()

        mainVM.observeAllLiveData().observe(this, Observer { currancy ->
            mainAdapter.setCurrencyList(currancy.Valute)
        })


        mainVM.observeLoadingLiveData().observe(this, Observer { isLoading ->
            if (isLoading && screenNotInitialized) {
                binding.messageError.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })


        mainVM.observeErrorLiveData().observe(this, Observer { error ->
            if (error != null) {
                val (_, errorMessage) = error
                if (errorMessage != null) {
                    if (screenNotInitialized){
                        showError(binding, getString(R.string.error_message_server))
                    }
                } else {
                    showSuccess(binding)
                }
            }
        })

    }


    private fun initAdapter() {
        binding.recyclerCurrency.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mainAdapter
        }
        binding.recyclerCurrency.addItemDecoration(SpaceItemDecoration(8))
    }


    private fun showError(binding: ActivityMainBinding, errorMessage: String) {
        binding.textHeading.visibility = View.GONE
        binding.lastUpdate.visibility = View.GONE
        binding.recyclerCurrency.visibility = View.GONE

        binding.messageError.visibility = View.VISIBLE
        binding.textError.text = errorMessage

        screenNotInitialized = true
    }


    private fun showSuccess(binding: ActivityMainBinding) {
        binding.textHeading.visibility = View.VISIBLE
        binding.lastUpdate.visibility = View.VISIBLE
        binding.recyclerCurrency.visibility = View.VISIBLE

        val currentTime = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val formattedTime = dateFormat.format(Date(currentTime))
        binding.textTheLatestUpdateTime.text = formattedTime

        binding.messageError.visibility = View.GONE
        screenNotInitialized = false
    }


    private fun checkInternet(binding: ActivityMainBinding) {
        if (isInternetAvailable(this)) {
            setupUI()
        } else {
            showError(
                binding,
                getString(R.string.error_message_network)
            )
        }
    }


    private fun isInternetAvailable(context: Context): Boolean {

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }

    }



}