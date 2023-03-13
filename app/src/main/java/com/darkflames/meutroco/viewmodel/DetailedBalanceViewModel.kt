package com.darkflames.meutroco.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.darkflames.meutroco.SQLiteHelper

class DetailedBalanceViewModel(private val sqLiteHelper: SQLiteHelper) : ViewModel() {

    private val _coin100Count = MutableLiveData<String>("")
    private val _coin050Count = MutableLiveData<String>("")
    private val _coin025Count = MutableLiveData<String>("")
    private val _coin010Count = MutableLiveData<String>("")
    private val _coin005Count = MutableLiveData<String>("")
    private val _totalCoins = MutableLiveData<String>("")

    val coin100Count: LiveData<String> = _coin100Count
    val coin050Count: LiveData<String> = _coin050Count
    val coin025Count: LiveData<String> = _coin025Count
    val coin010Count: LiveData<String> = _coin010Count
    val coin005Count: LiveData<String> = _coin005Count
    val totalCoins: LiveData<String> = _totalCoins

    init {
        getCoins()
    }

    fun clearCoins() {
        sqLiteHelper.emptyCashRegister()
        getCoins()
    }

    private fun getCoins() {
        val coinsList = sqLiteHelper.getAllCoins()

        val countCoin100 = coinsList.getOrNull(0)?.coin1real.toString() ?: "0"
        val countCoin050 = coinsList.getOrNull(0)?.coin050.toString() ?: "0"
        val countCoin025 = coinsList.getOrNull(0)?.coin025.toString() ?: "0"
        val countCoin010 = coinsList.getOrNull(0)?.coin010.toString() ?: "0"
        val countCoin005 = coinsList.getOrNull(0)?.coin005.toString() ?: "0"

        _coin100Count.value = countCoin100
        _coin050Count.value = countCoin050
        _coin025Count.value = countCoin025
        _coin010Count.value = countCoin010
        _coin005Count.value = countCoin005

        val totalCoins = (countCoin100.toInt() * 1) +
                (countCoin050.toInt() * 0.50) +
                (countCoin025.toInt() * 0.25) +
                (countCoin010.toInt() * 0.10) +
                (countCoin005.toInt() * 0.05)
        _totalCoins.value = "R$ ${(totalCoins).toFloat()}"
    }
}

