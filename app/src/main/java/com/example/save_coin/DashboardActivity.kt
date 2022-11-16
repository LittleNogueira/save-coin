package com.example.save_coin

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.save_coin.client.SaveCoinClient
import com.example.save_coin.client.response.EconomiaResponse
import com.example.save_coin.shared_preference.UsuarioSharedPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class DashboardActivity : AppCompatActivity() {

    private val DECIMAL_FORMAT = DecimalFormat("#.##")
    private val MOEDA = "R$"
    private val MES_FORMAT = SimpleDateFormat("MMMM", Locale("pt", "BR"));
    private var firstLoad = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        boasVindas()
        carregaDashboard()
    }

    override fun onStart() {
        super.onStart()
        if(firstLoad)
            carregaDashboard()
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

    private fun boasVindas(){
        val textViewBemVindo = findViewById<TextView>(R.id.txt_ola)

        val usuarioSharedPreference = UsuarioSharedPreference(this)
        val usuarioPreference = usuarioSharedPreference.get()

        textViewBemVindo.text = textViewBemVindo.text.toString().plus(" ").plus(usuarioPreference!!.nome)
    }

    private fun carregaDashboard(){
        val sharedPreference = UsuarioSharedPreference(this)
        val saveCoinClient = SaveCoinClient()

        val usuarioPreference = sharedPreference.get()
        val callback = saveCoinClient.buscarHistorico(usuarioPreference!!.uuid)

        callback.enqueue(object : Callback<List<EconomiaResponse>> {
            override fun onFailure(call: Call<List<EconomiaResponse>>, t: Throwable) {
                Toast.makeText(applicationContext, "Houve um falha na conexao com a API!", Toast.LENGTH_LONG).show()
            }
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<List<EconomiaResponse>>, response: Response<List<EconomiaResponse>>) {
                val economias = response.body()
                carregaEconomiaAtual(economias!![0])
                carregaHistorico(economias)
                firstLoad = true
            }
        })
    }

    private fun carregaEconomiaAtual(economia: EconomiaResponse){
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

    private fun carregaHistorico(economias: List<EconomiaResponse>){
        economias.forEachIndexed { index, economia ->
            val txtViewHistoriocoMes = findViewByString("txt_historico_mes_".plus(index))
            val txtViewHistoriocoEntrada = findViewByString("txt_historico_entrada_".plus(index))
            val txtViewHistoriocoSaida = findViewByString("txt_historico_saida_".plus(index))
            val txtViewHistoriocoEconomia = findViewByString("txt_historico_economia_".plus(index))

            val calendario = Calendar.getInstance()
            calendario.set(economia.ano(), economia.mes(), 0)

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

}