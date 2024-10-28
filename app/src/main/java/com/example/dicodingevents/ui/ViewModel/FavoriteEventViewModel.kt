import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.dicodingevents.database.FavRoomDatabase
import com.example.dicodingevents.database.FavoriteEvent
import com.example.dicodingevents.repository.FavEventRepository
import kotlinx.coroutines.launch

class FavoriteEventViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FavEventRepository

    init {
        val favoriteEventDao =
            FavRoomDatabase.getDatabase(application).favoriteEventDao()
        repository = FavEventRepository(favoriteEventDao)
    }

    fun getAllFavoriteEvents(): LiveData<List<FavoriteEvent>> {
        return repository.getAllFavoriteEvents()
    }
    fun insertFavorite(favoriteEvent: FavoriteEvent) = viewModelScope.launch {
        Log.d("FavoriteEventViewModel", "Inserting event: $favoriteEvent")
        repository.insert(favoriteEvent)
    }

    fun deleteFavorite(favoriteEvent: FavoriteEvent) = viewModelScope.launch {
        Log.d("FavoriteEventViewModel", "Deleting event: $favoriteEvent")
        repository.delete(favoriteEvent)
    }

    fun addFavoriteEvent(event: FavoriteEvent) {
        viewModelScope.launch {
            repository.insert(event)
        }

    }


}
