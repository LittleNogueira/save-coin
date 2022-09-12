package com.example.save_coin.database

import android.provider.BaseColumns

object EconomiaFeed : BaseColumns {
    const val TABLE_NAME = "economia"
    const val COLUMN_ENTRADA = "entrada"
    const val COLUMN_SAIDA = "saida"
    const val COLUMN_MES = "mes"
    const val COLUMN_ANO = "ano"
}