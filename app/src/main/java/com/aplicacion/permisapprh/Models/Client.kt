package com.aplicacion.permisapprh.Models

import android.widget.TextView
import com.beust.klaxon.*

private val klaxon = Klaxon()

data class Client(
    var id: String ? = null,
    val nombre: String ? = null,
    val apellido: String ? = null,
    val tel: String ? = null,
    val rfc: String?= null,
    var image: String ? = null,
    val area: String ? = null,
    val noEmpleado: String ? = null,
    val email: String ? = null,

    ) {
    public fun toJson() = klaxon.toJsonString(this)

    companion object {
        public fun fromJson(json: String) = klaxon.parse<Client>(json)
    }
}