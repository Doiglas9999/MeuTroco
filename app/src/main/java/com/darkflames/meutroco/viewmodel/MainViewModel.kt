package com.darkflames.meutroco.viewmodel

import androidx.lifecycle.ViewModel
import com.darkflames.meutroco.SQLiteHelper

class MainViewModel(private val sqLiteHelper: SQLiteHelper) : ViewModel(){

    fun createTable(){
        val coinsList = sqLiteHelper.getAllCoins()
        if (coinsList.size < 1){
            sqLiteHelper.emptyCashRegister()
        }
    }

    fun calcularTroco(totalValue: Double, valuePaid: Double): String {
        val totalCoins = (totalValue * 100).toInt()
        val paidCoins = (valuePaid * 100).toInt()
        val trocoCentavos = paidCoins - totalCoins
        val coinNameList = listOf("1 real", "50 centavos", "25 centavos", "10 centavos", "5 centavos")

        val coins = arrayOf(100, 50, 25, 10, 5)
        val coinToIndexMap = mapOf(100 to 0, 50 to 1, 25 to 2, 10 to 3, 5 to 4)

        val quantityCoins = IntArray(coins.size)
        var remainingValue = trocoCentavos
        val usedCoins = mutableListOf<String>()

        for (i in coins.indices) {
            val coinIndex = coinToIndexMap[coins[i]] ?: throw IllegalArgumentException("Moeda não encontrada")
            var availableQuantity = sqLiteHelper.getQuantityCoin(coinIndex)

            while (remainingValue >= coins[i] && availableQuantity > 0) {
                quantityCoins[i]++
                remainingValue -= coins[i]
                availableQuantity--
                usedCoins.add(coinNameList[coinIndex])
            }
        }

        if (remainingValue == 0) {
            usedCoins.forEach {
                val coinIndex = coinNameList.indexOf(it)
                sqLiteHelper.updateCoin(coinIndex, -1)
            }
            val sb = StringBuilder()
            for (i in coins.indices) {
                if (quantityCoins[i] > 0) {
                    val valueCoin = coins[i] / 100.0
                    sb.append("${quantityCoins[i]} moeda(s) de R$ ${String.format("%.2f", valueCoin)}")
                    sb.append("\n")
                }
            }
            return sb.toString()
        } else {
            return "Não há moedas suficientes para o troco."
        }
    }







}