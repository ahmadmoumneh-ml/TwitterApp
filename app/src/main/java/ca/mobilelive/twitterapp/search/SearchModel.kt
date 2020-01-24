package ca.mobilelive.twitterapp.search

import ca.mobilelive.twitterapp.common.TweetsReceivedListener
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.Search

/**
 * Represents a search model component
 */
class SearchModel : Callback<Search>() {

    private var callback: TweetsReceivedListener? = null //set empty tweets received listener

    /**
     * Clean up the search model component
     */
    fun cleanUp() {
        callback = null
    }

    /**
     * Fetch query tweets list
     * @param query Search query
     * @param callback Tweets received listener
     */
    fun fetchQueryTweetsList(query: String, callback: TweetsReceivedListener) {
        this.callback = callback
        try {
            TwitterCore.getInstance().apiClient.searchService.tweets(
                query, null, null, null,
                null, 100, null, null, null, true
            ).enqueue(this)
        } catch (exp: Exception) {
            exp.printStackTrace()
        }
    }

    /**
     * Set tweets list
     * @param result Search result
     */
    override fun success(result: Result<Search>?) {
        val tweetsList = (result?.response?.body() as Search).tweets ?: emptyList()
        callback?.onTweetReceivedSuccess(tweetsList.toMutableList())
    }

    /**
     * Print error if tweets list received failed
     * @param exception Twitter exception
     */
    override fun failure(exception: TwitterException?) {
        exception?.printStackTrace()
        callback?.onTweetReceivedFail()
    }
}