package com.devnic.bluetooothkotlin

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import com.devnic.bluetooothkotlin.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bluetooth: Bluetooth
    private lateinit var dialog: DialogoFragment
    private lateinit var location: RlLocation
    private lateinit var fusedLocation: FusedLocationProviderClient


    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { permisin ->
            bluetooth.searchdevice(permisin)
            getlocation()
        }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        val view = binding.root
        setContentView(view)
        bluetooth = Bluetooth(this)
        location = RlLocation(this)

        serch()
        binding.mostrar.setOnClickListener {
            dialog.show(supportFragmentManager, "DIALOG")
        }
        binding.location.setOnClickListener {
            Toast.makeText(this, "ESTA HABILITADO", Toast.LENGTH_SHORT).show()
            location.getlocation().observeForever {
                if (it == null) {
                    binding.tvubicaion.text = "UBICACION NULA"
                } else {
                    binding.tvubicaion.text = "LOCALIZACION ${it.longitude}  ${it.latitude}"
                }
            }
        }

    }

    fun getlocation(): MutableLiveData<Location> {
        val location: MutableLiveData<Location> = MutableLiveData()
        fusedLocation = LocationServices.getFusedLocationProviderClient(this)
        if (isLocationEnabled()) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                serch()
            }
            fusedLocation.lastLocation.addOnSuccessListener(this) { task ->
                location.value = task
                println("${task.latitude}")
                if (location == null) {
                    println("LOCATION IS NULL")
                } else {
                    println("LOCALIZACION ${location.value?.longitude}  ${location.value?.latitude}")
                    location.postValue(task)
                }
            }
        } else {
            Toast.makeText(this, "ACTIVE LA UBICACION POR FAVOR", Toast.LENGTH_SHORT).show()
        }
        return location
    }


    fun serch() {
        with(getResult) {
            launch(Manifest.permission.ACCESS_FINE_LOCATION)
            launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }


    override fun onResume() {
        super.onResume()
        dialog = DialogoFragment(bluetooth.listdevice)
    }

    @SuppressLint("ServiceCast")
    fun isLocationEnabled(): Boolean {
        //si los servicios de gps estan habilitados, retorna disponible
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }


}



