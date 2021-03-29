package com.example.galpndeavescontroldeluz.ui.dispositivo

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.galpndeavescontroldeluz.R
import com.example.galpndeavescontroldeluz.data.Horario
import com.example.galpndeavescontroldeluz.ui.home.HomeActivity
import com.example.hp.bluetoothjhr.BluetoothJhr
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.android.synthetic.main.activity_dispositivo.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread


class JsonHorarios(var horarios: List<Horario>) {
    fun onCreate(horarios: List<Horario>) {
        this.horarios = horarios
    }
}

class DispositivoActivity : AppCompatActivity() {

    lateinit var bluetoothJhr: BluetoothJhr
    var boolean: Boolean = true
    lateinit var horarios: JsonHorarios
    var numeroDeGalponAnterior = 0
    var numeroDeGalpon = 0
    var listaHorarios = listOf<List<Horario>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dispositivo)

        bluetoothJhr = BluetoothJhr(HomeActivity::class.java, this)
        boolean = true

        val list : MutableList<String> = ArrayList()
        for (i in 1..4)
            list.add("Galpon $i")
        val adapterListGalpones = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, list)
        lista_galpones.adapter = adapterListGalpones
        lista_galpones.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
//                showAlertDialog(list[position])
                cambiarGalpon(position)
            }
        }

        update_time.setOnClickListener {
            val fechaString = SimpleDateFormat("MMM dd YYYY;HH:mm:ss")
                .format(Date())
                .replace(" 0","  ")
                .replace(". "," ")
                .capitalize()
            fechaString.replace(" 0","  ")
            Log.e("STRING:", fechaString)
            bluetoothJhr.Tx("time,$fechaString,")
        }

        thread(start = true){
            while (boolean){
                Thread.sleep(500);
                this@DispositivoActivity.runOnUiThread(java.lang.Runnable {
                    var json = bluetoothJhr.Rx()
                    this.error.text = "Mensaje Recibido:\n" + json
                    Log.e("Galpon RECIVIDO:" , json)
                    if ("\n" in json) {
                        val mapper = jacksonObjectMapper()
                        val horarios = mapper.readValue<List<List<Horario>>>(json)
                        this.listaHorarios = horarios
                        this.lista_horarios.adapter = HorarioAdapter(
                            this,
                            this.numeroDeGalpon,
                            this.listaHorarios[numeroDeGalpon],
                            this.bluetoothJhr
                        )
                        bluetoothJhr.ResetearRx()
                    } else if (this.numeroDeGalpon != this.numeroDeGalponAnterior){
                        this.lista_horarios.adapter = HorarioAdapter(
                            this,
                            this.numeroDeGalpon,
                            this.listaHorarios[numeroDeGalpon],
                            this.bluetoothJhr
                        )
                        this.numeroDeGalponAnterior = this.numeroDeGalpon
                    }
                })
            }
        }
    }

    private fun cambiarGalpon(position: Int) {
        this.numeroDeGalpon = position
//        this.lista_horarios.adapter = HorarioAdapter(
//            this,
//            position,
//            this.listaHorarios[position],
//            this.bluetoothJhr
//        )
    }

    override fun onResume() {
        super.onResume()
        bluetoothJhr.ConectaBluetooth()
        bluetoothJhr.ResetearRx()
    }

    override fun onPause() {
        super.onPause()
        bluetoothJhr.CierraConexion()
        boolean = false
    }

    private fun showAlertDialog(text: String) {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this@DispositivoActivity)
        alertDialog.setTitle("AlertDialog")
        alertDialog.setMessage(text)
        alertDialog.setPositiveButton(
            "Ok"
        ) { _, _ ->
            Toast.makeText(this@DispositivoActivity, "Alert dialog closed.", Toast.LENGTH_LONG)
                .show()
        }
        alertDialog.setNegativeButton(
            "Cancel"
        ) { _, _ -> }
        val alert: AlertDialog = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
    }
}