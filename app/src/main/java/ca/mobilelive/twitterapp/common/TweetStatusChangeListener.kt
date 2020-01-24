package ca.mobilelive.twitterapp.common

import com.twitter.sdk.android.core.models.Tweet

/**
 * Represents a tweet status change listener
 */
interface TweetStatusChangeListener {
    /**
     * Show updated tweet when its status has changed
     * @param tweet Tweet
     */
    fun onTweetStatusChange(tweet: Tweet)
}