package com.example.save_coin

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.save_coin.client.SaveCoinClient
import com.example.save_coin.client.request.LancamentoRequest
import com.example.save_coin.client.response.LancamentoResponse
import com.example.save_coin.client.response.UsuarioResponse
import com.example.save_coin.database.BalancoDatabaseManager
import com.example.save_coin.enumeration.Recorrencia
import com.example.save_coin.enumeration.Tipo
import com.example.save_coin.model.Balanco
import com.example.save_coin.model.Economia
import com.example.save_coin.shared_preference.UsuarioSharedPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal

class AdicionarBalancoActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicionar_balanco)

        val intent = Intent(this, DashboardActivity::class.java)
        val sharedPreference = UsuarioSharedPreference(this)
        val databaseManager = BalancoDatabaseManager(this)
        val saveCoinClient = SaveCoinClient()

        val usuarioPreference = sharedPreference.get()

        val buttonAdicionarBalanco = findViewById<Button>(R.id.btn_adicionar_balanco)

        buttonAdicionarBalanco.setOnClickListener {

            buttonAdicionarBalanco.isEnabled = false;

            val lancamentoRequest = convertInputsToLancamento()
            val callback = saveCoinClient.criarLancamento(lancamentoRequest, usuarioPreference!!.uuid);

            callback.enqueue(object : Callback<LancamentoResponse> {
                override fun onFailure(call: Call<LancamentoResponse>, t: Throwable) {
                    buttonAdicionarBalanco.isEnabled = true;
                    Toast.makeText(applicationContext, "Houve um falha na conexao com a API!", Toast.LENGTH_LONG).show()
                }
                override fun onResponse(call: Call<LancamentoResponse>, response: Response<LancamentoResponse>) {
                    val balanco = convertInputsToBalanco()
                    databaseManager.inserir(balanco)

                    startActivity(intent)
                    finish()
                }
            })
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun convertInputsToBalanco(): Balanco{
        val editTextNome = findViewById<EditText>(R.id.edt_nome_balanco)
        val editTextValor = findViewById<EditText>(R.id.edt_valor)
        val spinnerRecorrencia = findViewById<Spinner>(R.id.spn_recorrencia)
        val spinnerTipo = findViewById<Spinner>(R.id.spn_tipo)

        return Balanco(
            nome = editTextNome.text.toString(),
            valor = BigDecimal(editTextValor.text.toString()),
            tipo = Tipo.valueOf(spinnerTipo.selectedItem.toString().uppercase()),
            recorrencia = Recorrencia.valueOf(spinnerRecorrencia.selectedItem.toString().uppercase())
        )
    }

    private fun convertInputsToLancamento(): LancamentoRequest{
        val editTextNome = findViewById<EditText>(R.id.edt_nome_balanco)
        val editTextValor = findViewById<EditText>(R.id.edt_valor)
        val spinnerRecorrencia = findViewById<Spinner>(R.id.spn_recorrencia)
        val spinnerTipo = findViewById<Spinner>(R.id.spn_tipo)

        return LancamentoRequest(
            nome = editTextNome.text.toString(),
            valor = BigDecimal(editTextValor.text.toString()),
            tipo = Tipo.valueOf(spinnerTipo.selectedItem.toString().uppercase()),
            recorrencia = Recorrencia.valueOf(spinnerRecorrencia.selectedItem.toString().uppercase())
        )
    }
}