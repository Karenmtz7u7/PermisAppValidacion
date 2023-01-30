package com.aplicacion.permisapprh.activities


import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.aplicacion.permisapprh.databinding.ActivityMainBinding
import com.aplicacion.permisapprh.providers.AuthProvider
import com.aplicacion.permisapprh.providers.ClientProvider
import com.aplicacion.permisapprh.providers.ClientRHProvider


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val authProvider = AuthProvider()
    private lateinit var dialog: AlertDialog.Builder
    private val clientProvider = ClientProvider()
    private val clientRHProvider = ClientRHProvider()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        binding.ingresarbtn.setOnClickListener {
            Login()
        }
        binding.olvidocontrabtn.setOnClickListener {
            irRestablecer()
        }
        verificar()
    }

    private fun irRestablecer() {
        val i = Intent(this, MainActivity_restablecer::class.java)
        startActivity(i)
    }


    //funcion para validar datos e iniciar sesión
    private fun Login() {
        val email = binding.correoelectronicotxt.text.toString()
        val pass = binding.passwordtxt.text.toString()

        if (validacion(email, pass)) {
            //funcion para iniciar sesion
            authProvider.logIn(email, pass).addOnCompleteListener {
                if (it.isSuccessful) {
                    setDialog()
                } else {
                    Toast.makeText(this@MainActivity, "Inicio de sesion incorrecto",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //verifica que no haya errores
    private fun validacion(email: String, pass: String): Boolean {
        if (email.isEmpty()) {
            Toast.makeText(this, "Ingresa tu correo electronico", Toast.LENGTH_SHORT).show()
            return false
        }
        if (pass.isEmpty()) {
            Toast.makeText(this, "Ingresa tu contraseña", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun verificar() {
        binding.correoelectronicotxt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (Patterns.EMAIL_ADDRESS.matcher(binding.correoelectronicotxt.text.toString())
                        .matches()
                ) {
                    binding.ingresarbtn.isEnabled = true
                } else {
                    binding.ingresarbtn.isEnabled = false
                    binding.correoelectronicotxt.error = "Correo Invalido"
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

    }

    private fun setDialog(){
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Iniciando sesion...")
        progressDialog.setCancelable(false)
        progressDialog.show()
        object: CountDownTimer(2000,2000){
            override fun onTick(millisUntilFinished: Long) {
            }
            //Ejecuta la pantalla de Home despues del tiempo asignado
            override fun onFinish() {
                progressDialog.dismiss()
                checkInformationRH()
            }
        }.start()
    }



    //funcion que te lleva a la pagina principal de la app Home
    private fun entrarApp() {
                val i = Intent(this, MainActivity_home::class.java)
                //esta linea borra el historial de pantallas
                i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(i)
    }

    private fun checkInformationRH(){
        clientRHProvider.getRH(authProvider.getid()).addOnSuccessListener { document->
            if (document.exists()){
                entrarApp()
            }else{
                val i = Intent(this, DatosMainActivity::class.java)
                //esta linea borra el historial de pantallas
                i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(i)
            }

        }
    }

    private fun checkUsers(){



    }

    //ciclo de vida para mantener la sesion abierta
    override fun onStart() {
        super.onStart()
        if (authProvider.starSession()) {
            entrarApp()
        }
    }
}