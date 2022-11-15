package com.example.save_coin.client.request

import com.example.save_coin.enumeration.Recorrencia
import com.example.save_coin.enumeration.Tipo
import java.math.BigDecimal

class LancamentoRequest(
    val nome: String,
    val valor: BigDecimal,
    val tipo: Tipo,
    val recorrencia: Recorrencia)
{
}