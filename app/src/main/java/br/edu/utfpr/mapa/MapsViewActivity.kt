package br.edu.utfpr.mapa

import android.annotation.SuppressLint
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import br.edu.utfpr.mapa.entity.Area
import br.edu.utfpr.mapa.entity.Ponto
import br.edu.utfpr.mapa.service.AreaService
import br.edu.utfpr.mapa.service.impl.AreaServiceImpl
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolygonOptions
import kotlinx.android.synthetic.main.activity_maps_view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.text.DecimalFormat

class MapsViewActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var areaService: AreaService
    private var area: Area = Area()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps_view)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        areaService = AreaServiceImpl(this@MapsViewActivity)

        val area = intent.getSerializableExtra("area")
        val pontos = intent.getSerializableExtra("pontos")
        val visualiza = intent.getBooleanExtra("visualiza", false)

        if (area != null) {
            this.area = area as Area
            edtPerimetro.setText(DecimalFormat.getNumberInstance().format(area.perimetro) + "m")
            edtArea.setText(DecimalFormat.getNumberInstance().format(area.area) + "m2")
        }

        if (pontos != null) {
            this.area.pontos = pontos as ArrayList<Ponto>
        }

        if (visualiza) {
            edtNome.setText(this.area.nome)
            edtNome.isEnabled = false
            btnSalvar.visibility = View.INVISIBLE
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val prefs = PreferenceManager.getDefaultSharedPreferences(this@MapsViewActivity)
        val tipoMapa = prefs.getString("pref_tipo_mapa", "0")
        val zoom = prefs.getString("pref_zoom", "15")

        if (tipoMapa.equals("1")) {
            mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
        } else if (tipoMapa.equals("2")) {
            mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
        }

        val options = PolygonOptions()
        area.pontos.forEach { e -> options.add(LatLng(e.latitude!!, e.longitude!!)) }
        mMap.addPolygon(options)

        mMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(area.pontos[0].latitude!!, area.pontos[0].longitude!!),
                zoom.toFloat()
            )
        )
    }

    fun salvar(view: View) {
        if (edtNome.text.toString().isEmpty()) {
            edtNome.error = "Obrigatório"
            return
        }
        doAsync {
            area.nome = edtNome.text.toString()
            areaService.save(area)

            uiThread {
                Snackbar.make(view, "Salvo com sucesso", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }

            Thread.sleep(1000)

            uiThread { finish() }
        }
    }
}
