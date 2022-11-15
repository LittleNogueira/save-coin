package com.example.save_coin

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.save_coin.client.SaveCoinClient
import com.example.save_coin.client.request.UsuarioRequest
import com.example.save_coin.client.response.UsuarioResponse
import com.example.save_coin.database.BalancoDatabaseManager
import com.example.save_coin.enumeration.Recorrencia
import com.example.save_coin.enumeration.Tipo
import com.example.save_coin.model.Balanco
import com.example.save_coin.shared_preference.UsuarioSharedPreference
import com.example.save_coin.shared_preference.model.UsuarioPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal

class DadosIniciasActivity : AppCompatActivity() {

    private val BALANCO_INICIAL = "balanco-inicial"

    override @RequiresApi(Build.VERSION_CODES.O)
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dados_inicias)

        val intent = Intent(this, DashboardActivity::class.java)
        val sharedPreference = UsuarioSharedPreference(this)
        val saveCoinClient = SaveCoinClient()

        val balancoDatabaseManager = BalancoDatabaseManager(this)//VAI SER REMOVIDO


        val buttonVamosEconomizar = findViewById<Button>(R.id.btn_vamos_economizar)

        buttonVamosEconomizar.setOnClickListener(){

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

                    val balanco = convertInputsToBalanco()
                    balancoDatabaseManager.inserir(balanco)

                    startActivity(intent)
                    finish()
                }
            })

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun convertInputsToBalanco(): Balanco {
        val editTextValor = findViewById<EditText>(R.id.edt_receita_liquida_mensal)

        return Balanco(
            nome = BALANCO_INICIAL,
            valor = BigDecimal(editTextValor.text.toString()),
            tipo = Tipo.ENTRADA,
            recorrencia = Recorrencia.MENSAL
        )
    }

    private fun convertInputsToUsuarioPreference(usuarioResponse: UsuarioResponse): UsuarioPreference {
        return UsuarioPreference(
            uuid = usuarioResponse.uuid,
            nome = usuarioResponse.nome,
            email = usuarioResponse.email
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