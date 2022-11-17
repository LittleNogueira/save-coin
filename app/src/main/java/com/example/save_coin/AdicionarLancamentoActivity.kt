package com.example.save_coin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.save_coin.client.SaveCoinClient
import com.example.save_coin.client.request.LancamentoRequest
import com.example.save_coin.client.response.LancamentoResponse
import com.example.save_coin.enumeration.Recorrencia
import com.example.save_coin.enumeration.Tipo
import com.example.save_coin.shared_preference.UsuarioSharedPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal

class AdicionarLancamentoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicionar_lancamento)

        val intent = Intent(this, DashboardActivity::class.java)
        val sharedPreference = UsuarioSharedPreference(this)
        val saveCoinClient = SaveCoinClient()

        val usuarioPreference = sharedPreference.get()

        val buttonAdicionarLancamento = findViewById<Button>(R.id.btn_adicionar_lancamento)

        buttonAdicionarLancamento.setOnClickListener {

            buttonAdicionarLancamento.isEnabled = false;

            val lancamentoRequest = convertInputsToLancamento()
            val callback = saveCoinClient.criarLancamento(lancamentoRequest, usuarioPreference!!.uuid);

            callback.enqueue(object : Callback<LancamentoResponse> {
                override fun onFailure(call: Call<LancamentoResponse>, t: Throwable) {
                    buttonAdicionarLancamento.isEnabled = true;
                    Toast.makeText(applicationContext, "Houve um falha na conexao com a API!", Toast.LENGTH_LONG).show()
                }
                override fun onResponse(call: Call<LancamentoResponse>, response: Response<LancamentoResponse>) {
                    startActivity(intent)
                    finish()
                }
            })
        }
    }

    private fun convertInputsToLancamento(): LancamentoRequest{
        val editTextNome = findViewById<EditText>(R.id.edt_nome_lancamento)
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