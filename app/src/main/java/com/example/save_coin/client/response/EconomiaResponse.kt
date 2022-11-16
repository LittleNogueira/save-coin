package com.example.save_coin.client.response

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

class EconomiaResponse(
    @SerializedName("id")           val id: Long,
    @SerializedName("entrada")      val entrada: BigDecimal,
    @SerializedName("saida")        val saida: BigDecimal,
    @SerializedName("referencia")   val referencia: String
){
    fun economia(): BigDecimal{
        return entrada.minus(saida)
    }

    fun ano(): Int{
        return referencia.substring(referencia.length-4,referencia.length).toInt()
    }

    fun mes(): Int{
        return referencia.substring(0,referencia.length-4).toInt()
    }
}
