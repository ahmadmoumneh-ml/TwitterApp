package ca.mobilelive.twitterapp.maps

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import ca.mobilelive.twitterapp.R
import ca.mobilelive.twitterapp.common.TweetsReceivedListener
import ca.mobilelive.twitterapp.maps.adapter.MapAdapter
import ca.mobilelive.twitterapp.utils.Constants.Companion.DEFAULT_RADIUS
import ca.mobilelive.twitterapp.utils.Constants.Companion.DEFAULT_ZOOM_SCALE
import ca.mobilelive.twitterapp.utils.getLatLng
import ca.mobilelive.twitterapp.utils.getZoomLevel
import ca.mobilelive.twitterapp.utils.isNetworkAvailable
import ca.mobilelive.twitterapp.utils.toJSONObjectForPin
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.twitter.sdk.android.core.models.Tweet

/**
 * Represents a map presenter component
 */
class MapPresenter

constructor(
    private var context: Context?, //context of the presenter component
    private var view: MapView?, //map view component
    private var model: MapModel? //map model component
) : TweetsReceivedListener {

    private lateinit var googleMap: GoogleMap //google map
    private lateinit var latLng: LatLng //latitude and longitude
    private var location: Location? = null //location on the map
    private lateinit var circle: Circle //circle on the map
    private var circleRadius = DEFAULT_RADIUS //circle radius

    /**
     * Initialize presenter component
     * @param location Current location on the map
     */
    fun onInitializeRequested() {
        location = getCurrentLocation()
    }

    /**
     * Initialize google maps
     * @param map Google map
     */
    fun setGoogleMaps(map: GoogleMap) {
        googleMap = map
        location?.let {
            latLng = LatLng(it.latitude, it.longitude)
        } ?: run {
            latLng = LatLng(-34.0, 151.0)
        }

        setMapZoom(latLng)
        setMapAdapter()
        loadNearbyTweets(latLng, circleRadius.toFloat(), this)
    }

    /**
     * Load tweets as per update radius
     * @param radius Specified radius
     */
    fun loadTweetsAsPerUpdateRadius(radius: Double) {
        circleRadius = radius
        loadNearbyTweets(latLng, circleRadius.toFloat(), this)
    }

    /**
     * Show tweets on the map when tweets are successfully received
     * @param tweetsList List of tweets
     */
    override fun onTweetReceivedSuccess(tweetsList: MutableList<Tweet>?) {
        showTweetsOnMap(tweetsList, latLng)
    }

    /**
     * Print error when tweets received failed
     */
    override fun onTweetReceivedFail() {
        context?.let {
            showError(it.getString(R.string.error_message_tweet_load))
        }
    }

    /**
     * Print error message
     * @param errorMessage Error message
     */
    private fun showError(errorMessage: String) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }

    /**
     * Set Map Zoom
     * @param latLng Latitude and longitude
     */
    private fun setMapZoom(latLng: LatLng) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap.animateCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition.fromLatLngZoom(
                    latLng,
                    DEFAULT_ZOOM_SCALE
                )
            )
        )
        googleMap.uiSettings.isMyLocationButtonEnabled = false
    }

    /**
     * Set map adapter
     */
    private fun setMapAdapter() {
        context?.let {
            val adapter = MapAdapter(it)
            googleMap.setInfoWindowAdapter(adapter)
            googleMap.setOnInfoWindowClickListener(adapter)
        }
    }

    /**
     * Load nearby tweets
     * @param latLng Latitude and longitude
     * @param radius Circle radius
     * @param callback Callback for loading nearby tweets
     */
    private fun loadNearbyTweets(latLng: LatLng, radius: Float, callback: TweetsReceivedListener) {

        context?.let {
            if (it.isNetworkAvailable()) {
                model?.fetchNearbyTweets(latLng, radius, callback)
            } else {
                showError(it.getString(R.string.error_message_internet))
            }
        }
    }

    /**
     * Show tweets on the map
     * @param tweetsList List of tweets
     * @param latLng Latitude and longitude
     */
    private fun showTweetsOnMap(tweetsList: MutableList<Tweet>?, latLng: LatLng) {
        googleMap.clear()
        addMapCircle(latLng)
        tweetsList?.let {
            it.map { tweet ->
                tweet.getLatLng()?.let { latLng ->
                    Pair(tweet, latLng)
                }
            }.forEach { pair: Pair<Tweet, LatLng>? ->
                pair?.let {
                    addMarker(pair)
                }
            }
        }
    }

    /**
     * Add map circle
     * @param latLng Latitude and longitude
     */
    private fun addMapCircle(latLong: LatLng) {
        circle = googleMap.addCircle(
            CircleOptions()
                .center(latLong)
                .radius(circleRadius)
                .strokeWidth(2f)
                .fillColor(0x220000FF)
        )
        googleMap.animateCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition.fromLatLngZoom(
                    latLong,
                    circle.getZoomLevel()
                )
            )
        )
    }

    /**
     * Add marker on the map
     * @param tweet Pair of tweet and latitude and longitude
     */
    private fun addMarker(tweet: Pair<Tweet, LatLng>) {
        val json = tweet.first.toJSONObjectForPin()
        googleMap.addMarker(
            MarkerOptions().position(tweet.second)
                .title(tweet.first.user.name)
                .snippet(json.toString())
        )
    }

    /**
     * get current location on the map
     * @return Location Location on the map
     */
    private fun getCurrentLocation(): Location? {
        context?.let {
            val locationManager = it.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return if (ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            } else {
                return null
            }
        }
        return null
    }

    /**
     * Clean up map presenter component
     */
    fun onCleanUpRequested() {
        context = null
        view = null
        model = null
    }
}