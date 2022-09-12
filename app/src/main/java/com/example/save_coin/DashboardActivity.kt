package com.example.save_coin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.save_coin.database.EconomiaDatabaseManager
import com.example.save_coin.model.Economia
import com.example.save_coin.shared_preference.UsuarioSharedPreference
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class DashboardActivity : AppCompatActivity() {

    private val DECIMAL_FORMAT = DecimalFormat("#.##")
    private val MOEDA = "R$"
    private val MES_FORMAT = SimpleDateFormat("MMMM", Locale("pt", "BR"));

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        boasVindas()
        carregaEconomiaAtual()
        carregaHistorico()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        carregaEconomiaAtual()
        carregaHistorico()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_adicionar_coisa -> {
                val intent = Intent(this, AdicionarBalancoActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_dashboard, menu)
        return true
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun carregaEconomiaAtual(){
        val id = Economia.idAtual()
        val economiaDatabaseManager = EconomiaDatabaseManager(this)
        var economia = economiaDatabaseManager.calcular(id)

        val textViewEntrada = findViewById<TextView>(R.id.txt_economia_entrada_valor)
        val textViewSaida = findViewById<TextView>(R.id.txt_economia_saida_valor)
        val textViewEconomia = findViewById<TextView>(R.id.txt_economia_valor)
        val textViewResultado = findViewById<TextView>(R.id.txt_resultado)

        textViewEntrada.text = MOEDA.plus(DECIMAL_FORMAT.format(economia.entrada))
        textViewSaida.text = MOEDA.plus(DECIMAL_FORMAT.format(economia.saida))
        textViewEconomia.text = MOEDA.plus(DECIMAL_FORMAT.format(economia.economia()))

        if(economia.economia() >= economia.entrada.multiply(BigDecimal(0.15))){
            textViewResultado.text = "Você bateu a meta de economia! :)"
        }else{
            textViewResultado.text = "Você não bateu a meta de economia :("
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun carregaHistorico(){
        val economiaDatabaseManager = EconomiaDatabaseManager(this)
        val historicos = economiaDatabaseManager.buscarHistorico()


        historicos.forEachIndexed { index, economia ->
            val txtViewHistoriocoMes = findViewByString("txt_historico_mes_".plus(index))
            val txtViewHistoriocoEntrada = findViewByString("txt_historico_entrada_".plus(index))
            val txtViewHistoriocoSaida = findViewByString("txt_historico_saida_".plus(index))
            val txtViewHistoriocoEconomia = findViewByString("txt_historico_economia_".plus(index))

            val calendario = Calendar.getInstance()
            calendario.set(economia.ano.toInt(), economia.mes.toInt(), 0)

            txtViewHistoriocoMes.text = MES_FORMAT.format(calendario.time).capitalize()
            txtViewHistoriocoEntrada.text = MOEDA.plus(DECIMAL_FORMAT.format(economia.entrada))
            txtViewHistoriocoSaida.text = MOEDA.plus(DECIMAL_FORMAT.format(economia.saida))
            txtViewHistoriocoEconomia.text = MOEDA.plus(DECIMAL_FORMAT.format(economia.economia()))
        }
    }

    private fun findViewByString(id: String): TextView{
        val idView: Int = resources.getIdentifier(id, "id", this.packageName)
        return findViewById(idView)
    }


    private fun boasVindas(){
        val textViewBemVindo = findViewById<TextView>(R.id.txt_ola)

        val usuarioSharedPreference = UsuarioSharedPreference(this)
        val usuarioPreference = usuarioSharedPreference.get()

        textViewBemVindo.text = textViewBemVindo.text.toString().plus(" ").plus(usuarioPreference!!.nome)
    }


}