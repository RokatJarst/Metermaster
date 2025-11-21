package com.example.metermaster.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "counters")
data class Counter(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val type: String,
    val unit: String = "m3",
    val lastValue: Double = 0.0
)
