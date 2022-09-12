package com.example.save_coin

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.save_coin.database.BalancoDatabaseManager
import com.example.save_coin.database.EconomiaDatabaseManager
import com.example.save_coin.enumeration.Recorrencia
import com.example.save_coin.enumeration.Tipo
import com.example.save_coin.model.Balanco
import com.example.save_coin.model.Economia
import java.math.BigDecimal

class AdicionarBalancoActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicionar_balanco)

        val buttonAdicionarBalanco = findViewById<Button>(R.id.btn_adicionar_balanco)

        buttonAdicionarBalanco.setOnClickListener {
            val databaseManager = BalancoDatabaseManager(this)
            val balanco = convertInputsToBalanco()

            databaseManager.inserir(balanco)

            finish()
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
}