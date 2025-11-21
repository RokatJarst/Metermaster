package com.example.metermaster.ui

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import com.example.metermaster.ui.viewmodel.MeterViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.metermaster.util.CsvExporter
import kotlinx.coroutines.runBlocking

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val vm: MeterViewModel = viewModel()
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Ustawienia", style = MaterialTheme.typography.h6)
        Spacer(Modifier.height(12.dp))
        Button(onClick = {
            val counters = runBlocking { vm.counters.value }
            val readings = runBlocking { vm.readings.value }
            val file = CsvExporter.exportReadings(context, counters, readings)
            val uri = FileProvider.getUriForFile(context, context.packageName + ".fileprovider", file)
            val share = Intent(Intent.ACTION_SEND).apply {
                type = "text/csv"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(share, "Udostępnij CSV"))
        }) { Text("Eksportuj odczyty (CSV) i udostępnij") }
    }
}
