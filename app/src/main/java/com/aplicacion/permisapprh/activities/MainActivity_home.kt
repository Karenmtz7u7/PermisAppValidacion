package com.aplicacion.permisapprh.activities

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import com.aplicacion.permisapprh.R
import com.aplicacion.permisapprh.databinding.ActivityMainHomeBinding
import com.aplicacion.permisapprh.providers.AuthProvider
import com.aplicacion.permisapprh.providers.ClientRHProvider


class MainActivity_home : AppCompatActivity() {
    private lateinit var binding: ActivityMainHomeBinding
    val clientRHProvider = ClientRHProvider()
    val authProvider = AuthProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainHomeBinding.inflate(layoutInflater)

        setContentView(binding.root)


        val homefragment = HomeFragment()
        val notifyfragment = NotifyFragment()
        val tramitesfragment = TramitesFragment()
        val addUserFragment = AddUserFragment()

        binding.bottomNavigationView.setOnNavigationItemSelectedListener{
            when (it.itemId) {
                R.id.nav_inicio -> {
                    setCurrentFragment(homefragment)
                    true
                }
                R.id.nav_notificacion -> {
                    setCurrentFragment(notifyfragment)
                    true
                }
                R.id.nav_tramites -> {
                    setCurrentFragment(tramitesfragment)
                    true
                }
                R.id.nav_solicitudes -> {
                    setCurrentFragment(addUserFragment)
                    true
                }

                else -> false
            }
        }

        binding.bottomNavigationView.getOrCreateBadge(R.id.nav_notificacion).apply {
            isVisible = true
            number
        }
        createToken()
        checkInformationRH()

    }

    private fun setCurrentFragment(fragment : Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.containerView, fragment)
            commit()
        }
    }

    private fun checkInformationRH(){
        clientRHProvider.getRH(authProvider.getid()).addOnSuccessListener { document->
            if (document.exists()){
//                val i = Intent(this, MainActivity::class.java)
//                //esta linea borra el historial de pantallas
//                i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//                startActivity(i)

            }else{
               GoDate()
            }

        }
    }



    private fun GoDate(){
        val i = Intent(this, DatosMainActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(i)
    }
    private fun createToken(){
        clientRHProvider.createToken(authProvider.getid())
    }



}


