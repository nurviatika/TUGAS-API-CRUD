package com.example.dicodingevents.repository

import androidx.lifecycle.LiveData
import com.example.dicodingevents.database.FavoriteEvent
import com.example.dicodingevents.database.FavoriteEventDao
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavEventRepository(private val mFavoriteEventDao: FavoriteEventDao) {

    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    fun getAllFavoriteEvents(): LiveData<List<FavoriteEvent>> {
        return mFavoriteEventDao.getAllFavoriteEvents()
    }

    fun insert(event: FavoriteEvent) {
        executorService.execute { mFavoriteEventDao.insert(event) }
    }

    fun delete(event: FavoriteEvent) {
        executorService.execute { mFavoriteEventDao.delete(event) }
    }

    fun update(event: FavoriteEvent) {
        executorService.execute { mFavoriteEventDao.update(event) }

    }



}
