package com.aplicacion.permisapprh.providers

import android.net.Uri
import android.util.Log
import com.aplicacion.permisapprh.Models.ClientRH
import com.aplicacion.permisapprh.Models.Incidencias
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import java.io.File
import java.text.Format.Field

class IncidenciasProvider {
    val authProvider = AuthProvider()
    val db = Firebase.firestore.collection("Incidencias")
    var storage = FirebaseStorage.getInstance().getReference().child("firmas")


    fun updateRH(incidencias: Incidencias, id: String): Task<Void> {
        val info: MutableMap<String, Any> = HashMap()
        info["status"]=incidencias.status!!
        info["firmaRH"] = incidencias.firmaRH!!
        info["hora"] = incidencias.hora!!
        info["fecha"] = incidencias.fecha!!
        return db.document(id.toString()).update((info))
    }

    fun getImageUrlFirmaRH(): Task<Uri> {
        return storage.downloadUrl
    }

    //esta funcion va a guardar la foto de firmas en fireStorage
    fun uploadImageFirmaRH(idRH: String, file: File): StorageTask<UploadTask.TaskSnapshot> {
        val fromFile = Uri.fromFile(file)
        val ref = storage.child("$idRH.jpg")
        storage = ref
        val uploadTask = ref.putFile(fromFile)

        return uploadTask.addOnFailureListener {
            Log.d("STORAGE", "ERROR: ${it.message}")
        }
    }
    fun getTramits(): Task<QuerySnapshot> {
        return db.get()
    }

    //funcion para obtener todo el documento
    fun getSP(id: String): Task<DocumentSnapshot> {
        return db.document(id.toString()).get()
    }
    //funcion para actualizar la informacion en FireStore

    fun remove(id : String): Task<Void> {
        return db.document(id.toString()).delete()
    }





}


