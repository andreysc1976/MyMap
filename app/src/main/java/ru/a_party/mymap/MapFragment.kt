package ru.a_party.mymap

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context.LOCATION_SERVICE
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.text.InputType
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import androidx.lifecycle.ViewModelProvider
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.GeoObjectTapEvent
import com.yandex.mapkit.layers.GeoObjectTapListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.runtime.image.ImageProvider
import ru.a_party.App
import ru.a_party.mymap.databinding.FragmentMapBinding

const val LOCATION_PERMISSION_REQUEST_CODE = 159753

class MapFragment : Fragment(), LocationListener {

    private lateinit var inputListener:InputListener

    private var _binding: FragmentMapBinding? = null
    private val binding: FragmentMapBinding get() = _binding!!

    private lateinit var locationManager: LocationManager

    companion object {
        fun newInstance() = MapFragment()
    }

    private lateinit var viewModel: MapViewModel

    fun requestLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val permissions = arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            ActivityCompat.requestPermissions(
                requireActivity(),
                permissions,
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun inputMarkerText(point:Point) {
        val builder:AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Введите название метки")
        val input = EditText(requireActivity())
        input.hint = "Название метки"
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            // Here you get get input text from the Edittext
            val mText = input.text.toString()
            viewModel.addMarker(point,mText)
        })
        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        builder.show()
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        locationManager = getActivity()?.getSystemService(LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5f, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requestLocationPermissions()

        if (App.mapInit==false)
            {
                MapKitFactory.setApiKey(BuildConfig.API_KEY)
                MapKitFactory.initialize(context)
                App.mapInit = true
            }

        _binding = FragmentMapBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MapViewModel::class.java]



        getLocation()

        viewModel.myLocation.observe(viewLifecycleOwner) { point ->
            val camPos = CameraPosition(point, 14f, 0f, 0f)
            binding.mapview.map.move(camPos)
        }


        viewModel.markers.observe(viewLifecycleOwner) { markers ->
            val mapObjects = binding.mapview.map.mapObjects
            mapObjects.clear()
            for (marker in markers) {
                mapObjects.addPlacemark(
                    marker.point,
                    ImageProvider.fromBitmap(drawSimpleBitmap("My location"))
                )
            }
        }


        inputListener = MyInputListener()

        binding.mapview.map.addInputListener(inputListener)
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

    fun drawSimpleBitmap(text: String): Bitmap {
        val vSize = 100
        val hSize = 300

        val bitmap = Bitmap.createBitmap(hSize, vSize, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.color = Color.RED
        paint.style = Paint.Style.FILL
        canvas.drawCircle(hSize/2f, vSize/2f, 20f, paint)

        return bitmap
    }

    inner class MyInputListener:InputListener{
        override fun onMapTap(p0: Map, p1: Point) {

        }

        override fun onMapLongTap(map: Map, point: Point) {
            inputMarkerText(point)
        }

    }
}

