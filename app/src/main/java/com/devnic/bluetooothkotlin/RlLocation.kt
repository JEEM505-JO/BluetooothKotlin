package com.devnic.bluetooothkotlin

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.*

@SuppressLint("MissingPermission")
class RlLocation(private val context: Context) {
    private lateinit var fusedLocation: FusedLocationProviderClient

    //    globally declare LocationRequest
    private lateinit var locationRequest: LocationRequest

    // globally declare LocationCallback
    private lateinit var locationCallback: LocationCallback


    fun InitLocationRequest() {
        fusedLocation = LocationServices.getFusedLocationProviderClient(context)
        locationRequest = LocationRequest.create().apply {
            interval = 0
            fastestInterval = 0
            numUpdates = 1
            smallestDisplacement = 170f
            priority = Priority.PRIORITY_HIGH_ACCURACY
            maxWaitTime = 100
        }
    }


    fun updatelocation() {
        fusedLocation = LocationServices.getFusedLocationProviderClient(context)
        fusedLocation.requestLocationUpdates(
            locationRequest, locationCallback, Looper.myLooper()!!
        )
    }

    fun getlocation(): MutableLiveData<Location> {
        val location: MutableLiveData<Location> = MutableLiveData()
        fusedLocation = LocationServices.getFusedLocationProviderClient(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //permisos de ubicacion
        } else {
            Toast.makeText(context, "ACTIVE LA UBICACION POR FAVOR", Toast.LENGTH_SHORT).show()
        }
        fusedLocation.lastLocation.addOnSuccessListener { task ->
            location.value = task
            println("${task.latitude}")
            if (location == null) {
                println("LOCATION IS NULL")
            } else {
                println("LOCALIZACION ${location.value?.longitude}  ${location.value?.latitude}")
                location.postValue(task)
            }
        }
        return location
    }
}


