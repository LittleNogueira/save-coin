package com.example.save_coin.client.response

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class EconomiaResponse(
    @SerializedName("id")           val id: Long,
    @SerializedName("entrada")      val entrada: BigDecimal,
    @SerializedName("saida")        val saida: BigDecimal,
    @SerializedName("referencia")   val referencia: String
)