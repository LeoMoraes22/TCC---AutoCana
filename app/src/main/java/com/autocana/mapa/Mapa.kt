package com.autocana.mapa

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.autocana.mapa.ui.mapa.MapaFrag
import com.autocana.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Mapa : AppCompatActivity() {

    private var REQUEST_LOCATION_PERMISION=1
    private lateinit var mFusedLocationClient:FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_mapa)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MapaFrag.newInstance())
                    .commitNow()
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISION)
        }else{
            //get location
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==REQUEST_LOCATION_PERMISION){
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                //get location
            }else{
                Toast.makeText(this, "Permissaõ de localização negada!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}