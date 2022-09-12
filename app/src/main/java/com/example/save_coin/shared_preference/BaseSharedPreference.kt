package com.example.save_coin.shared_preference

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

abstract class BaseSharedPreference(private val context: Context, val name: String) {
    protected val sharedPreference: SharedPreferences =  context.getSharedPreferences(name, MODE_PRIVATE)
}