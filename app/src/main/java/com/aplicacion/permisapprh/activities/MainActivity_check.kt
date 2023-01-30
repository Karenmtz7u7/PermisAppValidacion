package com.aplicacion.permisapprh.activities


import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.aplicacion.permisapprh.Models.Histories
import com.aplicacion.permisapprh.Models.Incidencias
import com.aplicacion.permisapprh.R
import com.aplicacion.permisapprh.databinding.ActivityMainCheckandFirmBinding
import com.aplicacion.permisapprh.providers.AuthProvider
import com.aplicacion.permisapprh.providers.HistoriesProvider
import com.aplicacion.permisapprh.providers.IncidenciasProvider
import com.github.dhaval2404.imagepicker.ImagePicker
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.Date as Date

class MainActivity_check : AppCompatActivity() {
    private lateinit var binding: ActivityMainCheckandFirmBinding
    var incidenciasProvider = IncidenciasProvider()
    private var incidencias: Incidencias? = null
    var historiesProvider = HistoriesProvider()
    val authProvider = AuthProvider()
    var imagebtn : Button?=null
    private var imageFile: File? = null
    private val PERMISO_STORAGE: Int = 99


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainCheckandFirmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imagebtn = binding.button
        binding.button.setOnClickListener {
            solicitarpermiso()
        }
        binding.aceptarbtn.setOnClickListener {
            update()
        }

        getInformation()
}
    private fun solicitarpermiso() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.CAMERA, )
                        == PackageManager.PERMISSION_GRANTED -> {
                    selectImage()
                }
                shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA) -> {
                    mostrarmensaje("El permiso fue rechazado previamente")
                }
                else -> {
                    requestPermissions(arrayOf(android.Manifest.permission.CAMERA),
                        PERMISO_STORAGE)
                }
            }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERMISO_STORAGE->{
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    selectImage()
                }
            }
            else->{
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }

    private fun mostrarmensaje(mensaje: String) {
        Toast.makeText(applicationContext, mensaje, Toast.LENGTH_LONG).show()
    }


    //esta funcion guardar la imagen dentro de el textView
    private val startImageForResult = registerForActivityResult(ActivityResultContracts.
    StartActivityForResult()) { result: ActivityResult ->
        val resultCode = result.resultCode
        val data = result.data
        if (resultCode == Activity.RESULT_OK) {
            val fileUri = data?.data
            imageFile = File(fileUri?.path)
            binding.firmaRHtxt.setImageURI(fileUri)
            Toast.makeText(this,
                "Firma obtenida correctamente",
                Toast.LENGTH_LONG).show()


        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this,
                ImagePicker.getError(data),
                Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Tarea cancelada", Toast.LENGTH_LONG)
                .show()
        }
    }

    //esta funcion abre la galeria o la opccion para tomar una foto
    private fun selectImage() {
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080,1080)
            .createIntent { intent ->
                startImageForResult.launch(intent)
            }

    }

    private fun update(){
        val id = intent.getStringExtra("id")
        val status = "Aceptado por RH"
        //hora y dia
        val date = Date()
        val fechaC = SimpleDateFormat("dd'/'MM'/'yyyy")
        val sFecha: String = fechaC.format(date)

        val format = SimpleDateFormat("hh:mm")
        val sTime : String = format.format(Date())
        val incidencias = Incidencias(
            id = id,
            status = status,
            fecha = sFecha,
            hora = sTime
        )
        if (imageFile != null) {
            incidenciasProvider.uploadImageFirmaRH(id.toString(), imageFile!!)
                .addOnSuccessListener { taskSnapshot ->
                    incidenciasProvider.getImageUrlFirmaRH().addOnSuccessListener { url ->
                        val imageUrl = url.toString()
                        incidencias.firmaRH = imageUrl
                        Log.d("STORAGE", "$imageUrl")

                        incidenciasProvider.updateRH(incidencias, id.toString()).addOnCompleteListener {
                            if (it.isSuccessful) {

                                historiesAdd()
                            } else {
                                Toast.makeText(this,
                                    "Hubo un error al procesar la solicitud ${it.exception.toString()}",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
        }
    }


    private fun getInformation() {
        val id = intent.getStringExtra("id")

        incidenciasProvider.getSP(id.toString()).addOnSuccessListener { document->
           if (document.exists()){
               val incidencia = document.toObject(Incidencias::class.java)
               if(incidencia != null){
                   binding.foliochecktxt.setText(incidencia.folio)
                   binding.fechaSolicitudchecktxt.setText(incidencia.fechaSolicitud)
                   binding.areachecktxt.setText(incidencia.area)
                   binding.incideciachecktxt.setText(incidencia.tipoIncidencia)
                   binding.horainicialchecktxt.setText(incidencia.horaInicial)
                   binding.Horafinalchecktxt.setText(incidencia.horaFinal)
                   binding.fechasolictadachecktxt.setText(incidencia.fechaSolicitada)
                   binding.fechainicialchecktxt.setText(incidencia.fechaInicial)
                   binding.fechafinalchecktxt.setText(incidencia.fechaFinal)
                   binding.evidenciachecktxt.setText(incidencia.evidencia)
                   binding.razonchecktxt.setText(incidencia.razon)
                   binding.nombrespchecktxt.setText("${incidencia.nombre} ${incidencia.apellido}")
                   binding.noEmpleadochecktxt.setText(incidencia.noEmpleado)

               }
           }
        }
    }

    private fun historiesAdd(){
        val id = intent.getStringExtra("id")
        //estas variables son para agregar la fecha actual para mostrar cuando se registro/hizo la solictud
        val date = Date()
        val fechaC = SimpleDateFormat("dd'/'MM'/'yyyy")
        val sFecha: String = fechaC.format(date)

        val format = SimpleDateFormat("hh:mm")
        val sTime : String = format.format(Date())

        incidenciasProvider.getSP(id.toString()).addOnSuccessListener { document ->
            val incidencia = document.toObject(Incidencias::class.java)
            val idSP = incidencia?.idSP
            val idRH = incidencia?.idRH
            val folio = incidencia?.folio
            val area = incidencia?.area
            val tipoIncidencia = incidencia?.tipoIncidencia
            val horaInicial = incidencia?.horaInicial
            val horaFinal = incidencia?.horaFinal
            val fechaSolicitada = incidencia?.fechaSolicitada
            val fechaInicial = incidencia?.fechaInicial
            val fechaFinal = incidencia?.fechaFinal
            val razon = incidencia?.razon
            val noEmpleado = incidencia?.noEmpleado
            val nombre = incidencia?.nombre
            val apellido = incidencia?.apellido
            val firmaRH = incidencia?.firmaRH
            val firmaSP = incidencia?.firmaSP

            val history = Histories(
                idSP = idSP,
                idRH = idRH,
                folio = folio,
                area = area,
                tipoIncidencia = tipoIncidencia,
                horaInicial = horaInicial,
                horaFinal = horaFinal,
                fechaSolicitada = fechaSolicitada,
                fechaInicial = fechaInicial,
                fechaFinal = fechaFinal,
                razon = razon,
                noEmpleado = noEmpleado,
                nombre = nombre,
                apellido = apellido,
                firmaRH = firmaRH,
                firmaSP = firmaSP,
                fecha = sFecha,
                hora = sTime,
            )
        historiesProvider.create(history).addOnCompleteListener {
            if (it.isSuccessful){
                showMessage()
                }
            }
        }
    }

    private fun showMessage(){
        val view = View.inflate(this, R.layout.dialog_view, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(view)

        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        view.findViewById<Button>(R.id.botonAlert).setOnClickListener {

            incidenciaDelet()
            dialog.dismiss()
        }
    }
    private fun home(){
        val intent = Intent(this, MainActivity_home::class.java)
        startActivity(intent)
    }

    private fun incidenciaDelet(){
        val id = intent.getStringExtra("id")
        incidenciasProvider.remove(id.toString()).addOnCompleteListener {
            home()
        }

    }


}
