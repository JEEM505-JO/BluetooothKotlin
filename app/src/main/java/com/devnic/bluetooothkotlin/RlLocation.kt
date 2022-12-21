package com.devnic.bluetooothkotlin

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

@SuppressLint("MissingPermission")
class RlLocation(private val context: Context) {
    private lateinit var fusedLocation: FusedLocationProviderClient



    fun getlocation(): MutableLiveData<Location> {
        val location: MutableLiveData<Location> = MutableLiveData()
        fusedLocation = LocationServices.getFusedLocationProviderClient(context)

        fusedLocation.lastLocation.addOnSuccessListener { task ->
            if (task == null) {
                println("LOCATION IS NULL")
            } else {
                println("LOCALIZATION ${location.value?.longitude}  ${location.value?.latitude}")
                location.postValue(task)
            }
        }
        return location
    }
}


