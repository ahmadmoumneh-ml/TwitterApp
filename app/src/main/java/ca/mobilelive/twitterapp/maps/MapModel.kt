package ca.mobilelive.twitterapp.maps

import ca.mobilelive.twitterapp.common.TweetsReceivedListener
import com.google.android.gms.maps.model.LatLng
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.Search
import com.twitter.sdk.android.core.services.params.Geocode

/**
 * Represents a map model component
 */
class MapModel : Callback<Search>() {

    private var callback: TweetsReceivedListener? = null // callback of the map model

    /**
     * Fetch nearby tweets
     * @param latLng Latitude and longitude of the tweet
     * @param radius Radius of the tweet
     * @param callback Callback for the tweet
     */
    fun fetchNearbyTweets(
        latLng: LatLng,
        radius: Float,
        callback: TweetsReceivedListener
    ) {

        this.callback = callback

        try {
            val geoCode = Geocode( //set the geo code according the latitude and longitude
                latLng.latitude,
                latLng.longitude,
                radius.toInt() / 1000,
                Geocode.Distance.KILOMETERS
            )
            TwitterCore.getInstance().apiClient.searchService.tweets( //set the tweets based on the geo code
                "#food", geoCode, null, null,
                null, 100, null, null, null, true
            ).enqueue(this)

        } catch (e: Exception) {
            e.printStackTrace()
            callback.onTweetReceivedFail()
        }

    }

    /**
     * Show tweets on the map when tweets have been successfully received
     * @param result Twitter exception
     */
    override fun success(result: Result<Search>?) {
        val tweets = (result?.response?.body() as Search).tweets //get the tweets
        callback?.onTweetReceivedSuccess(tweets.toMutableList()) //show tweets on the map
    }

    /**
     * Print error when tweets received failed
     * @param exception Twitter exception
     */
    override fun failure(exception: TwitterException?) {
        exception?.printStackTrace()
        callback?.onTweetReceivedFail()
    }
}