package com.devnic.bluetooothkotlin

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.devnic.bluetooothkotlin.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import java.text.SimpleDateFormat
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
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
        }

    @RequiresApi(Build.VERSION_CODES.Q)
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
                    binding.tvubicaion.text = "LOCALIZACION ${it.latitude}  ${it.longitude}"
                }
            }
        }

        binding.getdate.setOnClickListener {
            binding.movil.text = getDateMobile()
            dayBetweenDates()
        }
    }


    fun getDateMobile(): String {
        val date = Calendar.getInstance().time
//        val formatter = SimpleDateFormat.getDateTimeInstance() //or use getDateInstance()
        val formatter = SimpleDateFormat("yyyy/MM/dd")
        val formatedDate = formatter.format(date)
        return formatedDate
    }


    @SuppressLint("SimpleDateFormat")
    fun dayBetweenDates() {
        val fechaactual = Date(System.currentTimeMillis())
        val fechateorica = "2023/12/24"

        val date = SimpleDateFormat("yyyy/MM/dd")
        val fechaInicioDate = date.parse(fechateorica)
        if (fechaInicioDate != null) {
            if (fechaInicioDate.after(fechaactual)) {
                println("Fecha inicio mayor")
                //CONFIGURAR FECHA DE LECTURA TEORICA
            } else {
                println("Fecha actual mayor")
            }
        }

    }


    @RequiresApi(Build.VERSION_CODES.Q)
    fun serch() {
        with(getResult) {
            launch(Manifest.permission.ACCESS_FINE_LOCATION)
            launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }
    }


    override fun onResume() {
        super.onResume()
        bluetooth.listdevice.observeForever {
            dialog = DialogoFragment(it)
        }
    }


}



