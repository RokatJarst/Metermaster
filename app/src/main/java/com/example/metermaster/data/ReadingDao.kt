package com.example.metermaster.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ReadingDao {
    @Query("SELECT * FROM readings ORDER BY timestamp ASC")
    fun all(): Flow<List<Reading>>

    @Query("SELECT * FROM readings WHERE counterId = :counterId ORDER BY timestamp ASC")
    fun readingsFor(counterId: String): Flow<List<Reading>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(r: Reading)
}
