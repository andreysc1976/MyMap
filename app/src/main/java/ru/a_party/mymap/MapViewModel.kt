package ru.a_party.mymap

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yandex.mapkit.geometry.Point
import org.koin.java.KoinJavaComponent
import org.koin.java.KoinJavaComponent.inject

class MapViewModel() : ViewModel() {

    val myMarkers:MyMarkers = KoinJavaComponent.getKoin().get()

    private var mMarkers:MutableLiveData<List<MapMarker>> = MutableLiveData()
    var markers:LiveData<List<MapMarker>> = mMarkers


    private var mMyLocation: MutableLiveData<Point> = MutableLiveData()
    var  myLocation: LiveData<Point> = mMyLocation

    fun setLocation(location: Location) {
        val myLocation = Point(location.latitude, location.longitude)
        mMyLocation.postValue(myLocation)
        myMarkers.set(MapMarker(myLocation,"My pos"),0)
        mMarkers.postValue(myMarkers.get())
    }

    fun addMarker(point: Point, name:String){
        myMarkers.add(MapMarker(point,name))
        mMarkers.postValue(myMarkers.get())
    }
}