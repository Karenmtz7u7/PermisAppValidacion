package com.aplicacion.permisapprh.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.aplicacion.permisapprh.Models.Client
import com.aplicacion.permisapprh.Models.ClientRH
import com.aplicacion.permisapprh.databinding.ActivityMainPerfilBinding
import com.aplicacion.permisapprh.providers.AuthProvider
import com.aplicacion.permisapprh.providers.ClientRHProvider
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import java.io.File


class MainActivityPerfil : AppCompatActivity() {
    private lateinit var binding: ActivityMainPerfilBinding
    private var imageFile: File? = null
    val clientrhProvider = ClientRHProvider()
    val authProvider = AuthProvider()

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding= ActivityMainPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backbtn.setOnClickListener {
            startActivity(Intent(this, MainActivity_home::class.java))
            finish()
        }
        binding.editprofilebtn.setOnClickListener {
            startActivity(Intent(this, MainActivityEditProfile::class.java))
            finish()
        }
        binding.Camerabtn.setOnClickListener {selectImage()}
        binding.keytxt.setOnClickListener {

        }
        binding.deleteimgtxt.setOnClickListener {
        }
        getInformationUser()
    }
    //esta funcion guardar la imagen dentro de el textView
    private val startImageForResult = registerForActivityResult(ActivityResultContracts.
    StartActivityForResult()){ result: ActivityResult ->
        val resultCode = result.resultCode
        val data = result.data
        if (resultCode == Activity.RESULT_OK){
            val fileUri = data?.data
            imageFile = File(fileUri?.path)
            binding.userimagetxt.setImageURI(fileUri)
            updateInfo()
        }else if(resultCode == ImagePicker.RESULT_ERROR){
            Toast.makeText(this@MainActivityPerfil, ImagePicker.getError(data), Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this@MainActivityPerfil, "Tarea cancelada", Toast.LENGTH_LONG).show()
        }
    }

    //esta funcion abre la galeria o la opccion para tomar una foto
    private fun selectImage() {
        ImagePicker.with(this@MainActivityPerfil)
            .crop()
            .compress(1024)
            .maxResultSize(1080,1080)
            .createIntent { intent ->
                startImageForResult.launch(intent)
            }

    }
    private fun updateInfo(){
        val clientRH = ClientRH(
            idRH = authProvider.getid()
        )
        if (imageFile != null) {
            clientrhProvider.uploadImage(authProvider.getid(), imageFile!!)
                .addOnSuccessListener { taskSnapshot ->
                    clientrhProvider.getImageUrl().addOnSuccessListener { url ->
                        val imageUrl = url.toString()
                        clientRH.image = imageUrl
                        clientrhProvider.updateImage(clientRH).addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(this,
                                    "Imagen actualizada correctamente",
                                    Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(this,
                                    "No se pudo actualizar la foto",
                                    Toast.LENGTH_LONG).show()
                            }
                        }
                        Log.d("STORAGE", "$imageUrl")
                    }
                }
        }
    }
    //esta funcion obtiene los datos ingresados en FireStore
    private fun getInformationUser() {
        clientrhProvider.getRH(authProvider.getid()).addOnSuccessListener { document ->
            if (document.exists()) {
                val client = document.toObject(ClientRH::class.java)
                binding.nameusertxt.text = "${client?.nombre} ${client?.apellido}"
                binding.emailusertxt.text = "${client?.email}"
                binding.numberjobtxt.text = "${client?.noEmpleado}"
                binding.phoneusertxt.text = "${client?.tel}"
                binding.areatxt.text = "${client?.area}"

                if (client?.image != null) {
                    if (client.image != "") {
                        Glide.with(this).load(client.image).centerCrop().into(binding.userimagetxt)
                    }
                }
            }
        }
    }






}