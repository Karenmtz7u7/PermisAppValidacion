package com.aplicacion.permisapprh.Models

import com.beust.klaxon.Klaxon
import com.google.firebase.firestore.Exclude

private val klaxon = Klaxon()
data class Histories(
    @get:Exclude var id: String? = null,
    val idSP: String ? = null,
    val idRH: String ? = null,
    val folio: String? = null,
    val area: String ? = null,
    val tipoIncidencia:String?=null,
    val horaInicial:String?= null,
    val horaFinal:String?=null,
    val fechaSolicitada: String ?=null, //fecha que se solicita en la incidencia
    val fechaInicial: String?=null,
    val fechaFinal:String?=null,
    val razon: String?=null,
    val noEmpleado: String ? = null,
    val nombre: String? = null,
    val apellido: String? = null,
    val jefeInmediato: String?=null,
    var firmaRH:String?=null,
    var firmaSP:String?=null,
    val hora: String? = null,
    val fecha: String? = null, //fecha de aprobacion


    ) { public fun toJson() = klaxon.toJsonString(this)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Incidencias

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    companion object {
        public fun fromJson(json: String) = klaxon.parse<Histories>(json)
    }



}