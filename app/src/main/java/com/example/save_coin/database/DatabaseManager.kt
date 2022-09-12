package com.example.save_coin.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

abstract class DatabaseManager(context: Context) : SQLiteOpenHelper(context, "banco", null, 1) {
    private val SQL_CREATE_BALANCO =
        "CREATE TABLE ${BalancoFeed.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${BalancoFeed.COLUMN_NOME} VARCHAR(255)," +
                "${BalancoFeed.COLUMN_TIPO} VARCHAR(10)," +
                "${BalancoFeed.COLUMN_RECORRENCIA} VARCHAR(15)," +
                "${BalancoFeed.COLUMN_MES_ANO_REFERENCIA} VARCHAR(15)," +
                "${BalancoFeed.COLUMN_VALOR} DECIMAL(10,5))"

    private val SQL_CREATE_ECONOMIA =
        "CREATE TABLE ${EconomiaFeed.TABLE_NAME} (" +
                "${BaseColumns._ID} VARCHAR(255) PRIMARY KEY," +
                "${EconomiaFeed.COLUMN_ENTRADA} DECIMAL(10,5)," +
                "${EconomiaFeed.COLUMN_MES} INTEGER," +
                "${EconomiaFeed.COLUMN_ANO} INTEGER," +
                "${EconomiaFeed.COLUMN_SAIDA} DECIMAL(10,5))"

    override fun onCreate(db: SQLiteDatabase?) {
        val dbManager = db!!
        dbManager.execSQL(SQL_CREATE_BALANCO)
        dbManager.execSQL(SQL_CREATE_ECONOMIA)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {}

}