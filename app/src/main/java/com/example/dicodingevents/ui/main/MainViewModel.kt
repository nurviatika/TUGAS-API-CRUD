import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.dicodingevents.database.FavRoomDatabase
import com.example.dicodingevents.database.FavoriteEvent
import com.example.dicodingevents.repository.FavEventRepository

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val favoriteEventDao =
        FavRoomDatabase.getDatabase(application).favoriteEventDao()
    private val mNoteRepository:
            FavEventRepository = FavEventRepository(favoriteEventDao)

    fun getAllFavoriteEvents():
            LiveData<List<FavoriteEvent>> = mNoteRepository.getAllFavoriteEvents()
}