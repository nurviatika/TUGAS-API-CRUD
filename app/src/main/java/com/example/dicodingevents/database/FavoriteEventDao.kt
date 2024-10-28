package com.example.dicodingevents.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteEventDao {
    @Query("SELECT * FROM FavoriteEvent")
    fun getAllFavoriteEvents(): LiveData<List<FavoriteEvent>>

    @Query("SELECT * FROM FavoriteEvent WHERE id = :id")
    fun getFavoriteEventById(id: String): LiveData<FavoriteEvent>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(note: FavoriteEvent)

    @Update
    fun update(note: FavoriteEvent)

    @Delete
    fun delete(note: FavoriteEvent)
}
