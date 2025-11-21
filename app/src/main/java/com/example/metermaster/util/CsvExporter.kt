package com.example.metermaster.util

import android.content.Context
import com.example.metermaster.data.Counter
import com.example.metermaster.data.Reading
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

object CsvExporter {
    fun exportReadings(context: Context, counters: List<Counter>, readings: List<Reading>): File {
        val f = File(context.cacheDir, "meter_readings_${System.currentTimeMillis()}.csv")
        val fw = FileWriter(f)
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        fw.append("counter_id,counter_name,timestamp,value\n")
        val map = counters.associateBy { it.id }
        for (r in readings) {
            val name = map[r.counterId]?.name ?: "unknown"
            fw.append("${r.counterId},${name},${df.format(Date(r.timestamp))},${r.value}\n")
        }
        fw.flush(); fw.close()
        return f
    }
}
