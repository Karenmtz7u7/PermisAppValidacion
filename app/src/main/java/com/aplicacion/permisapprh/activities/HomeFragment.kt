package com.aplicacion.permisapprh.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.aplicacion.permisapprh.Models.ClientRH
import com.aplicacion.permisapprh.R
import com.aplicacion.permisapprh.databinding.FragmentHomeBinding
import com.aplicacion.permisapprh.providers.AuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.aplicacion.permisapprh.providers.*
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.phone.SmsRetriever.getClient
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    val clientRHProvider = ClientRHProvider()
    val authProvider = AuthProvider()
    private val modalMenu = BottomSheetFragment()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(layoutInflater)


        binding.iconPerson.setOnClickListener {
            val intent = Intent(this@HomeFragment.requireContext(), MainActivityPerfil::class.java)
            startActivity(intent)
        }
        binding.emaildefaultxt.setOnClickListener {
            val intent = Intent(this@HomeFragment.requireContext(), MainActivityPerfil::class.java)
            startActivity(intent)
        }

        val bottomSheetFragment = BottomSheetFragment()
        binding.menubtn.setOnClickListener {
            fragmentManager?.let { it1 -> bottomSheetFragment.show(it1, "BottomSheetDialog") }

        }

        return binding.root

    }

    override fun onStart() {
        super.onStart()
        getClient()

    }

    private fun getClient() {
        clientRHProvider.getRH(authProvider.getid()).addOnSuccessListener { document ->
            if (document.exists()) {
                val client = document.toObject(ClientRH::class.java)
                binding.emaildefaultxt.text = "${client?.nombre} ${client?.apellido}"
                if (client?.image != null) {
                    if (client.image != "") {
                        Glide.with(this@HomeFragment.requireContext()).load(client.image).centerCrop().into(binding.userimagetxt)
                    }
                }
            }
        }
    }


}
















