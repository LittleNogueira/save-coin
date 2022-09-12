package com.example.save_coin.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.save_coin.enumeration.Recorrencia
import com.example.save_coin.enumeration.Tipo
import java.math.BigDecimal

class Balanco{
    val id: Long?
    val nome: String
    val valor: BigDecimal
    val tipo: Tipo
    val recorrencia: Recorrencia
    val mesAnoReferencia: String

    @RequiresApi(Build.VERSION_CODES.O)
    constructor(nome: String,
                valor: BigDecimal,
                tipo: Tipo,
                recorrencia: Recorrencia,){
        this.id = null
        this.nome = nome
        this.valor = valor
        this.tipo = tipo
        this.recorrencia = recorrencia
        this.mesAnoReferencia = Economia.idAtual()
    }

    constructor(id: Long,
                nome: String,
                valor: BigDecimal,
                tipo: Tipo,
                recorrencia: Recorrencia,
                mesAnoReferencia: String){
        this.id = id
        this.nome = nome
        this.valor = valor
        this.tipo = tipo
        this.recorrencia = recorrencia
        this.mesAnoReferencia = mesAnoReferencia
    }

    fun mesReferencia(): String{
        return mesAnoReferencia.substring(0,mesAnoReferencia.length-4)
    }

    fun anoReferencia(): String{
        return mesAnoReferencia.substring(mesAnoReferencia.length-4,mesAnoReferencia.length)
    }
}