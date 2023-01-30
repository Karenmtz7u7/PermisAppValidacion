package com.aplicacion.permisapprh.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.aplicacion.permisapprh.Models.Client
import com.aplicacion.permisapprh.R
import com.aplicacion.permisapprh.adapters.UsersAdapter
import com.aplicacion.permisapprh.databinding.FragmentAddUserBinding
import com.aplicacion.permisapprh.providers.ClientProvider

class AddUserFragment : Fragment(R.layout.fragment_add_user) {
    private lateinit var binding: FragmentAddUserBinding
    private var clientProvider = ClientProvider()
    private var histories = ArrayList<Client>()
    private lateinit var adapter: UsersAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddUserBinding.inflate(layoutInflater)
        val linearLayoutManager = LinearLayoutManager(this@AddUserFragment.requireContext())
        binding.RecyclerViewUsers.layoutManager = linearLayoutManager

        getHistories()

        return binding.root
    }
    private fun getHistories() {

        histories.clear()
        clientProvider.getuser().addOnSuccessListener {

            for (document in it){
                val history = document.toObject(Client::class.java)
                history.id = history.id
                histories.add(history!!)

            }

            adapter = UsersAdapter(this@AddUserFragment.requireActivity(), histories)
            binding.RecyclerViewUsers.adapter    = adapter

        }
    }

}