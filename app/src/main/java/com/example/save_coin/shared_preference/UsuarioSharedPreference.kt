package com.example.save_coin.shared_preference

import android.content.Context
import com.example.save_coin.shared_preference.model.UsuarioPreference
import java.math.BigDecimal

class UsuarioSharedPreference(context: Context) : BaseSharedPreference(context, "usuario") {

    private val NOME = "nome"
    private val EMAIL = "email"
    private val RECEITA_LIQUIDA = "receita-liquida"

    fun put(usuarioPreference: UsuarioPreference) {
        val editor = sharedPreference.edit()

        editor.putString(NOME, usuarioPreference.nome)
        editor.putString(EMAIL, usuarioPreference.email)
        editor.putString(RECEITA_LIQUIDA, usuarioPreference.receita.toString())

        editor.apply()
    }

    fun get(): UsuarioPreference? {
        val nome = sharedPreference.getString(NOME, "")
        val email = sharedPreference.getString(EMAIL, "")
        val receitaLiquida = sharedPreference.getString(RECEITA_LIQUIDA, "")

        if(nome.isNullOrBlank() || email.isNullOrBlank() || receitaLiquida.isNullOrBlank())
            return null

        return UsuarioPreference(nome, BigDecimal(receitaLiquida), email)
    }
}