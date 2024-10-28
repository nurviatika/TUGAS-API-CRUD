package com.example.dicodingevents.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class FavoriteEvent(
    @PrimaryKey(autoGenerate = false)
    var id: String = " 8973",

    var name: String = "DevCoach 175: React | Kenalan dengan React: UI Web Masa Kini!",

    var summary: String = "Acara ini sepenuhnya GRATIS dan akan diselenggarakan hari Jumat, 1 November 2024 pukul 16.00 - 17.00 WIB Live di YouTube",

    var cityName: String ="Online",

    var mediaCover: String? = "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/event/dos-devcoach_175_react_kenalan_dengan_react_ui_web_masa_kini_mc_261024104238.jpg"
) : Parcelable
