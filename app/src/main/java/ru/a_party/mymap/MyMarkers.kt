package ru.a_party.mymap

import com.yandex.mapkit.geometry.Point

class MyMarkers {
    private var markers:ArrayList<MapMarker> = arrayListOf(MapMarker(Point(0.00,0.00),"My pos"))

    fun add(mapMarker:MapMarker){
        markers.add(mapMarker)
    }

    fun clear(){
        markers.clear()
    }

    fun remove(index:Int){
        markers.removeAt(index)
    }

    fun set(mapMarker:MapMarker,pos:Int){
        markers[pos]=mapMarker
    }

    fun get():List<MapMarker>{
        return markers
    }

    fun getById(position: Int): MapMarker{
        return markers[position]
    }

    fun getSize():Int
    {
        return markers.size
    }
}