package com.example.plant_leaf_disase_app

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.IOException
import java.util.*


class location : AppCompatActivity() {

    val MY_PREFS_NAME = "MyPrefsFile"

    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    var lat: String? = null
    var log: String? = null
    var address: String? =null
    var txtlocation: TextView? = null


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        val btn = findViewById<Button>(R.id.button)
          txtlocation= findViewById<TextView>(R.id.location)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)




        btn.setOnClickListener {

            getlocation()
        }
    }



    private fun getlocation() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationProviderClient!!.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val geocoder = Geocoder(applicationContext, Locale.getDefault())
                try {
                    val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)


                    lat = addresses[0].latitude.toString()
                    log = addresses[0].longitude.toString()
                    address = addresses[0].getAddressLine(0)
                    val city = addresses[0].locality

                    txtlocation!!.setText(city.toString())


//                    Toast.makeText(applicationContext,city.toString(), Toast.LENGTH_LONG).show()

                    val sharedPreferences = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE)
                    val myEdit = sharedPreferences.edit()
                    myEdit.putString("address",city)

                    myEdit.apply()
                    myEdit.commit()




                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }



    }
    }
