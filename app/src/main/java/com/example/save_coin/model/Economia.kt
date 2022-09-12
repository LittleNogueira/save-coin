package com.example.save_coin.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.math.BigDecimal
import java.time.LocalDate

class Economia {
    val id: String
    var entrada: BigDecimal
    var saida: BigDecimal
    val mes: Int
    val ano: Int

    constructor(id: String, entrada: BigDecimal, saida: BigDecimal, mes: Int, ano: Int){
        this.id = id
        this.entrada = entrada
        this.saida = saida
        this.mes = mes
        this.ano = ano
    }

    @RequiresApi(Build.VERSION_CODES.O)
    constructor(entrada: BigDecimal, saida: BigDecimal){
        val mesAnoReferencia = idAtual()
        this.id = mesAnoReferencia
        this.entrada = entrada
        this.saida = saida
        this.ano = mesAnoReferencia.substring(mesAnoReferencia.length-4,mesAnoReferencia.length).toInt()
        this.mes = mesAnoReferencia.substring(0,mesAnoReferencia.length-4).toInt()
    }

    fun economia(): BigDecimal{
        return entrada.minus(saida)
    }

    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun idAtual() : String{
            val dataAtual = LocalDate.now()
            val mes = dataAtual.monthValue.toString()
            val ano = dataAtual.year.toString()
            return mes.plus(ano)
        }
    }

}