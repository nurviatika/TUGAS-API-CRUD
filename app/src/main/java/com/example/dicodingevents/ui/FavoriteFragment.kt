package com.example.dicodingevents.ui

import FavoriteEventViewModel
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dicodingevents.R
import com.example.dicodingevents.data.response.ListEventsItem
import com.example.dicodingevents.database.FavoriteEvent
import com.example.dicodingevents.ui.ViewModel.FavoriteEventViewModelFactory


class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private lateinit var viewModel: FavoriteEventViewModel
    private lateinit var adapter: EventAdapter
    private lateinit var emptyTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val factory = FavoriteEventViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, factory).get(FavoriteEventViewModel::class.java)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emptyTextView = view.findViewById(R.id.empty_text_view)
        setupRecyclerView(view)
        observeViewModel()
    }


    private fun setupRecyclerView(view: View) {
        adapter = EventAdapter(requireContext())
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeViewModel() {
        viewModel.getAllFavoriteEvents().observe(viewLifecycleOwner) {favoriteEvents ->
            val items = mapToListEventsItems(favoriteEvents)

            adapter.submitList(items)

            if (items.isEmpty()) {
                emptyTextView.visibility = View.VISIBLE

            } else {
                emptyTextView.visibility = View.GONE

            }

            Log.d("FavoriteFragment", "Data favorit: $items")
        }
    }

    private fun mapToListEventsItems(events: List<FavoriteEvent>?): List<ListEventsItem> {
        return events?.map { event ->
            ListEventsItem(
                id = event.id.toIntOrNull() ?: 0,
                name = event.name,
                summary = event.summary,
                cityName = event.cityName,
                imageLogo = event.mediaCover ?: ""
            )
        } ?: emptyList()
    }
}



