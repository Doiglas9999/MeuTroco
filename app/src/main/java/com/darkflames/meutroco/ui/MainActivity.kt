package com.darkflames.meutroco.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.darkflames.meutroco.viewmodel.MainViewModel
import com.darkflames.meutroco.SQLiteHelper
import com.darkflames.meutroco.addCurrencyFormatting
import com.darkflames.meutroco.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private lateinit var sqLiteHelper: SQLiteHelper
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sqLiteHelper = SQLiteHelper(this)
        mainViewModel = MainViewModel(sqLiteHelper)

        mainViewModel.createTable()

        binding.buttonDeposit.setOnClickListener {depositar()}
        binding.textViewDetailedBalance.setOnClickListener{detailedBalance()}

        calculateMoney()
        totalCoins()

        binding.editTextTextPurchaseValue.addCurrencyFormatting()
        binding.editTextAmountPaidCostumer.addCurrencyFormatting()

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        totalCoins()
    }

    private fun totalCoins() {
        val coinsList = sqLiteHelper.getAllCoins()

        val countCoin100 = if (coinsList.size != 0) coinsList[0].coin1real.toString() else "0"
        val countCoin050 = if (coinsList.size != 0)coinsList[0].coin050.toString() else "0"
        val countCoin025 = if (coinsList.size != 0)coinsList[0].coin025.toString() else "0"
        val countCoin010 = if (coinsList.size != 0)coinsList[0].coin010.toString() else "0"
        val countCoin005 = if (coinsList.size != 0)coinsList[0].coin005.toString() else "0"

        val totalCoins = (countCoin100.toInt() * 1) +
                (countCoin050.toInt() * 0.50) +
                (countCoin025.toInt() * 0.25) +
                (countCoin010.toInt() * 0.10) +
                (countCoin005.toInt() * 0.05)

        binding.textViewBalanceCashRegister.text = "Total em caixa: R$ ${totalCoins.toFloat()}"
    }

    private fun calculateMoney(){
        binding.buttonCalculateMoney.setOnClickListener {

            try {
                val totalValue = binding.editTextTextPurchaseValue.text.toString().replace(",",".").toDouble()
                val valuePaid = binding.editTextAmountPaidCostumer.text.toString().replace(",",".").toDouble()
                val resultMoney = (valuePaid - totalValue).toFloat()

                if (totalValue > valuePaid){
                    binding.textViewResultMoney.text = "O cliente não pode ficar devendo!"
                } else {
                    binding.textViewResultMoney.text = "Você deve dar ao cliente R$"+resultMoney.toString().replace(".",",")
                    calcularTroco(totalValue,valuePaid)
                }
            } catch (e:java.lang.Exception){
                binding.textViewResultMoney.text = "Digite os valores corretamente!"
            }
        }
    }

    fun calcularTroco(totalValue: Double, valuePaid: Double){
        val message = mainViewModel.calcularTroco(totalValue, valuePaid)

        binding.textViewCoinsCount.apply {
            text = message
            visibility = View.VISIBLE
        }

        totalCoins()
    }

    private fun detailedBalance(){
        val intent = Intent(this, DetailedBalanceActivity::class.java)
        startActivity(intent)
    }

    private fun depositar(){
        val intent = Intent(this, DepositActivity::class.java)
        startActivity(intent)
    }
}