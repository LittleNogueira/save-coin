package com.example.save_coin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.save_coin.client.SaveCoinClient
import com.example.save_coin.client.request.LancamentoRequest
import com.example.save_coin.client.request.UsuarioRequest
import com.example.save_coin.client.response.LancamentoResponse
import com.example.save_coin.client.response.UsuarioResponse
import com.example.save_coin.enumeration.Recorrencia
import com.example.save_coin.enumeration.Tipo
import com.example.save_coin.shared_preference.UsuarioSharedPreference
import com.example.save_coin.shared_preference.model.UsuarioPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal

class DadosIniciasActivity : AppCompatActivity() {

    private val BALANCO_INICIAL = "balanco-inicial"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dados_inicias)

        val buttonVamosEconomizar = findViewById<Button>(R.id.btn_vamos_economizar)

        buttonVamosEconomizar.setOnClickListener(){
            salvarDadosInicias()
        }
    }

    private fun salvarDadosInicias(){
        val intent = Intent(this, DashboardActivity::class.java)
        val buttonVamosEconomizar = findViewById<Button>(R.id.btn_vamos_economizar)
        val sharedPreference = UsuarioSharedPreference(this)
        val saveCoinClient = SaveCoinClient()

        buttonVamosEconomizar.isEnabled = false;

        val usuarioRequest = convertInputToUsuarioRequest()
        val callback = saveCoinClient.criarUsuario(usuarioRequest)

        callback.enqueue(object : Callback<UsuarioResponse> {
            override fun onFailure(call: Call<UsuarioResponse>, t: Throwable) {
                buttonVamosEconomizar.isEnabled = true;
                Toast.makeText(applicationContext, "Houve um falha na conexao com a API!", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<UsuarioResponse>, response: Response<UsuarioResponse>) {
                val usuarioPreference = convertInputsToUsuarioPreference(response.body()!!)
                sharedPreference.put(usuarioPreference)

                val lancamentoRequest = convertInputToLancamentoRequest()
                val callbackLancamento = saveCoinClient.criarLancamento(lancamentoRequest, usuarioPreference!!.uuid)

                callbackLancamento.enqueue(object : Callback<LancamentoResponse> {
                    override fun onFailure(call: Call<LancamentoResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, "Houve um falha na conexao com a API!", Toast.LENGTH_LONG).show()
                    }
                    override fun onResponse(call: Call<LancamentoResponse>, response: Response<LancamentoResponse>) {
                        startActivity(intent)
                        finish()
                    }
                })
            }
        })
    }

    private fun convertInputsToUsuarioPreference(usuarioResponse: UsuarioResponse): UsuarioPreference {
        return UsuarioPreference(
            uuid = usuarioResponse.uuid,
            nome = usuarioResponse.nome,
            email = usuarioResponse.email
        )
    }

    private fun convertInputToLancamentoRequest(): LancamentoRequest {
        val editTextReceita = findViewById<TextView>(R.id.edt_receita_liquida_mensal)

        return LancamentoRequest(
            nome = BALANCO_INICIAL,
            valor = BigDecimal(editTextReceita.text.toString()),
            tipo = Tipo.ENTRADA,
            recorrencia = Recorrencia.MENSAL
        )
    }

    private fun convertInputToUsuarioRequest(): UsuarioRequest{
        val editTextNome = findViewById<TextView>(R.id.edt_nome)
        val editTextEmail = findViewById<TextView>(R.id.edt_email)

        return UsuarioRequest(
            nome = editTextNome.text.toString(),
            email = editTextEmail.text.toString()
        )
    }
}