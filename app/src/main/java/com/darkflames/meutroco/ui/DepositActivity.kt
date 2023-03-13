package com.darkflames.meutroco.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.darkflames.meutroco.viewmodel.DepositViewModel
import com.darkflames.meutroco.SQLiteHelper
import com.darkflames.meutroco.databinding.ActivityDepositBinding

class DepositActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDepositBinding

    private lateinit var depositViewModel: DepositViewModel
    private lateinit var sqLiteHelper: SQLiteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDepositBinding.inflate(layoutInflater)
        setContentView(binding.root)
        calculateCoins()

        depositViewModel = DepositViewModel(this)
        sqLiteHelper = SQLiteHelper(this)

        binding.buttonDepositAmount.setOnClickListener { addCoins() }

        binding.imageViewArrowBack.setOnClickListener { finish() }
    }

    private fun addCoins() {
        val coin1real = if (!binding.editTextCoin1real.text.isEmpty())binding.editTextCoin1real.text.toString().toInt() else 0
        val coin050 = if (!binding.editTextCoin050.text.isEmpty())binding.editTextCoin050.text.toString().toInt() else 0
        val coin025 = if (!binding.editTextCoin025.text.isEmpty())binding.editTextCoin025.text.toString().toInt() else 0
        val coin010 = if (!binding.editTextCoin010.text.isEmpty())binding.editTextCoin010.text.toString().toInt() else 0
        val coin005 = if (!binding.editTextCoin005.text.isEmpty())binding.editTextCoin005.text.toString().toInt() else 0

        val status = depositViewModel.addCoins(coin1real, coin050, coin025, coin010, coin005)

        if (status) {
            Toast.makeText(this, "Moedas Depositadas!", Toast.LENGTH_LONG).show()
            clearEditText()
            finish()
        } else {
            Toast.makeText(this, "Moedas não depositadas..", Toast.LENGTH_LONG).show()
        }
    }

    private fun clearEditText(){
        binding.editTextCoin1real.text = null
        binding.editTextCoin050.text = null
        binding.editTextCoin025.text = null
        binding.editTextCoin010.text = null
        binding.editTextCoin005.text = null
        binding.editTextCoin1real.requestFocus()
    }

    private fun calculateCoins(){
        binding.editTextCoin1real.doOnTextChanged { text, start, before, count -> calculate() }
        binding.editTextCoin050.doOnTextChanged { text, start, before, count -> calculate() }
        binding.editTextCoin025.doOnTextChanged { text, start, before, count -> calculate() }
        binding.editTextCoin010.doOnTextChanged { text, start, before, count -> calculate() }
        binding.editTextCoin005.doOnTextChanged { text, start, before, count -> calculate() }
    }

    private fun calculate(){
        val total1Real : Float = (if (!binding.editTextCoin1real.text.isEmpty())binding.editTextCoin1real.text.toString().toInt() * 1 else 0).toFloat()
        val total050Real: Float = (if (!binding.editTextCoin050.text.isEmpty())binding.editTextCoin050.text.toString().toInt() * 0.50 else 0).toFloat()
        val total025Real: Float = (if (!binding.editTextCoin025.text.isEmpty())binding.editTextCoin025.text.toString().toInt() * 0.25 else 0).toFloat()
        val total010Real: Float = (if (!binding.editTextCoin010.text.isEmpty())binding.editTextCoin010.text.toString().toInt() * 0.10 else 0).toFloat()
        val total005Real: Float = (if (!binding.editTextCoin005.text.isEmpty())binding.editTextCoin005.text.toString().toInt() * 0.05 else 0).toFloat()
        val totalDeposit: Float = total1Real + total050Real + total025Real + total010Real + total005Real
        binding.textViewTotalDepositAmount.text = totalDeposit.toString()
    }

}