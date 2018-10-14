package br.edu.utfpr.mapa

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import br.edu.utfpr.mapa.adapter.AreaAdapter
import br.edu.utfpr.mapa.entity.Area
import br.edu.utfpr.mapa.service.AreaService
import br.edu.utfpr.mapa.service.impl.AreaServiceImpl
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainActivity : AppCompatActivity() {

    private lateinit var areaService: AreaService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        areaService = AreaServiceImpl(this@MainActivity)

        checkPermissions()

        fab.setOnClickListener {
            startActivity(Intent(this@MainActivity, MapsActivity::class.java))
        }

        listView.setOnItemClickListener { parent, view, position, id ->
            onListClick(id)
        }

        findAll()
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0
            )
        }
    }

    private fun onListClick(id: Long) {
        doAsync {
            val area = areaService.findById(id.toInt())
            val intent = Intent(this@MainActivity, MapsViewActivity::class.java)
            intent.putExtra("area", area)
            intent.putExtra("pontos", area.pontos)
            intent.putExtra("visualiza", true)

            uiThread { startActivity(intent) }
        }
    }

    fun findAll() {
        doAsync {
            val list = areaService.findAll()
            val adapter = AreaAdapter(list, this@MainActivity)

            uiThread {
                listView.adapter = adapter
            }
        }
    }

    override fun onResume() {
        super.onResume()
        findAll()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
