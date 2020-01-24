package ca.mobilelive.twitterapp.maps.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import ca.mobilelive.twitterapp.R
import ca.mobilelive.twitterapp.details.TweetDetailsActivity
import ca.mobilelive.twitterapp.utils.Utils
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.layout_tweet_marker.view.*
import org.json.JSONObject

/**
 * Represents a map adapter for Google Maps
 */
class MapAdapter(private val context: Context) :
    GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener {

    companion object {
        const val AVATAR_URL = "profileImageUrl"
        const val TWEET_ID = "id"
        const val TWEET_TEXT = "text"
        const val PLACE_NAME = "placeName"
        const val TWEET_TIME = "time"
    }

    /**
     * Get information contents
     * @param marker
     * @return View Marker view
     */
    @SuppressLint("InflateParams")
    override fun getInfoContents(marker: Marker?): View {
        val markerView = LayoutInflater.from(context).inflate(R.layout.layout_tweet_marker, null) //update tweet marker layout
        val markerData = getMarkerData(marker) //get the marker
        markerView.userName.text = marker?.title //set username
        markerView.placeName.text = markerData.getString(PLACE_NAME) //set place name
        markerView.tweetTimeStamp.text =
            Utils.formatTime(markerData.getString(TWEET_TIME)) //set tweet time
        return markerView
    }

    /**
     * Start tweet details activity when a pin is clicked
     * @param marker Marker on the map
     */
    override fun onInfoWindowClick(marker: Marker?) {
        val tweetId = getMarkerData(marker).getLong(TWEET_ID) //get tweet ID
        onPinClick(tweetId) //start tweet details activity
    }

    /**
     * Get info window
     * @param marker Marker on the map
     */
    override fun getInfoWindow(marker: Marker?) = null

    /**
     * Get marker data
     * @param marker Marker on the map
     */
    private fun getMarkerData(marker: Marker?): JSONObject {
        marker?.snippet?.let {
            return JSONObject(it)
        }
        return JSONObject()
    }

    /**
     * Start tweet details activity when a pin is clicked
     * @param tweetId Tweet ID
     */
    private fun onPinClick(tweetId: Long) {
        val tweetDetails = Intent(context, TweetDetailsActivity::class.java).apply {
            putExtra(TweetDetailsActivity.TWEET_EXTRA_KEY, tweetId)
        }

        context.startActivity(tweetDetails)
    }
}