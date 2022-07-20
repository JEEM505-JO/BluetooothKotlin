package com.devnic.bluetooothkotlin

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.devnic.bluetooothkotlin.AdapterRv.viewHolder
import com.devnic.bluetooothkotlin.databinding.ItemcardBinding

class AdapterRv : ListAdapter<BluetoothDevice, viewHolder>(ModelComparator()) {

    class viewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemcardBinding.bind(view)

        @SuppressLint("MissingPermission")
        fun bin(device: BluetoothDevice) {
            binding.tvmac.text = device.address
            binding.tvnombre.text = device.name
        }
        companion object {
            fun create(parent: ViewGroup): viewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.itemcard, parent, false)
                return viewHolder(view)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        return viewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.bin(getItem(position))
    }
}

private class ModelComparator : DiffUtil.ItemCallback<BluetoothDevice>() {
    override fun areItemsTheSame(oldItem: BluetoothDevice, newItem: BluetoothDevice): Boolean =
        oldItem.address == newItem.address

    override fun areContentsTheSame(oldItem: BluetoothDevice, newItem: BluetoothDevice): Boolean =
        oldItem == newItem

}
