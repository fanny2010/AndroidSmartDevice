package fr.isen.auphan.androidsmartdevice

import android.Manifest
import android.bluetooth.*
import android.bluetooth.BluetoothDevice.EXTRA_DEVICE
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import fr.isen.auphan.androidsmartdevice.databinding.ActivityDeviceBinding
import android.annotation.SuppressLint
import androidx.core.view.isVisible
import java.io.IOException
import java.util.*

@SuppressLint("MissingPermission")
class DeviceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeviceBinding
    private var bluetoothGatt: BluetoothGatt? = null
    private val serviceUUID = UUID.fromString("0000feed-cc7a-482a-984a-7f2ed5b3e58f")
    private val characteristicLedUUID = UUID.fromString("0000abcd-8e22-4541-9d4c-21edae82ed19")
    private val characteristicButtonUUID = UUID.fromString("00001234-8e22-4541-9d4c-21edae82ed19")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bluetoothDevice: BluetoothDevice? = intent.getParcelableExtra("device")
        val bluetoothGatt = bluetoothDevice?.connectGatt(this, false, bluetoothGattCallback)
        // bluetoothGatt?.connect()

        LedClick()
    }



    override fun onStop() {
        super.onStop()
        bluetoothGatt?.close()
    }

    @SuppressLint("MissingPermission")
    private fun LedClick(){
        binding.Led1.setOnClickListener{

            val characteristic = bluetoothGatt?.getService(serviceUUID)?.getCharacteristic(characteristicLedUUID)

            if(binding.Led1.imageTintList == getColorStateList(R.color.teal_200)){
                binding.Led1.imageTintList = getColorStateList(R.color.black)
                characteristic?.value = byteArrayOf(0x00)
                bluetoothGatt?.writeCharacteristic(characteristic)
                Toast.makeText(this, "La Led 1 est activée", Toast.LENGTH_LONG).show()
            } else{
                binding.Led1.imageTintList = getColorStateList(R.color.teal_200)
                binding.Led2.imageTintList = getColorStateList(R.color.black)
                binding.Led3.imageTintList = getColorStateList(R.color.black)
                characteristic?.value = byteArrayOf(0x01)
                bluetoothGatt?.writeCharacteristic(characteristic)

            }
        }
        binding.Led2.setOnClickListener{
            val characteristic = bluetoothGatt?.getService(serviceUUID)?.getCharacteristic(characteristicLedUUID)

            if(binding.Led2.imageTintList == getColorStateList(R.color.teal_200)){
                binding.Led2.imageTintList = getColorStateList(R.color.black)
                characteristic?.value = byteArrayOf(0x00)
                bluetoothGatt?.writeCharacteristic(characteristic)
                Toast.makeText(this, "La Led 2 est activée", Toast.LENGTH_LONG).show()
            } else{
                binding.Led2.imageTintList = getColorStateList(R.color.teal_200)
                binding.Led1.imageTintList = getColorStateList(R.color.black)
                binding.Led3.imageTintList = getColorStateList(R.color.black)
                characteristic?.value = byteArrayOf(0x02)
                bluetoothGatt?.writeCharacteristic(characteristic)
            }
        }
        binding.Led3.setOnClickListener{
            val characteristic = bluetoothGatt?.getService(serviceUUID)?.getCharacteristic(characteristicLedUUID)

            if(binding.Led3.imageTintList == getColorStateList(R.color.teal_200)){
                binding.Led3.imageTintList = getColorStateList(R.color.black)
                characteristic?.value = byteArrayOf(0x00)
                bluetoothGatt?.writeCharacteristic(characteristic)
                Toast.makeText(this, "La Led 3 est activée", Toast.LENGTH_LONG).show()
            } else{
                binding.Led3.imageTintList = getColorStateList(R.color.teal_200)
                binding.Led1.imageTintList = getColorStateList(R.color.black)
                binding.Led2.imageTintList = getColorStateList(R.color.black)
                characteristic?.value = byteArrayOf(0x03)
                bluetoothGatt?.writeCharacteristic(characteristic)
            }
        }
    }
    private val bluetoothGattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                runOnUiThread {
                    displayContentConnected()
                }
                bluetoothGatt?.discoverServices()
            }
        }
        @SuppressLint("MissingPermission")
        override fun onServicesDiscovered(bluetoothGatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(bluetoothGatt, status)
            if (status == BluetoothGatt.GATT_SUCCESS) {
// Enable notifications for the desired characteristic
                val characteristicButton3 = bluetoothGatt?.getService(serviceUUID)?.getCharacteristic(characteristicButtonUUID)
                bluetoothGatt?.setCharacteristicNotification(characteristicButton3, true)
                characteristicButton3?.descriptors?.forEach { descriptor ->
                    descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                    bluetoothGatt.writeDescriptor(descriptor)
                }
            }
        }
        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
            super.onCharacteristicChanged(gatt, characteristic)
            if (characteristic.uuid == characteristicButtonUUID) {
                val value = characteristic.value
                val clicks = value[0].toInt()
                runOnUiThread {
                    binding.nbr.text = "Nombre de click: ${clicks.toString()}"
                }
            }
        }
    }

    private fun displayContentConnected() {
        binding.connexionText.text = getString(R.string.device_led_text)
        binding.progressBar.isVisible = false
        binding.Led1.isVisible = true
        binding.TPBLE.text = "TPBLE"
        binding.LedAffichage.text = "Affichage des LEDs"
        binding.textView3.isVisible = true
        binding.CheckBox.isVisible = true
        binding.Led2.isVisible = true
        binding.Led3.isVisible = true
        binding.nbr.isVisible = true
    }

}
