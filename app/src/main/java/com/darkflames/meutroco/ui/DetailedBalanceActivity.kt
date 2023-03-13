package com.darkflames.meutroco.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.darkflames.meutroco.viewmodel.DetailedBalanceViewModel
import com.darkflames.meutroco.SQLiteHelper
import com.darkflames.meutroco.databinding.ActivityDetailedBalanceBinding

class DetailedBalanceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailedBalanceBinding
    private lateinit var viewModel: DetailedBalanceViewModel

    private lateinit var sqLiteHelper: SQLiteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailedBalanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sqLiteHelper = SQLiteHelper(this)

        viewModel = DetailedBalanceViewModel(sqLiteHelper)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.buttonEmptyCashRegister.setOnClickListener {emptyCashRegister()}

        binding.imageViewArrowBackBalance.setOnClickListener{finish()}

    }

    private fun emptyCashRegister(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmar ação")
        builder.setMessage("Tem certeza que deseja limpar o caixa?")
        builder.setPositiveButton("Sim") { _, _ ->
            viewModel.clearCoins()
        }
        builder.setNegativeButton("Não", null)
        val dialog = builder.create()
        dialog.show()
    }
}

