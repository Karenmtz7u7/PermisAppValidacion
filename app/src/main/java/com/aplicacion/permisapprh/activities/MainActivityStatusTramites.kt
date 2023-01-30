package com.aplicacion.permisapprh.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aplicacion.permisapprh.databinding.ActivityMainStatusTramitesBinding

class MainActivityStatusTramites : AppCompatActivity() {
    private lateinit var binding: ActivityMainStatusTramitesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainStatusTramitesBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}