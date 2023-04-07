package fr.isen.auphan.androidsmartdevice

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ExpandableListView.OnChildClickListener
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.isen.auphan.androidsmartdevice.databinding.ActivityRecyclerviewAdapterBinding


class ScanAdapter(var devices: ArrayList<BluetoothDevice>, var onDeviceClickListener: (BluetoothDevice)-> Unit) : RecyclerView.Adapter<ScanAdapter.ScanViewHolder>() {
    private var rssiValues = arrayListOf<Int>()
    fun addDevice(device: BluetoothDevice) {
        var shouldAddDevice = true
        devices.forEachIndexed { index, bluetoothDevice ->
            if (bluetoothDevice.address == device.address) {
                devices[index] = device
                shouldAddDevice = false
            }
        }
        if (shouldAddDevice) {
            devices.add(device)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding= ActivityRecyclerviewAdapterBinding.inflate(inflater, parent, false)
        return ScanViewHolder(binding)
    }

    override fun getItemCount(): Int = devices.size

 @SuppressLint("MissingPermission")
    override fun onBindViewHolder(holder: ScanViewHolder, position: Int) {
        holder.deviceAddress.text = devices[position].address
        holder.RSSI.text = rssiValues[position].toString()
        holder.deviceName.text=devices[position].address
        holder.itemView.setOnClickListener{
            onDeviceClickListener(devices[position])
        }

    }



    class ScanViewHolder(binding: ActivityRecyclerviewAdapterBinding) : RecyclerView.ViewHolder(binding.root) {
        val deviceName = binding.DeviceName
        val deviceAddress : TextView = binding.MacAdress
        val RSSI : TextView = binding.RSSI
    }

    fun addDevice(device: BluetoothDevice, rssi: Int) {
        val deviceIndex = devices.indexOfFirst { it.address == device.address }
        if (deviceIndex != -1) {
            devices[deviceIndex] = device
            rssiValues[deviceIndex] = rssi // <-- Ajouter la valeur RSSI correspondante à la liste rssiValues
        } else {
            devices.add(device)
            rssiValues.add(rssi) // <-- Ajouter la valeur RSSI correspondante à la liste rssiValues
        }
    }

}

