package com.aplicacion.permisapprh.activities

import android.annotation.SuppressLint
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.aplicacion.permisapprh.Models.ClientRH
import com.aplicacion.permisapprh.databinding.ActivityMainEditProfileBinding
import com.aplicacion.permisapprh.providers.AuthProvider
import com.aplicacion.permisapprh.providers.ClientRHProvider
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker

import java.io.File

class MainActivityEditProfile : AppCompatActivity() {

    private lateinit var binding: ActivityMainEditProfileBinding
    val clientRHProvider = ClientRHProvider()
    val authProvider = AuthProvider()
    private var imageFile: File? = null

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

        getInformation()

        binding= ActivityMainEditProfileBinding.inflate(layoutInflater)
            setContentView(binding.root)

        binding.editprofilebtn.setOnClickListener {
                UpdateInfo()
        }
        binding.Camerabtn.setOnClickListener {selectImage()}

    }


    private fun getInformation() {
        clientRHProvider.getRH(authProvider.getid()).addOnSuccessListener { document ->
            if (document.exists()) {
                val client = document.toObject(ClientRH::class.java)
                binding.usuarionametxt.setText("${client?.nombre}")
                binding.usuarioapellidotxt.setText("${client?.apellido}")
                binding.email.setText("${authProvider.auth.currentUser?.email}")
                binding.Carreratxt.setText("Recursos Humanos")
                binding.noemtxt.setText( "${client?.noEmpleado}")
                binding.telphonetxt.setText("${client?.tel}")

                if (client?.image != null) {
                    if (client.image != "") {
                        Glide.with(this).load(client.image).centerCrop().into(binding.userimagetxt)
                    }
                }
            }
        }
    }

    private fun UpdateInfo(){
        val  nombre = binding.usuarionametxt.text.toString()
        val  apellido = binding.usuarioapellidotxt.text.toString()
        val  tel = binding.telphonetxt.text.toString()

        val client = ClientRH(
            idRH = authProvider.getid(),
            nombre = nombre,
            apellido = apellido,
            tel = tel
        )
        clientRHProvider.update(client).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "Datos actualizados correctamente", Toast.LENGTH_LONG).show()
            }
            else {
                Toast.makeText(this, "No se pudo actualizar la informacion", Toast.LENGTH_LONG).show()
            }
        }
    }

    //esta funcion guardar la imagen dentro de el textView
    private val startImageForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result: ActivityResult ->
        val resultCode = result.resultCode
        val data = result.data
        if (resultCode == Activity.RESULT_OK){
            val fileUri = data?.data
            imageFile = File(fileUri?.path)
            binding.userimagetxt.setImageURI(fileUri)
            imageUpload()

        }else if(resultCode == ImagePicker.RESULT_ERROR){
            Toast.makeText(this@MainActivityEditProfile, ImagePicker.getError(data), Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this@MainActivityEditProfile, "Tarea cancelada", Toast.LENGTH_LONG).show()
        }



    }

    //esta funcion abre la galeria o la opccion para tomar una foto
    private fun selectImage() {
        ImagePicker.with(this@MainActivityEditProfile)
            .crop()
            .compress(1024)
            .maxResultSize(1080,1080)
            .createIntent { intent ->
                startImageForResult.launch(intent)
            }

    }
    private fun imageUpload(){
        val client = ClientRH(
            idRH = authProvider.getid()
        )
        if (imageFile != null) {
            clientRHProvider.uploadImage(authProvider.getid(), imageFile!!)
                .addOnSuccessListener { taskSnapshot ->
                    clientRHProvider.getImageUrl().addOnSuccessListener { url ->
                        val imageUrl = url.toString()
                        client.image = imageUrl
                        clientRHProvider.updateImage(client).addOnCompleteListener {
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








}









