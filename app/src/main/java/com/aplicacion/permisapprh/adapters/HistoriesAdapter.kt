package com.aplicacion.permisapprh.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aplicacion.permisapprh.Models.Incidencias
import com.aplicacion.permisapprh.R
import com.aplicacion.permisapprh.activities.MainActivity_check

class HistoriesAdapter(val context: Activity, val histories: ArrayList<Incidencias>):
    RecyclerView.Adapter<HistoriesAdapter.HistoriesAdapterViewHolder>() {

     //var onItemClick : ((Incidencias) -> Unit)? = null


    // Sirve para instanciar la lista que se va a mostrar en el historial
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoriesAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.check_tramite_cardview,parent,false)
        return HistoriesAdapterViewHolder(view)


    }


    // Se establece la información en los textView
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HistoriesAdapterViewHolder, position: Int) {

        val incidencias = histories[position] //Se obtiene un solo historial
        holder.namesp.text = "${incidencias.nombre} ${incidencias.apellido}"
        holder.tipoincidenciaHistorial.text = incidencias.tipoIncidencia
        holder.fechaHistorial.text = incidencias.fechaSolicitud
        holder.horaHistorial.text = incidencias.hora

        holder.checkViewbtn.setOnClickListener { v->
            val intent = Intent(v.context, MainActivity_check::class.java).apply {
                putExtra("id", incidencias.id)
                putExtra("folio", incidencias.folio)
                putExtra("fechaSolicitud", incidencias.fechaSolicitud)
                putExtra("area", incidencias.area)
                putExtra("tipoIncidencia", incidencias.tipoIncidencia)
                putExtra("horaInicial", incidencias.horaInicial)
                putExtra("horaFinal", incidencias.horaFinal)
                putExtra("fechaSolicitada", incidencias.fechaSolicitada)
                putExtra("fechaInicial", incidencias.fechaInicial)
                putExtra("fechaFinal", incidencias.fechaFinal)
                putExtra("evidencia", incidencias.evidencia)
                putExtra("razon", incidencias.razon)
                putExtra("nombre", incidencias.nombre)
                putExtra("apellido", incidencias.apellido)
                putExtra("noEmpleado", incidencias.noEmpleado)
                putExtra("idRH", incidencias.idRH)
                putExtra("firmaRH", incidencias.firmaRH)
                putExtra("status", incidencias.status)

            }
            v.context.startActivity(intent)
           // onItemClick?.invoke(incidencias)
        }

    }



    // Sirve para mostrar el tamaño de la lista que se va a mostrar
    override fun getItemCount(): Int {
        return histories.size
    }



    //Clase que permite instanciar las vistas que tienen ID en el layout
    class HistoriesAdapterViewHolder(view: View): RecyclerView.ViewHolder(view){

        val namesp: TextView
        val tipoincidenciaHistorial: TextView
        val fechaHistorial: TextView
        val horaHistorial : TextView
        val checkViewbtn : Button

        //constructor para inicializar las variables
        init {
            namesp = view.findViewById(R.id.nameSPtxt)
            tipoincidenciaHistorial = view.findViewById(R.id.tipoincidenciaHistorial)
            fechaHistorial = view.findViewById(R.id.fechaHistorial)
            horaHistorial = view.findViewById(R.id.horaHistorial)
            checkViewbtn = view.findViewById(R.id.checkviewbtn)
        }

    }







}