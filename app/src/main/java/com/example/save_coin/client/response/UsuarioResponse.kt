package com.example.save_coin.client.response

import com.google.gson.annotations.SerializedName

data class UsuarioResponse(
    @SerializedName("uuid")     val uuid : String,
    @SerializedName("nome")     val nome : String,
    @SerializedName("email")    val email : String
)
