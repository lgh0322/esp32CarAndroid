package com.vaca.car.ble

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import android.util.Log
import no.nordicsemi.android.ble.BleManager
import no.nordicsemi.android.ble.ReadRequest
import no.nordicsemi.android.ble.Request
import no.nordicsemi.android.ble.data.Data
import java.util.*

class BleDataManager(context: Context) : BleManager(context) {
    private var listener: OnNotifyListener? = null
    fun setNotifyListener(listener: OnNotifyListener?) {
        this.listener = listener
    }

    override fun getGattCallback(): BleManagerGattCallback {
        return MyManagerGattCallback()
    }

    fun sendCmd(bytes: ByteArray?) {
        writeCharacteristic(write_char, bytes)
            .split()
            .enqueue()
    }

    public override fun refreshDeviceCache(): Request {
        return super.refreshDeviceCache()
    }

    override fun shouldClearCacheWhenDisconnected(): Boolean {
        return true
    }

    public override fun readCharacteristic(characteristic: BluetoothGattCharacteristic?): ReadRequest {
        return super.readCharacteristic(characteristic)
    }

    override fun log(priority: Int, message: String) {}
    interface OnNotifyListener {
        fun onNotify(device: BluetoothDevice?, data: Data?)
    }

    /**
     * BluetoothGatt callbacks object.
     */
    private inner class MyManagerGattCallback : BleManagerGattCallback() {
        public override fun isRequiredServiceSupported(gatt: BluetoothGatt): Boolean {
            gatt.getService(service_uuid)?.run {
                write_char = getCharacteristic(write_uuid)
                notify_char = getCharacteristic(notify_uuid)
            }
            return true
        }

        // If you have any optional services, allocate them here. Return true only if
        // they are found.
        override fun isOptionalServiceSupported(gatt: BluetoothGatt): Boolean {
            return super.isOptionalServiceSupported(gatt)
        }

        // Initialize your device here. Often you need to enable notifications and set required
        // MTU or write some initial data. Do it here.
        override fun initialize() {
            // You may enqueue multiple operations. A queue ensures that all operations are
            // performed one after another, but it is not required.
            beginAtomicRequestQueue()
                .add(requestMtu(23) // Remember, GATT needs 3 bytes extra. This will allow packet size of 244 bytes.
                    .with { _: BluetoothDevice?, mtu: Int ->
                        log(
                            Log.INFO,
                            "MTU set to $mtu"
                        )
                    }
                    .fail { _: BluetoothDevice?, status: Int ->
                        log(
                            Log.WARN,
                            "Requested MTU not supported: $status"
                        )
                    })
                .add(enableNotifications(notify_char))
                .done { log(Log.INFO, "Target initialized") }
                .enqueue()
            // You may easily enqueue more operations here like such:
            setNotificationCallback(notify_char)
                .with { device: BluetoothDevice?, data: Data? -> listener!!.onNotify(device, data) }
        }

        override fun onDeviceDisconnected() {

        }

        override fun onServicesInvalidated() {

        }
    }

    companion object {
        val service_uuid: UUID = UUID.fromString("59462f12-9543-9999-12c8-58b459a2712d")
        val write_uuid: UUID = UUID.fromString("5c3a659e-897e-45e1-b016-007107c96df7")
        val notify_uuid: UUID = UUID.fromString("4c3a659e-897e-45e1-b016-007107c96df6")


        var write_char: BluetoothGattCharacteristic? = null
        var notify_char: BluetoothGattCharacteristic? = null
        var model_char: BluetoothGattCharacteristic? = null
        var firmware_char: BluetoothGattCharacteristic? = null
    }
}