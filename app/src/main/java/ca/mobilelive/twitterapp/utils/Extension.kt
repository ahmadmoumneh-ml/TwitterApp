package ca.mobilelive.twitterapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.ImageView
import ca.mobilelive.twitterapp.maps.adapter.MapAdapter.Companion.AVATAR_URL
import ca.mobilelive.twitterapp.maps.adapter.MapAdapter.Companion.PLACE_NAME
import ca.mobilelive.twitterapp.maps.adapter.MapAdapter.Companion.TWEET_ID
import ca.mobilelive.twitterapp.maps.adapter.MapAdapter.Companion.TWEET_TEXT
import ca.mobilelive.twitterapp.maps.adapter.MapAdapter.Companion.TWEET_TIME
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.LatLng
import com.squareup.picasso.Picasso
import com.twitter.sdk.android.core.models.Coordinates
import com.twitter.sdk.android.core.models.Tweet
import org.json.JSONObject

fun Context.isNetworkAvailable(): Boolean {

    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
    return if (connectivityManager is ConnectivityManager) {
        val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        networkInfo?.isConnected ?: false
    } else false
}

fun Circle.getZoomLevel(): Float {
    val radiusCircle = radius
    val radius = radiusCircle + radiusCircle / 2
    val scale = radius / 500
    val zoomLevel = ((16 - Math.log(scale) / Math.log(2.0)).toFloat())
    return zoomLevel + 0.4F
}

/**
 * Check if tweets has latitude and longitude
 */
fun Tweet.getLatLng(): LatLng? {
    return (this.coordinates?.let { tweet ->
        LatLng(tweet.latitude, tweet.longitude)
    } ?: run {
        this.place?.boundingBox?.coordinates?.let { it ->
            if (it.isNotEmpty() && it[0].isNotEmpty()) LatLng(
                it[0][0][Coordinates.INDEX_LATITUDE],
                it[0][0][Coordinates.INDEX_LONGITUDE]
            )
            else null
        } ?: run { null }
    })
}

/**
 * Set Data for Pin to display on MAP
 */

fun Tweet.toJSONObjectForPin(): JSONObject {
    val obj = JSONObject()
    obj.put(AVATAR_URL, this.user.profileImageUrlHttps)
    obj.put(TWEET_ID, this.id)
    obj.put(TWEET_TEXT, this.text)
    obj.put(TWEET_TIME, this.createdAt)
    obj.put(PLACE_NAME, this.place.fullName)
    return obj
}

fun ImageView.loadImageFromUrl(url: String?) {
    Picasso.get().load(url).into(this)
}

fun Tweet.hasPhoto(): Boolean {
    extendedEntities?.media?.size?.let { return it == 1 && extendedEntities.media[0].type == "photo" }
    return false
}

fun Tweet.hasSinglePhoto(): Boolean {
    extendedEntities?.media?.size?.let { return it == 1 && extendedEntities.media[0].type == "photo" }
    return false
}

fun Tweet.hasSingleVideo(): Boolean {
    extendedEntities?.media?.size?.let { return it == 1 && extendedEntities.media[0].type != "photo" }
    return false
}

fun Tweet.hasMultipleMedia(): Boolean {
    extendedEntities?.media?.size?.let { return it > 1 }.run { return false }
}

fun Tweet.getImageUrl(): String {
    if (hasSinglePhoto() || hasMultipleMedia()) {
        return entities.media[0]?.mediaUrl ?: ""
    }
    throw RuntimeException("Photo not Available")
}

fun Tweet.getVideoCoverUrl(): String {
    if (hasSingleVideo() || hasMultipleMedia()) {
        return entities.media[0]?.mediaUrlHttps ?: (entities.media[0]?.mediaUrl ?: "")
    }
    throw RuntimeException("Video not Available")
}

fun Tweet.getVideoUrl(): Pair<String, String> {
    if (hasSingleVideo() || hasMultipleMedia()) {
        return Pair(
            extendedEntities.media[0].videoInfo.variants[0].url,
            extendedEntities.media[0].videoInfo.variants[0].contentType
        )
    }
    throw RuntimeException("Video not Available")
}





