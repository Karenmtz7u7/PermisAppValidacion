package com.aplicacion.permisapprh.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aplicacion.permisapprh.Models.Client
import com.aplicacion.permisapprh.R
import com.aplicacion.permisapprh.activities.MainActivity_check

class UsersAdapter(val context: Activity, val histories: ArrayList<Client>):
    RecyclerView.Adapter<UsersAdapter.UsersAdapterViewHolder>() {

     //var onItemClick : ((Incidencias) -> Unit)? = null


    // Sirve para instanciar la lista que se va a mostrar en el historial
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_add_user,parent,false)
        return UsersAdapterViewHolder(view)


    }


    // Se establece la información en los textView
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: UsersAdapterViewHolder, position: Int) {

        val client = histories[position] //Se obtiene un solo historial
        holder.namesp.text = "${client.nombre} ${client.apellido}"
        holder.area.text = client.area
        holder.noEmpleado.text = client.noEmpleado
        holder.rfc.text = client.rfc

//        holder.checkViewbtn.setOnClickListener { v->
//            val intent = Intent(v.context, MainActivity_check::class.java).apply {
//                putExtra("id", incidencias.id)
//                putExtra("folio", incidencias.folio)
//                putExtra("fechaSolicitud", incidencias.fechaSolicitud)
//                putExtra("area", incidencias.area)
//                putExtra("tipoIncidencia", incidencias.tipoIncidencia)
//                putExtra("horaInicial", incidencias.horaInicial)
//                putExtra("horaFinal", incidencias.horaFinal)
//                putExtra("fechaSolicitada", incidencias.fechaSolicitada)
//                putExtra("fechaInicial", incidencias.fechaInicial)
//                putExtra("fechaFinal", incidencias.fechaFinal)
//                putExtra("evidencia", incidencias.evidencia)
//                putExtra("razon", incidencias.razon)
//                putExtra("nombre", incidencias.nombre)
//                putExtra("apellido", incidencias.apellido)
//                putExtra("noEmpleado", incidencias.noEmpleado)
//                putExtra("idRH", incidencias.idRH)
//                putExtra("firmaRH", incidencias.firmaRH)
//                putExtra("status", incidencias.status)
//
//            }
//            v.context.startActivity(intent)
//           // onItemClick?.invoke(incidencias)
//        }

    }



    // Sirve para mostrar el tamaño de la lista que se va a mostrar
    override fun getItemCount(): Int {
        return histories.size
    }



    //Clase que permite instanciar las vistas que tienen ID en el layout
    class UsersAdapterViewHolder(view: View): RecyclerView.ViewHolder(view){

        val namesp: TextView
        val area: TextView
        val noEmpleado: TextView
        val rfc : TextView

        //constructor para inicializar las variables
        init {
            namesp = view.findViewById(R.id.nameusertxt)
            area = view.findViewById(R.id.areatxt)
            noEmpleado = view.findViewById(R.id.numberjobtxt)
            rfc = view.findViewById(R.id.rfctxt)

        }

    }

}