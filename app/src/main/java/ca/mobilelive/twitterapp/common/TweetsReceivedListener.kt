package ca.mobilelive.twitterapp.common

import com.twitter.sdk.android.core.models.Tweet

/**
 * Represents a listener for received tweets
 */
interface TweetsReceivedListener {
    /**
     * Show tweets on map when tweets are successfully received
     * @param tweetsList List of tweets
     */
    fun onTweetReceivedSuccess(tweetsList: MutableList<Tweet>?)

    /**
     * Print error when tweets received failed
     */
    fun onTweetReceivedFail()
}