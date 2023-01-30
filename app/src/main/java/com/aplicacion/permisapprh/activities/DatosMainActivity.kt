package com.aplicacion.permisapprh.activities

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.aplicacion.permisapprh.Models.ClientRH
import com.aplicacion.permisapprh.R
import com.aplicacion.permisapprh.databinding.ActivityDatosMainBinding
import com.aplicacion.permisapprh.providers.AuthProvider
import com.aplicacion.permisapprh.providers.ClientRHProvider
import org.checkerframework.checker.units.qual.Area

class DatosMainActivity : AppCompatActivity() {
    private val authProvider = AuthProvider()
    private val clientRHProvider = ClientRHProvider()
    private lateinit var binding: ActivityDatosMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDatosMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        alertdialog()
        binding.saveBtn.setOnClickListener { registro() }
        binding.logoutbtn.setOnClickListener { cerrarsesion() }


    }

    private fun registro(){
        val nombre = binding.usuarionametxt.text.toString()
        val apellido = binding.usuarioapellidotxt.text.toString()
        val tel = binding.telphonetxt.text.toString()
        val noEmpleado = binding.noemtxt.text.toString()
        val area = "Recursos Humanos"

        if (validacion(nombre, apellido, tel, noEmpleado, area )) {
            val client = ClientRH(
                idRH = authProvider.getid(),
                nombre = nombre,
                email = authProvider.auth.currentUser?.email,
                apellido = apellido,
                tel = tel,
                noEmpleado = noEmpleado,
                area = area,
            )
            clientRHProvider.create(client).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Registro exitoso",
                        Toast.LENGTH_SHORT).show()

                    val i = Intent(this, MainActivity_home::class.java)
                    //esta linea borra el historial de pantallas
                    i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(i)

                } else {
                    Toast.makeText(this,
                        "Hubo un error al registrarte los datos del usuario ${it.exception.toString()}",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun validacion (nombre:String, apellido:String, tel:String, noEmpleado : String, area: String):Boolean{
        if(nombre.isEmpty()){
            Toast.makeText(this, "Debes ingresar tu(s) Nombre(s)", Toast.LENGTH_SHORT).show()
            return false
        }
        if(apellido.isEmpty()){
            Toast.makeText(this, "Debes ingresar tus Apellidos", Toast.LENGTH_SHORT).show()
            return false
        }
        if(noEmpleado.isEmpty()){
            Toast.makeText(this, "Debes ingresar tu No. de Empleado", Toast.LENGTH_SHORT).show()
            return false
        }


        return true
    }

    private fun  alertdialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@DatosMainActivity)
        builder.setTitle(getString(R.string.datos))
        builder.setMessage(getString(R.string.registrardatos))
        builder.setPositiveButton("Aceptar") { _, _ ->


        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun cerrarsesion() {
        authProvider.logOut()
        val i= Intent(this, MainActivity::class.java )
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)

    }

}