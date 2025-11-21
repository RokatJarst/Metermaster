package com.example.metermaster.ui.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.metermaster.data.AppDatabase
import com.example.metermaster.data.Counter
import com.example.metermaster.data.Reading
import com.example.metermaster.ocr.OcrService
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MeterViewModel(application: Application): AndroidViewModel(application) {
    private val db = AppDatabase.get(application)
    private val counterDao = db.counterDao()
    private val readingDao = db.readingDao()

    val counters: StateFlow<List<Counter>> = counterDao.all().map { it }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val readings: StateFlow<List<Reading>> = readingDao.all().map { it }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun addCounter(name: String, type: String, unit: String = "m3") {
        viewModelScope.launch { counterDao.insert(Counter(name = name, type = type, unit = unit)) }
    }

    fun addReadingFor(counterId: String, value: Double) {
        viewModelScope.launch { readingDao.insert(Reading(counterId = counterId, timestamp = System.currentTimeMillis(), value = value)) }
    }

    suspend fun analyzeBitmap(bitmap: Bitmap): Pair<Double?, String?> {
        val res = OcrService.analyzeBitmap(bitmap)
        return Pair(res.numericReading, res.detectedLabel)
    }
}
