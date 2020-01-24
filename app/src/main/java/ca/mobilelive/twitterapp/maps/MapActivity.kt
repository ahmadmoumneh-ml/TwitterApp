package ca.mobilelive.twitterapp.maps

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ca.mobilelive.twitterapp.R
import ca.mobilelive.twitterapp.utils.PermissionValidator
import ca.mobilelive.twitterapp.utils.PermissionValidator.REQUEST_PERMISSION_CODE

/**
 * Represents a map activity
 */
class MapActivity : AppCompatActivity() {

    private val mapView : MapView = MapView() // View component for map activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        if (PermissionValidator.isLocationPermissionAllowed(this)) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.map_container, mapView) //update map view
                .commit()
        } else {
            PermissionValidator.requestPermission(this)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.map_container, mapView) //update map view
                    .commit()
            } else {
                Toast.makeText( //print error when permission not allowed
                    this,
                    getString(R.string.message_allow_permission),
                    Toast.LENGTH_SHORT
                ).show()
            }
            finish()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}