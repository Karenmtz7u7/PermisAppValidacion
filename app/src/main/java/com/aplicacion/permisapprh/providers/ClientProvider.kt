package com.aplicacion.permisapprh.providers

import android.app.ProgressDialog
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.aplicacion.permisapprh.Models.Client
import com.google.android.gms.tasks.Task
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import java.io.File

class ClientProvider {
    val db = Firebase.firestore.collection("UserServerP")

    var storage = FirebaseStorage.getInstance().getReference().child("UserServerP")

    //esta funcion inserta los datos en FireStorage solo si el usuario ya esta registrado y existe
    //en la base de datos
    fun create(client : Client): Task<Void>{
        return db.document(client.id!!).set(client)
    }
    //esta funcion va a guardar la foto de perfil en fireStorage
    fun uploadImage(id: String, file: File): StorageTask<UploadTask.TaskSnapshot> {
        val fromFile = Uri.fromFile(file)
        val ref = storage.child("$id.jpg")
        storage = ref
        val uploadTask = ref.putFile(fromFile)

        return uploadTask.addOnFailureListener {
            Log.d("STORAGE", "ERROR: ${it.message}")
        }
    }


    //funcion para mandar a llamar el id del usuario
    fun getSP(id: String): Task<DocumentSnapshot>{
        return db.document(id).get()
    }

    fun getuser(): Task<QuerySnapshot> {
        return db.get()
    }

    //funcion para actualizar la informacion en FireStore
    fun update(client: Client):  Task<Void> {
        val info: MutableMap<String, Any> = HashMap()
        info["nombre"] = client.nombre!!
        info["apellido"] = client.apellido!!
        info["tel"] = client.tel!!

        return db.document(client.id!!).update(info)
    }

    fun updateImage(client: Client):  Task<Void> {
        val info: MutableMap<String, Any> = HashMap()
       info["image"] = client.image!!
        return db.document(client.id!!).update(info)
    }

    fun getImageUrl(): Task<Uri> {
        return storage.downloadUrl
    }

}