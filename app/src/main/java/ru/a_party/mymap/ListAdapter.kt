package ru.a_party.mymap

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.koin.java.KoinJavaComponent

class ListAdapter: RecyclerView.Adapter<ListAdapter.ListHolder>() {
    val myMarkers:MyMarkers = KoinJavaComponent.getKoin().get()

    class ListHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
        private val tLatitude = itemView.findViewById<TextView>(R.id.tvLat)
        private val tLongitude= itemView.findViewById<TextView>(R.id.tvLong)
        private val tName = itemView.findViewById<TextView>(R.id.tvName)

        fun bind(mapMarker: MapMarker) {
            tLatitude.text = mapMarker.point.latitude.toString()
            tLongitude.text = mapMarker.point.longitude.toString()
            tName.text = mapMarker.name
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.point_holder, parent, false)
        return ListHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListHolder, position: Int) {
        holder.bind(myMarkers.getById(position))
    }

    override fun getItemCount(): Int {
        return myMarkers.getSize()
    }

}