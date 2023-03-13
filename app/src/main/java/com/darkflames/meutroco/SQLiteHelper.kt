package com.darkflames.meutroco

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.darkflames.meutroco.model.CaixaModel

class SQLiteHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{

        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "meutroco.db"
        private const val TBL_CAIXA = "tbl_caixa"
        private const val COIN1REAL = "coin1real"
        private const val COIN050 = "coin050"
        private const val COIN025 = "coin025"
        private const val COIN010 = "coin010"
        private const val COIN005 = "coin005"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTblCaixa = (
                "CREATE TABLE " + TBL_CAIXA + "(" + COIN1REAL + " INTEGER PRYMARY KEY,"
                + COIN050 + " INTEGER," + COIN025 + " INTEGER," + COIN010 + " INTEGER," + COIN005
                + " INTEGER" + ")"
                )
        db?.execSQL(createTblCaixa)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TBL_CAIXA")
        onCreate(db)
    }

    fun insertCoin(caixa: CaixaModel): Long{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(COIN1REAL, caixa.coin1real)
        contentValues.put(COIN050, caixa.coin050)
        contentValues.put(COIN025, caixa.coin025)
        contentValues.put(COIN010, caixa.coin010)
        contentValues.put(COIN005, caixa.coin005)


        val coinsList = getAllCoins()
        val success = if (coinsList.size >= 1){
            db.update(TBL_CAIXA, contentValues, null, null).toLong()
        } else {
            db.insert(TBL_CAIXA, null, contentValues)
        }


        db.close()
        return success

    }

    fun getQuantityCoin(coinIndex: Int): Int {
        val db = readableDatabase
        val tableName: String = when (coinIndex) {
            0 -> "COIN1REAL"
            1 -> "COIN050"
            2 -> "COIN025"
            3 -> "COIN010"
            4 -> "COIN005"
            else -> throw IllegalArgumentException("Índice de moeda inválido")
        }
        val query = "SELECT $tableName FROM $TBL_CAIXA"
        val cursor = db.rawQuery(query, null)
        val quantity = if (cursor.moveToFirst()) {
            cursor.getInt(0)
        } else {
            throw IllegalArgumentException("Moeda não encontrada")
        }
        cursor.close()
        db.close()
        return quantity
    }


    @SuppressLint("Range")
    fun getAllCoins(): ArrayList<CaixaModel>{
        val coinsList: ArrayList<CaixaModel> = ArrayList()
        val selectQuerry = "SELECT * FROM $TBL_CAIXA"
        val db = this.readableDatabase

        val cursor: Cursor?
        try {
            cursor = db.rawQuery(selectQuerry, null,)
        } catch (e:Exception){
            e.printStackTrace()
            db.execSQL(selectQuerry)
            return  ArrayList()
        }

        var coin1real : Int
        var coin050 : Int
        var coin025 : Int
        var coin010 : Int
        var coin005 : Int

        if (cursor.moveToFirst()){
            do {
                coin1real = cursor.getInt(cursor.getColumnIndex("coin1real"))
                coin050 = cursor.getInt(cursor.getColumnIndex("coin050"))
                coin025 = cursor.getInt(cursor.getColumnIndex("coin025"))
                coin010 = cursor.getInt(cursor.getColumnIndex("coin010"))
                coin005 = cursor.getInt(cursor.getColumnIndex("coin005"))
                val caixa = CaixaModel(coin1real = coin1real,coin050 = coin050,coin025 = coin025,coin010 = coin010, coin005 = coin005)
                coinsList.add(caixa)
            } while (cursor.moveToNext())
        }
        return coinsList
    }

    fun emptyCashRegister(){
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(COIN1REAL, 0)
        contentValues.put(COIN050, 0)
        contentValues.put(COIN025, 0)
        contentValues.put(COIN010, 0)
        contentValues.put(COIN005, 0)


        val coinsList = getAllCoins()
        if (coinsList.size >= 1){
            db.update(TBL_CAIXA, contentValues, null, null).toLong()
        } else {
            db.insert(TBL_CAIXA, null, contentValues)
        }

        db.close()

    }

    @SuppressLint("Range")
    fun updateCoin(coinIndex: Int, quantity: Int): Boolean {
        val db = writableDatabase
        val tableName: String = when (coinIndex) {
            0 -> "coin1real"
            1 -> "coin050"
            2 -> "coin025"
            3 -> "coin010"
            4 -> "coin005"
            else -> throw IllegalArgumentException("Índice de moeda inválido")
        }

        val selectQuery = "SELECT $tableName FROM $TBL_CAIXA"
        val cursor = db.rawQuery(selectQuery, null)
        var currentQuantity = 0
        if (cursor.moveToFirst()) {
            currentQuantity = cursor.getInt(cursor.getColumnIndex("${tableName.toLowerCase()}"))
        }
        cursor.close()

        val newQuantity = currentQuantity + quantity
        if (newQuantity < 0) {
            return false // não atualize se a quantidade resultante for negativa
        }

        val query = "UPDATE $TBL_CAIXA SET $tableName = $newQuantity"
        db.execSQL(query)
        db.close()

        return true // atualização bem sucedida
    }

}