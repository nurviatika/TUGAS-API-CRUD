package com.example.dicodingevents.ui

import FavoriteEventViewModel
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.dicodingevents.data.response.DetailEventResponse
import com.example.dicodingevents.data.retrofit.ApiConfig
import com.example.dicodingevents.databinding.ActivityDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.dicodingevents.R
import com.example.dicodingevents.database.FavoriteEvent
import com.example.dicodingevents.ui.ViewModel.FavoriteEventViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var favoriteEventViewModel: FavoriteEventViewModel
    private var isFavorite = false
    private var eventId: Int = -1

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        favoriteEventViewModel =
            ViewModelProvider(this, FavoriteEventViewModelFactory(application))
                .get(FavoriteEventViewModel::class.java)

        val fab: FloatingActionButton = findViewById(R.id.fab)


        eventId = intent.getIntExtra("EVENT_ID", -1)
        updateFavoriteStatus()

        fab.setOnClickListener {
            if (isFavorite) {

                deleteFavoriteEvent()
            } else {

                addFavoriteEvent()
            }
            isFavorite = !isFavorite
            updateFavoriteIcon()
        }

        binding.progressBar.visibility = View.VISIBLE

        if (eventId != -1) {
            getDetailEvent(eventId.toString())
        } else {
            Toast.makeText(this, "Event ID not found", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun updateFavoriteIcon() {
        val fab: FloatingActionButton = findViewById(R.id.fab)
        if (isFavorite) {
            fab.setImageResource(R.drawable.baseline_favorite_24)
        } else {
            fab.setImageResource(R.drawable.baseline_favorite_border_24)
        }
    }

    private fun addFavoriteEvent() {
        val eventName = binding.tvTitle.text.toString()
        val mediaCover = ""

        val favoriteEvent = FavoriteEvent(
            id = eventId.toString(),
            name = eventName,
            mediaCover = mediaCover
        )


        favoriteEventViewModel.addFavoriteEvent(favoriteEvent)

        Toast.makeText(this, "Event added to favorites", Toast.LENGTH_SHORT).show()
    }

    private fun deleteFavoriteEvent() {
        val favoriteEvent = FavoriteEvent(
            id = eventId.toString(),
            name = binding.tvTitle.text.toString(),
            mediaCover = ""
        )


        favoriteEventViewModel.deleteFavorite(favoriteEvent)

        Toast.makeText(this, "Event removed from favorites", Toast.LENGTH_SHORT).show()
    }

    private fun updateFavoriteStatus() {

        favoriteEventViewModel.getAllFavoriteEvents().observe(this) { favoriteEvents ->
            isFavorite = favoriteEvents.any { it.id == eventId.toString() }
            updateFavoriteIcon()
        }
    }

    private fun getDetailEvent(id: String) {
        binding.progressBar.visibility = View.VISIBLE
        val client = ApiConfig.getApiService().getDetailEvent(id)
        client.enqueue(object : Callback<DetailEventResponse> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<DetailEventResponse>,
                response: Response<DetailEventResponse>
            ) {
                binding.progressBar.visibility = View.GONE

                if (response.isSuccessful) {
                    val detail = response.body()?.event
                    if (detail != null) {
                        binding.apply {
                            tvTitle.text =
                                detail.name
                            tvDescription.text =
                                HtmlCompat.fromHtml(detail.description ?: "", HtmlCompat.FROM_HTML_MODE_LEGACY)
                            tvLocation.text =
                                detail.cityName
                            tvOwner.text =
                                detail.ownerName
                            tvCategory.text =
                                detail.category

                            val beginTime =
                                getString(R.string.begin_time, detail.beginTime)
                            tvBeginTime.text = beginTime

                            Glide.with(this@DetailActivity)
                                .load(detail.mediaCover)
                                .into(ivMediaCover)

                            Glide.with(this@DetailActivity)
                                .load(detail.imageLogo)
                                .into(ivImageLogo)

                            val remainingQuota =
                                (detail.quota ?: 0) - (detail.registrants ?: 0)
                            tvQuota.text =
                                getString(R.string.remaining_quota, remainingQuota)

                            btnRegisterLink.setOnClickListener {
                                val registrationLink = detail.link
                                if (!registrationLink.isNullOrEmpty()) {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(registrationLink))
                                    startActivity(intent)
                                } else {
                                    Toast.makeText(this@DetailActivity, "Registration link is not available", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    } else {
                        Toast.makeText(this@DetailActivity, "Event details not found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@DetailActivity, "Failed to load event details. Response code: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DetailEventResponse>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this@DetailActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}