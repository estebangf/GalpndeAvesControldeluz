package com.example.galpndeavescontroldeluz.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.galpndeavescontroldeluz.R
import com.example.galpndeavescontroldeluz.ui.dispositivo.DispositivoActivity
import com.example.hp.bluetoothjhr.BluetoothJhr
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val bluetoothJhr = BluetoothJhr(this, lista_dispositivos)
        bluetoothJhr.EncenderBluetooth()

        lista_dispositivos.setOnItemClickListener { adapterView, view, i, l ->
            bluetoothJhr.Disp_Seleccionado(view,i, DispositivoActivity::class.java)
        }
    }
}