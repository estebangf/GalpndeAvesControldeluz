package com.example.galpndeavescontroldeluz.ui.dispositivo

import android.app.TimePickerDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.galpndeavescontroldeluz.R
import com.example.galpndeavescontroldeluz.data.Horario
import com.example.hp.bluetoothjhr.BluetoothJhr
import kotlinx.android.synthetic.main.item_times.view.*
import java.text.SimpleDateFormat
import java.util.*

class HorarioAdapter(
    private val mContext: Context,
    private val numeroDeGalpon: Int,
    private val listaHorarios: List<Horario>,
    private var bluetoothJhr: BluetoothJhr):
    ArrayAdapter<Horario> (mContext, 0, numeroDeGalpon, listaHorarios) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(mContext).inflate(R.layout.item_times, parent, false)

        val horario = listaHorarios[position]

        val on = ("" + horario.on[0]+horario.on[1]+ horario.on[3]+horario.on[4]).toInt()
        val off = ("" + horario.off[0]+horario.off[1] + horario.off[3]+horario.off[4]).toInt()
        val c = (SimpleDateFormat("HHmm").format(Date())).toInt()
//        val currentTime : String = SimpleDateFormat("HH:mm:ss").format(Date())

        val isOn : Boolean = c in on..off
        if (isOn) {
            layoutInflater.light_state.setBackgroundResource(R.drawable.ligth_is_on)
        } else {
            layoutInflater.light_state.setBackgroundResource(R.drawable.ligth_is_off)
        }

        layoutInflater.horario_on.text = horario.on
        layoutInflater.horario_on.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListenerOn = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                var horarioOn = SimpleDateFormat("HH:mm").format(cal.time).toString()
                Log.e("Galpon ENVIANDO", "on,$numeroDeGalpon,$position,$horarioOn,")
                bluetoothJhr.Tx("on,$numeroDeGalpon,$position,$horarioOn,")
            }
            TimePickerDialog(
                mContext,
                timeSetListenerOn,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }

        layoutInflater.horario_off.text = horario.off
        layoutInflater.horario_off.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListenerOn = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                var horarioOff = SimpleDateFormat("HH:mm").format(cal.time).toString()
                Log.e("Galpon ENVIANDO", "off,$numeroDeGalpon,$position,$horarioOff,")
                bluetoothJhr.Tx("off,$numeroDeGalpon,$position,$horarioOff,")
            }
            TimePickerDialog(
                mContext,
                timeSetListenerOn,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }

        return layoutInflater
    }
}