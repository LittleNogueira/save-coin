package com.example.save_coin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.save_coin.database.BalancoDatabaseManager
import com.example.save_coin.enumeration.Recorrencia
import com.example.save_coin.enumeration.Tipo
import com.example.save_coin.model.Balanco
import com.example.save_coin.shared_preference.UsuarioSharedPreference
import com.example.save_coin.shared_preference.model.UsuarioPreference
import java.math.BigDecimal

class DadosIniciasActivity : AppCompatActivity() {

    private val BALANCO_INICIAL = "balanco-inicial"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dados_inicias)

        val buttonVamosEconomizar = findViewById<Button>(R.id.btn_vamos_economizar)

        buttonVamosEconomizar.setOnClickListener() {
            val sharedPreference = UsuarioSharedPreference(this)
            val balancoDatabaseManager = BalancoDatabaseManager(this)

            val usuarioPreference = convertInputsToUsuarioPreference()
            sharedPreference.put(usuarioPreference)

            val balanco = convertInputsToBalanco()
            balancoDatabaseManager.inserir(balanco)

            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun convertInputsToBalanco(): Balanco {
        val editTextValor = findViewById<EditText>(R.id.edt_receita_liquida_mensal)

        return Balanco(
            nome = BALANCO_INICIAL,
            valor = BigDecimal(editTextValor.text.toString()),
            tipo = Tipo.ENTRADA,
            recorrencia = Recorrencia.MENSAL
        )
    }

    private fun convertInputsToUsuarioPreference(): UsuarioPreference {
        val editTextNome = findViewById<TextView>(R.id.edt_nome)
        val editTextReceitaLiquida = findViewById<TextView>(R.id.edt_receita_liquida_mensal)
        val editTextEmail = findViewById<TextView>(R.id.edt_email)

        return UsuarioPreference(
            nome = editTextNome.text.toString(),
            receita = BigDecimal(editTextReceitaLiquida.text.toString()),
            email = editTextEmail.text.toString()
        )
    }
}