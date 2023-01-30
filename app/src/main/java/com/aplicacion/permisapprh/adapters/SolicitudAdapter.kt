package com.aplicacion.permisapprh.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aplicacion.permisapprh.Models.Incidencias
import com.aplicacion.permisapprh.R

class SolicitudAdapter(val context: Activity, val histories: ArrayList<Incidencias>):
    RecyclerView.Adapter<SolicitudAdapter.HistoriesAdapterViewHolder>() {


    // Sirve para instanciar la lista que se va a mostrar en el historial
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoriesAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.check_tramite_cardview,parent,false)
        return HistoriesAdapterViewHolder(view)
    }


    // Se establece la información en los textView
    override fun onBindViewHolder(holder: HistoriesAdapterViewHolder, position: Int) {

        val incidencias = histories[position] //Se obtiene un solo historial
        holder.namesp.text = incidencias.nombre; incidencias.apellido
        holder.tipoincidenciaHistorial.text = incidencias.tipoIncidencia
        holder.fechaHistorial.text = incidencias.fechaSolicitud


    }

   // Sirve para mostrar el tamaño de la lista que se va a mostrar
    override fun getItemCount(): Int {
        return histories.size
    }



    //Clase que permite instanciar las vistas que tienen ID en el layout
    class HistoriesAdapterViewHolder(view: View): RecyclerView.ViewHolder(view){

        val tipoincidenciaHistorial: TextView
        val fechaHistorial: TextView
        val namesp: TextView

        //constructor para inicializar las variables
        init {
            namesp = view.findViewById(R.id.nameSPtxt)
            tipoincidenciaHistorial = view.findViewById(R.id.tipoincidenciaHistorial)
            fechaHistorial = view.findViewById(R.id.fechaHistorial)
        }

    }



}