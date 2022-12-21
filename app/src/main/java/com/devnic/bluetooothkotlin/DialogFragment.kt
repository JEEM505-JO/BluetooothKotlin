package com.devnic.bluetooothkotlin

import android.bluetooth.BluetoothDevice
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.devnic.bluetooothkotlin.databinding.FragmentDialogBinding

class DialogoFragment(private var list: List<BluetoothDevice>) : DialogFragment() {
    private lateinit var binding: FragmentDialogBinding
    private lateinit var bluetooth: Bluetooth
    private lateinit var adapterRv: AdapterRv


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dialog, container, false)
        binding.lifecycleOwner = this
        bluetooth = Bluetooth(requireContext())
        adapterRv = AdapterRv()

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adapterRv
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        list.let {
            adapterRv.submitList(it)
        }
    }


}