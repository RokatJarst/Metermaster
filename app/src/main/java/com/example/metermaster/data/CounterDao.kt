package com.example.metermaster.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CounterDao {
    @Query("SELECT * FROM counters")
    fun all(): Flow<List<Counter>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(c: Counter)

    @Delete
    suspend fun delete(c: Counter)
}
