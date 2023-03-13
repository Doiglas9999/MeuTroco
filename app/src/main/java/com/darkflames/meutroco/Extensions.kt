package com.darkflames.meutroco

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.text.NumberFormat
import java.util.*

fun EditText.addCurrencyFormatting() {
    var current = ""

    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val stringText = s.toString()

            if (stringText != current) {
                removeTextChangedListener(this)
                val locale = Locale("es", "CH")

                val currency = Currency.getInstance(locale)
                val cleanString = stringText.replace("[${currency.symbol},.]".toRegex(), "")
                val parsed = cleanString.toDouble()
                val formatted =
                    NumberFormat.getCurrencyInstance(locale).format(parsed / 100).filter { !it.isWhitespace() }.removeSuffix(currency.symbol)

                current = formatted
                setText(formatted)
                setSelection(formatted.length)
                addTextChangedListener(this)
            }
        }
    })
}