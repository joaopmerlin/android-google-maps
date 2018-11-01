package br.edu.utfpr.mapa

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import br.edu.utfpr.mapa.entity.Area
import br.edu.utfpr.mapa.entity.Ponto
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.SphericalUtil
import java.util.stream.Collectors

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var pontos: ArrayList<Ponto> = arrayListOf()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val prefs = PreferenceManager.getDefaultSharedPreferences(this@MapsActivity)
        val tipoMapa = prefs.getString("pref_tipo_mapa", "0")
        val zoom = prefs.getString("pref_zoom", "15")

        if (tipoMapa.equals("1")) {
            mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
        } else if (tipoMapa.equals("2")) {
            mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    mMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(location.latitude, location.longitude),
                            zoom.toFloat()
                        )
                    )
                } else {
                    mMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(-14.235, -51.9253),
                            4f
                        )
                    )
                }
            }

        mMap.setOnMapClickListener { onMapClick(it) }
    }

    fun onMapClick(position: LatLng?) {
        if (position != null) {
            mMap.addMarker(MarkerOptions().position(position))
            pontos.add(Ponto(null, null, position.latitude, position.longitude))
        }
    }

    fun finalizar(view: View) {
        if (pontos.isEmpty()) {
            Snackbar.make(view, "Clique no mapa para marcar os pontos", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            return
        }

        if (pontos.size <= 2) {
            Snackbar.make(view, "É necessário marcar pelo menos 3 pontos", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            return
        }

        val area = Area()
        area.perimetro = SphericalUtil.computeLength(getLatLng())
        area.area = SphericalUtil.computeArea(getLatLng())

        val intent = Intent(this@MapsActivity, MapsViewActivity::class.java)
        intent.putExtra("area", area)
        intent.putExtra("pontos", pontos)

        startActivity(intent)
        finish()
    }

    fun getLatLng(): List<LatLng> {
        return pontos.stream().map { e -> LatLng(e.latitude!!, e.longitude!!) }.collect(Collectors.toList())
    }
}
