package com.example.save_coin.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.provider.BaseColumns
import androidx.annotation.RequiresApi
import com.example.save_coin.enumeration.Recorrencia
import com.example.save_coin.enumeration.Recorrencia.*
import com.example.save_coin.enumeration.Tipo
import com.example.save_coin.enumeration.Tipo.ENTRADA
import com.example.save_coin.model.Balanco
import com.example.save_coin.model.Economia
import java.math.BigDecimal

class BalancoDatabaseManager(context: Context) : DatabaseManager(context) {

    fun inserir(balanco: Balanco){
        val db = this.readableDatabase
        val cv = ContentValues()

        cv.put(BalancoFeed.COLUMN_NOME, balanco.nome)
        cv.put(BalancoFeed.COLUMN_VALOR, balanco.valor.toDouble())
        cv.put(BalancoFeed.COLUMN_TIPO, balanco.tipo.name)
        cv.put(BalancoFeed.COLUMN_RECORRENCIA, balanco.recorrencia.name)
        cv.put(BalancoFeed.COLUMN_MES_ANO_REFERENCIA, balanco.mesAnoReferencia)

        db.insert(BalancoFeed.TABLE_NAME, BaseColumns._ID, cv)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("Range")
    fun buscarLancamentosDoMes(): List<Balanco> {
        val db = this.readableDatabase
        val select = "SELECT * FROM ${BalancoFeed.TABLE_NAME} " +
                "WHERE ${BalancoFeed.COLUMN_RECORRENCIA} IN ('$DIARIA', '$MENSAL','$SEMANAL') " +
                "OR (${BalancoFeed.COLUMN_RECORRENCIA} = '$UNICA' AND ${BalancoFeed.COLUMN_MES_ANO_REFERENCIA} = '${Economia.idAtual()}')";
        val cursor = db.rawQuery(select, null)

        val balancos = ArrayList<Balanco>()
        while (!cursor.isLast){
            cursor.moveToNext()
            val balanco = Balanco(
                id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID)),
                nome = cursor.getString(cursor.getColumnIndex(BalancoFeed.COLUMN_NOME)),
                valor = BigDecimal(cursor.getString(cursor.getColumnIndex(BalancoFeed.COLUMN_VALOR))),
                tipo = Tipo.valueOf(cursor.getString(cursor.getColumnIndex(BalancoFeed.COLUMN_TIPO))),
                recorrencia = Recorrencia.valueOf(cursor.getString(cursor.getColumnIndex(BalancoFeed.COLUMN_RECORRENCIA))),
                mesAnoReferencia = cursor.getString(cursor.getColumnIndex(BalancoFeed.COLUMN_MES_ANO_REFERENCIA)),
            )
            balancos.add(balanco)
        }

        return balancos
    }
}