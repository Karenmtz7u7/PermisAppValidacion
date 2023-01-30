package com.aplicacion.permisapprh.providers

import android.net.Uri
import android.util.Log
import com.aplicacion.permisapprh.Models.Histories
import com.aplicacion.permisapprh.Models.Incidencias
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import java.io.File

class HistoriesProvider {
    val authProvider = AuthProvider()
    val db = Firebase.firestore.collection("Histories")


    fun create(histories: Histories): Task<DocumentReference> {

        return db.add(histories).addOnFailureListener {
            Log.d("FIRESTORE", "ERROR ${it.message}")
        }
    }

    fun getTramits(): Task<QuerySnapshot> {
        return db.get()
    }
    fun getSP(id: String): Task<DocumentSnapshot> {
        return db.document(id.toString()).get()
    }
    //funcion para actualizar la informacion en FireStore



}