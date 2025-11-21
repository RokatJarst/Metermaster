package com.example.metermaster.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "readings")
data class Reading(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val counterId: String,
    val timestamp: Long,
    val value: Double
)
