package com.example.psbank.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.psbank.R
import com.example.psbank.data.model.Valute
import com.example.psbank.databinding.ListItemBinding
import com.google.gson.internal.LinkedTreeMap

class MainAdapter : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    private var currencyList = ArrayList<String>()
    private var currencyMap = LinkedTreeMap<String, Valute>()

    fun setCurrencyList(currencyMap: Map<String, Valute>) {
        this.currencyList = currencyMap.keys.toList() as ArrayList<String>
        this.currencyMap = currencyMap as LinkedTreeMap<String, Valute>
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currency = currencyList[position]
        val context = holder.itemView.context

        holder.binding.textCurrencyCharCode.text = currency
        holder.binding.textCurrencyName.text = currencyMap[currency]?.Name ?: ""
        holder.binding.textCurrencyName.text = currencyMap[currency]?.Name ?: ""
        holder.binding.textCurrencyNominal.text = currencyMap[currency]?.Nominal.toString()
        holder.binding.textCurrencyValue.text = "${currencyMap[currency]?.Value}₽"

        val colorString = currencyMap[currency]?.NumCode.toString()
        val red = colorString[0].toString().toInt() * 16
        val green = colorString[1].toString().toInt() * 16
        val blue = colorString[2].toString().toInt() * 16
        holder.binding.frameValute.setCardBackgroundColor(Color.argb(255, red, green, blue))

        holder.binding.framePercent.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        val newValue = currencyMap[currency]?.Value ?: 0.0
        val oldValue = currencyMap[currency]?.Previous ?: 0.0

        val change = (newValue - oldValue) / oldValue * 100

        if (change > 0) {
            holder.binding.framePercent.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green))
            holder.binding.imageArrow.setImageResource(R.drawable.baseline_arrow_drop_up_24)
            holder.binding.textCurrencyPercent.text = "+${"%.2f".format(change)}%"
        } else {
            holder.binding.framePercent.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red))
            holder.binding.imageArrow.setImageResource(R.drawable.baseline_arrow_drop_down_24)
            holder.binding.textCurrencyPercent.text = "${"%.2f".format(change)}%"
        }

    }

    override fun getItemCount(): Int {
        return currencyList.size
    }
}