package com.devnic.bluetooothkotlin

/**
 * Buscar dispositivos bluetooth
 * @param BluetoothDevice
 * @author Joaquin Espinoza
 */

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.widget.Toast
import androidx.lifecycle.MutableLiveData

@SuppressLint("MissingPermission")
class Bluetooth(private val context: Context) {

    val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    val bluetoothAdapter = bluetoothManager.adapter
    val listdevice: MutableLiveData<List<BluetoothDevice>> = MutableLiveData()
    val list = mutableListOf<BluetoothDevice>()

    fun BluetoothState(): MutableLiveData<Boolean> {
        val status: MutableLiveData<Boolean> = MutableLiveData()
        if (bluetoothAdapter.isEnabled) {
            status.postValue(true)
        } else {
            status.postValue(false)
        }
        return status
    }

    fun enable() {
        bluetoothAdapter.enable()
    }

    fun disable() {
        bluetoothAdapter.disable()
    }

    fun searchdevice(permiso: Boolean) {
        if (permiso) {
            val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices

            if (pairedDevices != null) {
                list.addAll(pairedDevices)
            }
            val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
            val receiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    intent?.let { it ->
                        when (it.action!!) {
                            BluetoothDevice.ACTION_FOUND -> {
                                val device: BluetoothDevice? =
                                    it.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                                device?.let { it1 ->
                                    println("NAME: ${it1.name}, ADDRESS: ${it1.address}")
                                    list.add(it1)
                                }
                            }
                            else -> {
                                println("NAME: ACTION NO FOUND")
                            }
                        }
                    }
                }
            }
            listdevice.postValue(list)
            context.registerReceiver(receiver, filter)
            bluetoothAdapter.startDiscovery()
            return
        } else {
            Toast.makeText(context, "Permiso denegado", Toast.LENGTH_SHORT).show()
        }
    }

    fun getdevice(): MutableLiveData<List<BluetoothDevice>> {
        val devices: MutableLiveData<List<BluetoothDevice>> = MutableLiveData()
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
        pairedDevices?.forEach {
            devices.postValue(listOf(it))
        }
        devices.postValue(list)
        return devices
    }
}