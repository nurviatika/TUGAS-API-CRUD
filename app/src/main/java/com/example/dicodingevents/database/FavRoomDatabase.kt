package com.example.dicodingevents.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase



@Database(entities = [FavoriteEvent::class], version = 1, exportSchema = false)
abstract class FavRoomDatabase : RoomDatabase() {
    abstract fun favoriteEventDao(): FavoriteEventDao

    companion object {
        @Volatile
        private var INSTANCE: FavRoomDatabase? = null


        fun getDatabase(context: Context): FavRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavRoomDatabase::class.java,
                    "Fav_room_database"
                ).build()
                INSTANCE = instance
                instance

            }
        }

    }

}