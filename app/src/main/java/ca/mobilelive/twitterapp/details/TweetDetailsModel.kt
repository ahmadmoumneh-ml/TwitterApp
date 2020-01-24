package ca.mobilelive.twitterapp.details

import ca.mobilelive.twitterapp.common.TweetStatusChangeListener
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.Tweet

/**
 * Represents a tweet details model component
 */
class TweetDetailsModel : Callback<Tweet>() {

    private var callback: TweetStatusChangeListener? = null

    /**
     * Set tweet status listener
     * @param callback Tweet status change listener
     */
    fun setTweetStatusListener(callback: TweetStatusChangeListener) {
        this.callback = callback
    }

    /**
     * Show tweet
     * @param id Tweet ID
     */
    fun showTweet(id: Long) {
        TwitterCore.getInstance().apiClient.statusesService.show(id, null, null, null).enqueue(this)
    }

    /**
     * Mark tweet favorite
     * @param id Tweet ID
     */
    fun markTweetFavorite(id: Long) {
        TwitterCore.getInstance().apiClient.favoriteService.create(id, null).enqueue(this)
    }

    /**
     * Mark tweet unfavorite
     * @param id Tweet ID
     */
    fun markTweetUnFavorite(id: Long) {
        TwitterCore.getInstance().apiClient.favoriteService.destroy(id, null).enqueue(this)
    }

    /**
     * Mark retweet favorite
     * @param id Tweet ID
     */
    fun markReTweet(id: Long) {
        TwitterCore.getInstance().apiClient.statusesService.retweet(id, null).enqueue(this)
    }

    /**
     * Mark retweet Undo
     * @param id Tweet ID
     */
    fun markReTweetUndo(id: Long) {
        TwitterCore.getInstance().apiClient.statusesService.unretweet(id, null).enqueue(this)
    }

    /**
     * Update tweet details when tweet status changes
     * @param result Tweet
     */
    override fun success(result: Result<Tweet>?) {
        val tweet = result?.response?.body() as Tweet
        callback?.onTweetStatusChange(tweet)
    }

    /**
     * Print error when tweet status change has failed
     * @param exception Twitter exception
     */
    override fun failure(exception: TwitterException?) {
        exception?.printStackTrace()
    }
}