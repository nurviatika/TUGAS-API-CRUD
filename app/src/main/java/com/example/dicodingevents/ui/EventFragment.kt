package com.example.dicodingevents.ui

import FavoriteEventViewModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dicodingevents.R
import com.example.dicodingevents.data.response.EventResponse
import com.example.dicodingevents.data.response.ListEventsItem
import com.example.dicodingevents.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventFragment : Fragment() {

    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: FavoriteEventViewModel
    private lateinit var adapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_event, container, false)
        progressBar = view.findViewById(R.id.progressBar)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel = ViewModelProvider(this)
            .get(FavoriteEventViewModel::class.java)

        adapter = EventAdapter(requireContext())
        recyclerView.adapter = adapter

        viewModel.getAllFavoriteEvents()
            .observe(viewLifecycleOwner) { user ->
                val items = user.map {
                    ListEventsItem(id = it.id.toInt(), name = it.name, imageLogo = it.mediaCover)
                }
                adapter.submitList(items)
            }

        showLoading(true)
        val activeValue = arguments?.getInt("key_active") ?: 0
        getEvents(activeValue)

        return view
    }

    private fun getEvents(active: Int) {
        showLoading(true)

        val client = ApiConfig.getApiService()
            .getActiveEvents(active)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                showLoading(false)

                if (response.isSuccessful) {
                    val events = response.body()?.listEvents ?: emptyList()
                    val apiItems = events.map {
                        ListEventsItem(id = it.id, name = it.name, imageLogo = it.mediaCover)
                    }
                    adapter.submitList(apiItems)
                } else {
                    Log.e("API_Error", "Response Code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                showLoading(false)
                Log.e("API_Error", "onFailure: ${t.message}")
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility =
            if (isLoading) View.VISIBLE else View.GONE
        recyclerView.visibility =
            if (isLoading) View.GONE else View.VISIBLE
    }
}
