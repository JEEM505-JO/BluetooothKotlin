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
    val listparent = mutableListOf<BluetoothDevice>()
    val listsearch = mutableListOf<BluetoothDevice>()
    val principalList = mutableListOf<BluetoothDevice>()


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
                listparent.addAll(pairedDevices)
            }
            if (principalList.size <= 0) {
                principalList.addAll(listparent)
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
                                    compareDevices(listparent, it1.address).let { boolean ->
                                        if (!boolean) {
                                            principalList.add(it1)
                                        }
                                    }
                                }
                            }
                            else -> {
                                println("NAME: ACTION NO FOUND")
                            }
                        }
                    }
                }
            }


            if (listsearch.size > 0) {
                for (itemP in principalList.indices) {
                    for (searchItem in listsearch.indices) {
                        if (listsearch[searchItem].address != principalList[itemP].address) {
                            principalList.add(listsearch[searchItem])
                        }
                    }
                }
            }




            listdevice.postValue(principalList)
            context.registerReceiver(receiver, filter)
            bluetoothAdapter.startDiscovery()
            return
        } else {
            Toast.makeText(context, "Permiso denegado", Toast.LENGTH_SHORT).show()
        }
    }

    fun compareDevices(list: List<BluetoothDevice>, device: String): Boolean {
        var tru: Int = 0
        for (item in list.indices) {
            if (list[item].address == device) {
                tru++
            }
        }
        if (tru > 0) {
            return true
        }
        return false
    }

    fun getdevice(): MutableLiveData<List<BluetoothDevice>> {
        val devices: MutableLiveData<List<BluetoothDevice>> = MutableLiveData()
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
        pairedDevices?.forEach {
            devices.postValue(listOf(it))
        }
//        devices.postValue(list)
        return devices
    }
}
