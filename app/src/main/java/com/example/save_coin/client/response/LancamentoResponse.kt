package com.example.save_coin.client.response

import com.example.save_coin.enumeration.Recorrencia
import com.example.save_coin.enumeration.Tipo
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class LancamentoResponse(
    @SerializedName("id")           val id: Long,
    @SerializedName("nome")         val nome: String,
    @SerializedName("valor")        val valor: BigDecimal,
    @SerializedName("tipo")         val tipo: Tipo,
    @SerializedName("recorrencia")  val recorrencia: Recorrencia,
    @SerializedName("referencia")   val referencia: String
)