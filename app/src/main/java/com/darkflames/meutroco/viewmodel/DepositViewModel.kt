package com.darkflames.meutroco.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.darkflames.meutroco.SQLiteHelper
import com.darkflames.meutroco.model.CaixaModel

class DepositViewModel(context: Context): ViewModel() {

    private var sqLiteHelper = SQLiteHelper(context)

    fun addCoins(coin1real: Int, coin050: Int, coin025: Int, coin010: Int, coin005: Int): Boolean {
        val coinsList = sqLiteHelper.getAllCoins()
        val countCoin100 = if (coinsList.size != 0) coinsList[0].coin1real.toString() else "0"
        val countCoin050 = if (coinsList.size != 0)coinsList[0].coin050.toString() else "0"
        val countCoin025 = if (coinsList.size != 0)coinsList[0].coin025.toString() else "0"
        val countCoin010 = if (coinsList.size != 0)coinsList[0].coin010.toString() else "0"
        val countCoin005 = if (coinsList.size != 0)coinsList[0].coin005.toString() else "0"

        val newCoin1real = coin1real + countCoin100.toInt()
        val newCoin050 = coin050 + countCoin050.toInt()
        val newCoin025 = coin025 + countCoin025.toInt()
        val newCoin010 = coin010 + countCoin010.toInt()
        val newCoin005 = coin005 + countCoin005.toInt()

        val caixa = CaixaModel(coin1real = newCoin1real, coin050 = newCoin050, coin025 = newCoin025, coin010 = newCoin010, coin005 = newCoin005)
        val status = sqLiteHelper.insertCoin(caixa)

        return status > -1
    }

}
