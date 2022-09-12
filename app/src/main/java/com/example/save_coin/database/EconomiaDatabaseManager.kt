package com.example.save_coin.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.provider.BaseColumns
import androidx.annotation.RequiresApi
import com.example.save_coin.enumeration.Recorrencia.*
import com.example.save_coin.enumeration.Tipo.ENTRADA
import com.example.save_coin.model.Balanco
import com.example.save_coin.model.Economia
import java.math.BigDecimal
import java.time.YearMonth
import java.util.Objects.isNull

class EconomiaDatabaseManager(val context: Context) : DatabaseManager(context) {

    fun inserir(economia: Economia): Economia{
        val db = this.readableDatabase
        val cv = ContentValues()

        cv.put(BaseColumns._ID, economia.id)
        cv.put(EconomiaFeed.COLUMN_ENTRADA, economia.entrada.toDouble())
        cv.put(EconomiaFeed.COLUMN_SAIDA, economia.saida.toDouble())
        cv.put(EconomiaFeed.COLUMN_MES, economia.mes)
        cv.put(EconomiaFeed.COLUMN_ANO, economia.ano)

        db.insert(EconomiaFeed.TABLE_NAME, BaseColumns._ID, cv)

        return economia
    }

    fun atualizar(economia: Economia): Economia{
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(EconomiaFeed.COLUMN_ENTRADA, economia.entrada.toDouble())
        cv.put(EconomiaFeed.COLUMN_SAIDA, economia.saida.toDouble())

        db.update(EconomiaFeed.TABLE_NAME,cv, "${BaseColumns._ID}=?", arrayOf(economia.id))

        return economia
    }

    @SuppressLint("Range")
    fun buscar(id: String): Economia? {
        val db = this.readableDatabase
        val select = "SELECT * FROM ${EconomiaFeed.TABLE_NAME} WHERE ${BaseColumns._ID} = $id"
        val cursor = db.rawQuery(select, null)

        if(cursor.count > 0){
            cursor.moveToFirst()
            return Economia(
                id = cursor.getString(cursor.getColumnIndex(BaseColumns._ID)),
                entrada = BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(EconomiaFeed.COLUMN_ENTRADA))),
                saida = BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(EconomiaFeed.COLUMN_SAIDA))),
                mes = cursor.getInt(cursor.getColumnIndex(EconomiaFeed.COLUMN_MES)),
                ano = cursor.getInt(cursor.getColumnIndex(EconomiaFeed.COLUMN_ANO)),
            )
        }

        return null
    }

    @SuppressLint("Range")
    fun buscarHistorico(): List<Economia> {
        val db = this.readableDatabase
        val select = "SELECT * FROM ${EconomiaFeed.TABLE_NAME} ORDER BY ${EconomiaFeed.COLUMN_ANO} DESC, ${EconomiaFeed.COLUMN_MES} DESC LIMIT 3"
        val cursor = db.rawQuery(select, null)

        val economiaList = ArrayList<Economia>()
        while (!cursor.isLast){
            cursor.moveToNext()
            val economia = Economia(
                id = cursor.getString(cursor.getColumnIndex(BaseColumns._ID)),
                entrada = BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(EconomiaFeed.COLUMN_ENTRADA))),
                saida = BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(EconomiaFeed.COLUMN_SAIDA))),
                mes = cursor.getInt(cursor.getColumnIndex(EconomiaFeed.COLUMN_MES)),
                ano = cursor.getInt(cursor.getColumnIndex(EconomiaFeed.COLUMN_ANO)),
            )
            economiaList.add(economia)
        }

        return economiaList.asReversed()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun calcular(id: String): Economia{
        val balancoDatabaseManager = BalancoDatabaseManager(this.context)
        val balancos = balancoDatabaseManager.buscarLancamentosDoMes()

        var entrada = BigDecimal.ZERO
        var saida = BigDecimal.ZERO

        for(balanco in balancos){
            if(ENTRADA == balanco.tipo){
                entrada = entrada.plus(calculaValor(balanco))
            }else{
                saida = saida.plus(calculaValor(balanco))
            }
        }

        val economia = buscar(id)

        return if(isNull(economia))
            inserir(Economia(entrada, saida))
        else{
            economia!!.entrada = entrada
            economia.saida = saida
            atualizar(atualizar(economia))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculaValor(balanco: Balanco): BigDecimal{
        val valor = balanco.valor
        val mesAtual = YearMonth.of( balanco.anoReferencia().toInt(), balanco.mesReferencia().toInt());

        return if(arrayOf(MENSAL, UNICA).contains(balanco.recorrencia)){
            valor;
        }else if(SEMANAL == balanco.recorrencia){
            valor.multiply(BigDecimal(mesAtual.lengthOfMonth()/7))
        }else{
            valor.multiply(BigDecimal(mesAtual.lengthOfMonth()))
        }
    }

}