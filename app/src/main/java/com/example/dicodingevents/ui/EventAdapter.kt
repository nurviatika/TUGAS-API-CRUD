package com.example.dicodingevents.ui

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingevents.R
import com.example.dicodingevents.data.response.ListEventsItem

class EventAdapter (
    private val context: Context,
    private val favoriteEvents: List<String> = emptyList()) :
    ListAdapter<ListEventsItem, EventAdapter.EventViewHolder>(EventDiffCallback()) {

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageLogo: ImageView =
            itemView.findViewById(R.id.imageLogo)
        val textName: TextView =
            itemView.findViewById(R.id.textName)
        val textSummary: TextView =
            itemView.findViewById(R.id.textSummary)
        val textCity: TextView =
            itemView.findViewById(R.id.textCity)
        val imageFavorite: ImageView =
            itemView.findViewById(R.id.imageFavorite)

        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val event = getItem(position)
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra("EVENT_ID", event.id)
                    context.startActivity(intent)
                }
            }
        }


        fun bind(event: ListEventsItem) {
            textName.text = event.name
            textSummary.text = event.summary
            textCity.text = event.cityName

            val imageUrl = event.imageLogo
            Log.d("EventAdapter", "Loading image from URL: $imageUrl")

            if (!imageUrl.isNullOrEmpty()) {
                Glide.with(itemView.context)

                    .load(event.imageLogo)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(imageLogo)
            } else {
                imageLogo.setImageResource(R.drawable.placeholder_image)
            }


            imageFavorite.visibility =
                if (favoriteEvents.contains(event.id.toString())) {
                    View.VISIBLE
                } else {
                    View.GONE
                }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }

    class EventDiffCallback : DiffUtil.ItemCallback<ListEventsItem>() {
        override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
            return oldItem == newItem
        }
    }
}