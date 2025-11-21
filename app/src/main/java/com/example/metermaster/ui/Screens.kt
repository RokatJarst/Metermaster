package com.example.metermaster.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.metermaster.ui.viewmodel.MeterViewModel
import com.example.metermaster.data.Counter
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

@Composable
fun ConsumptionScreen(vm: MeterViewModel, onOpen: (Counter)->Unit) {
    val counters by vm.counters.collectAsState()
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Lista liczników", style = MaterialTheme.typography.h6)
        Spacer(Modifier.height(12.dp))
        for (c in counters) {
            Card(Modifier.fillMaxWidth().padding(4.dp)) {
                Row(Modifier.padding(12.dp)) {
                    Column(Modifier.weight(1f)) {
                        Text(c.name)
                        Text("Odczyt: ${'$'}{c.lastValue}", style = MaterialTheme.typography.body2)
                    }
                    Button(onClick = { onOpen(c) }) { Text("Szczegóły") }
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        var showAdd by remember { mutableStateOf(false) }
        if (showAdd) AddMeterDialog(onAdd = { name, type -> vm.addCounter(name, type); showAdd=false }, onDismiss = { showAdd=false })
        Button(onClick = { showAdd = true }, modifier = Modifier.align(Alignment.End)) { Text("Dodaj licznik") }
    }
}

@Composable
fun AddMeterDialog(onAdd:(String,String)->Unit, onDismiss:()->Unit) {
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("woda") }
    AlertDialog(onDismissRequest = onDismiss, title = { Text("Dodaj licznik") }, text = {
        Column {
            OutlinedTextField(value = name, onValueChange={name=it}, label = { Text("Nazwa") })
            OutlinedTextField(value = type, onValueChange={type=it}, label = { Text("Typ (woda/gaz/prad)") })
        }
    }, confirmButton = { Button(onClick={ if (name.isNotBlank()) onAdd(name,type) }) { Text("Dodaj") } }, dismissButton = { Button(onClick=onDismiss){ Text("Anuluj") } })
}

@Composable
fun SettingsScreen() {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Ustawienia", style = MaterialTheme.typography.h6)
        Spacer(Modifier.height(12.dp))
        Text("Tutaj ustawisz ceny za media, dodasz liczniki i podliczniki.")
    }
}
