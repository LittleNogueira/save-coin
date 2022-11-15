package com.example.save_coin.client

import com.example.save_coin.client.endpoint.SaveCoinEndpoint
import com.example.save_coin.client.request.LancamentoRequest
import com.example.save_coin.client.request.UsuarioRequest
import com.example.save_coin.client.response.EconomiaResponse
import com.example.save_coin.client.response.LancamentoResponse
import com.example.save_coin.client.response.UsuarioResponse
import com.example.save_coin.util.NetworkUtils
import retrofit2.Call
import retrofit2.Retrofit

class SaveCoinClient() {
    private val retrofit: Retrofit = NetworkUtils.getRetrofitInstance("http://10.0.2.2:8080")
    private val saveCoinEndpoint: SaveCoinEndpoint = retrofit.create(SaveCoinEndpoint::class.java)

    fun criarUsuario(usuarioRequest: UsuarioRequest): Call<UsuarioResponse> {
        return saveCoinEndpoint.criarUsuario(usuarioRequest)
    }

    fun criarLancamento(lancamentoRequest: LancamentoRequest, usuario: String): Call<LancamentoResponse> {
        return saveCoinEndpoint.criarLancamento(lancamentoRequest, usuario)
    }

    fun buscarHistorico(usuario: String): Call<List<EconomiaResponse>> {
        return saveCoinEndpoint.buscarHistorico(usuario)
    }
}