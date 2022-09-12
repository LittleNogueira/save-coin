package com.example.save_coin

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.save_coin.shared_preference.UsuarioSharedPreference
import java.util.Objects.isNull

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar!!.hide()

        Handler(Looper.getMainLooper()).postDelayed({
            val usuarioSharedPreference = UsuarioSharedPreference(this)
            val usuarioPreference = usuarioSharedPreference.get()

            val intent = if(isNull(usuarioPreference)){
                Intent(this, DadosIniciasActivity::class.java)
            }else{
                Intent(this, DashboardActivity::class.java)
            }

            startActivity(intent)
            finish()
        }, 3000)
    }

}