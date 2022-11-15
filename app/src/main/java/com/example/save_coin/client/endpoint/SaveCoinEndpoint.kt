package com.example.save_coin.client.endpoint

import com.example.save_coin.client.request.LancamentoRequest
import com.example.save_coin.client.request.UsuarioRequest
import com.example.save_coin.client.response.LancamentoResponse
import com.example.save_coin.client.response.UsuarioResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header

import retrofit2.http.POST

interface SaveCoinEndpoint {
    @POST("/usuario")
    fun criarUsuario(@Body user: UsuarioRequest): Call<UsuarioResponse>

    @POST("/lancamento")
    fun criarLancamento(@Body lancamento: LancamentoRequest, @Header("usuario") usuario: String): Call<LancamentoResponse>
}