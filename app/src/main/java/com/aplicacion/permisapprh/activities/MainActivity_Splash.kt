package com.aplicacion.permisapprh.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.aplicacion.permisapprh.R

class MainActivity_Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_splash)
        //Ejecuta la funcion agregada al final
        startTimer()

    }

    //Funcion para el splash
    fun startTimer(){

        //Asignar tiempo de duracion del splash
        object: CountDownTimer(1000,1000){
            override fun onTick(millisUntilFinished: Long) {

            }
            //Ejecuta la pantalla de loggeo despues del tiempo asignado
            override fun onFinish() {
                val intent= Intent(applicationContext, MainActivity::class.java).apply{}
                startActivity(intent)
                finish()
            }

        }.start()
    }
}