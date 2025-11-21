package com.example.metermaster.ui

import android.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.metermaster.ui.viewmodel.MeterViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.LineData
import java.util.*
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

@Composable
fun StatsScreen() {
    val vm: MeterViewModel = viewModel()
    val readingsState = vm.readings.collectAsState()
    val sdf = SimpleDateFormat("yyyy-MM", Locale.getDefault())
    val monthly = mutableMapOf<String, Double>()
    for (r in readingsState.value) {
        val k = sdf.format(Date(r.timestamp))
        monthly[k] = (monthly[k] ?: 0.0) + r.value
    }
    val keys = monthly.keys.sorted()
    val entries = ArrayList<Entry>()
    for ((i,k) in keys.withIndex()) entries.add(Entry(i.toFloat(), (monthly[k]?:0.0).toFloat()))

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Statystyki", style = MaterialTheme.typography.h6)
        Spacer(Modifier.height(12.dp))
        AndroidView(factory = { ctx ->
            LineChart(ctx).apply {
                val set = LineDataSet(entries, "Zużycie")
                set.lineWidth = 2f
                set.setDrawValues(false)
                set.setDrawCircles(true)
                val data = LineData(set)
                this.data = data
                this.description.isEnabled = false
                this.setBackgroundColor(Color.WHITE)
                this.invalidate()
            }
        }, modifier = Modifier.fillMaxWidth().height(300.dp))
        Spacer(Modifier.height(8.dp))
        Text("Miesiące: ${keys.joinToString(", ")}")
    }
}
