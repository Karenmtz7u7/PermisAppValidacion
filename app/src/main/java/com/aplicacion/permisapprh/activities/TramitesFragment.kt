package com.aplicacion.permisapprh.activities

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.aplicacion.permisapprh.Models.Incidencias
import com.aplicacion.permisapprh.R
import com.aplicacion.permisapprh.adapters.HistoriesAdapter
import com.aplicacion.permisapprh.databinding.FragmentTramitesBinding
import com.aplicacion.permisapprh.providers.IncidenciasProvider

class TramitesFragment : Fragment(R.layout.fragment_tramites) {
    private lateinit var binding: FragmentTramitesBinding
    private var incidenciasProvider = IncidenciasProvider()
    private var histories = ArrayList<Incidencias>()
    private lateinit var adapter: HistoriesAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTramitesBinding.inflate(layoutInflater)
        val linearLayoutManager = LinearLayoutManager(this@TramitesFragment.requireContext())
        binding.RecyclerViewHistorial.layoutManager = linearLayoutManager

        getHistories()

        return binding.root
    }


    private fun getHistories() {

        histories.clear()

        incidenciasProvider.getTramits().addOnSuccessListener {

                    for (document in it){
                        val tramite = document.toObject(Incidencias::class.java)
                         tramite.id = document.id
                        histories.add(tramite)
                    }

                    adapter = HistoriesAdapter(this@TramitesFragment.requireActivity(), histories)
                    binding.RecyclerViewHistorial.adapter    = adapter

            }
        }
}




