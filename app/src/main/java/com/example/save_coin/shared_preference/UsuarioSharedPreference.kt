package com.example.save_coin.shared_preference

import android.content.Context
import com.example.save_coin.shared_preference.model.UsuarioPreference
import java.math.BigDecimal

class UsuarioSharedPreference(context: Context) : BaseSharedPreference(context, "usuario") {

    private val UUID = "uuid"
    private val NOME = "nome"
    private val EMAIL = "email"

    fun put(usuarioPreference: UsuarioPreference) {
        val editor = sharedPreference.edit()

        editor.putString(UUID, usuarioPreference.uuid)
        editor.putString(NOME, usuarioPreference.nome)
        editor.putString(EMAIL, usuarioPreference.email)

        editor.apply()
    }

    fun get(): UsuarioPreference? {
        val uuid = sharedPreference.getString(UUID, "")
        val nome = sharedPreference.getString(NOME, "")
        val email = sharedPreference.getString(EMAIL, "")

        if(nome.isNullOrBlank() || email.isNullOrBlank() || uuid.isNullOrBlank())
            return null

        return UsuarioPreference(uuid, nome, email)
    }
}