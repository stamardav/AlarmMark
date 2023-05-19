package com.example.alarmmark

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var tvAlarmPrompt: TextView
    private var ALARM_REQUEST_CODE: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        tvAlarmPrompt = findViewById(R.id.tv_alarm_prompt)
    }

    fun clickSetAlarm(view: View) {
        tvAlarmPrompt.text = ""
        openTimePickerDialog()
    }


    // готовый пикер
    private fun openTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _,hour,minute ->
            val calNow = Calendar.getInstance()

            val calSet = Calendar.getInstance()
            calSet.apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
                // проверка вводимого значения времени
            when {
                calSet <= calNow -> {
                    calSet.add(Calendar.DATE, 1)
                    Log.i("Результат", " =<0")
                }
                calSet > calNow -> {
                    Log.i("Результат", " > 0")
                }
                else -> {
                    Log.i("Результат", " else ")
                }
            }
            setAlarm(calSet)
        }
        val timePickerDialog = TimePickerDialog(this,timeSetListener,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),true)
        timePickerDialog.setTitle("Установить время для будильника")
        timePickerDialog.show()
    }
// при успешном создании будильника - появляется "toast"
    @SuppressLint("SetTextI18n")
    private fun setAlarm(targetCal: Calendar) {
        tvAlarmPrompt.text = """
                ***
                Будильник установлен на ${targetCal.time}
                ***
                """.trimIndent()
        val intent = Intent(this@MainActivity, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this@MainActivity, ALARM_REQUEST_CODE, intent, 0)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            targetCal.timeInMillis,
            pendingIntent
        )
    }
}

