package ru.a_party.mymap

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import ru.a_party.mymap.databinding.FragmentMapBinding

const val LOCATION_PERMISSION_REQUEST_CODE = 159753

class MapFragment : Fragment(), LocationListener {

    private var _binding: FragmentMapBinding?=null
    private val binding: FragmentMapBinding get()=_binding!!

    private lateinit var locationManager: LocationManager

    companion object {
        fun newInstance() = MapFragment()
    }

    private lateinit var viewModel: MapViewModel

    fun requestLocationPermissions(){
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val permissions  = arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
            ActivityCompat.requestPermissions(requireActivity(),permissions,LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        locationManager = getActivity()?.getSystemService(LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requestLocationPermissions()
        MapKitFactory.setApiKey(BuildConfig.API_KEY)
        MapKitFactory.initialize(context)
        _binding = FragmentMapBinding.inflate(inflater, container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MapViewModel::class.java)

        viewModel.initMarkers()
        getLocation()

        viewModel.myLocation.observe(viewLifecycleOwner){point->
            val camPos = CameraPosition(point,14f,0f,0f)
            binding.mapview.getMap().move(camPos,Animation(Animation.Type.SMOOTH, 5f),null)
        }

        viewModel.markers.observe(viewLifecycleOwner){markers->
            val mapObjects =binding.mapview.map.mapObjects
            mapObjects.clear()
            for (marker in markers){
                mapObjects.addPlacemark(marker.point)
            }
        }
    }

    override fun onLocationChanged(location: Location) {
        viewModel.setLocation(location)
    }

    override fun onStop() {
        binding.mapview.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart();
        binding.mapview.onStart();
    }
}