package ru.a_party.mymap

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yandex.mapkit.geometry.Point

class MapViewModel : ViewModel() {
    private var markerList:ArrayList<MapMarker> = ArrayList<MapMarker>()
    private var mMarkers:MutableLiveData<List<MapMarker>> = MutableLiveData()
    var markers:LiveData<List<MapMarker>> = mMarkers


    private var mMyLocation: MutableLiveData<Point> = MutableLiveData()
    var  myLocation: LiveData<Point> = mMyLocation

    fun setLocation(location: Location) {
        val myLocation = Point(location.latitude, location.longitude)
        mMyLocation.postValue(myLocation)
        markerList[0].point = myLocation
        mMarkers.postValue(markerList)
    }

    fun addMarker(point: Point, name:String){
        markerList.add(MapMarker(point,name))
        mMarkers.postValue(markerList)
    }

    fun initMarkers(){
        markerList.clear()
        markerList.add(MapMarker(Point(0.00,0.00),"my pos"))
    }

}