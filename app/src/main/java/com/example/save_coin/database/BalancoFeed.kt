package com.example.save_coin.database

import android.provider.BaseColumns

object BalancoFeed : BaseColumns {
    const val TABLE_NAME = "balanco"
    const val COLUMN_NOME = "nome"
    const val COLUMN_VALOR = "valor"
    const val COLUMN_TIPO = "tipo"
    const val COLUMN_RECORRENCIA = "recorrencia"
    const val COLUMN_MES_ANO_REFERENCIA = "mes_ano_referencia"
}