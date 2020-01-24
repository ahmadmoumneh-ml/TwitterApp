package ca.mobilelive.twitterapp.search

import android.content.Context
import android.content.Intent
import android.widget.Toast
import ca.mobilelive.twitterapp.R
import ca.mobilelive.twitterapp.common.OnTweetListItemClickListener
import ca.mobilelive.twitterapp.common.TweetsReceivedListener
import ca.mobilelive.twitterapp.details.TweetDetailsActivity
import ca.mobilelive.twitterapp.maps.MapActivity
import ca.mobilelive.twitterapp.utils.Constants.Companion.DEFAULT_QUERY_PARAM
import ca.mobilelive.twitterapp.utils.isNetworkAvailable
import com.twitter.sdk.android.core.models.Tweet

/**
 * Represents a search presenter component
 */
class SearchPresenter

constructor(
    private var context: Context?, //context of the search presenter
    private var view: SearchView?, //serach view component
    private var model: SearchModel? //serach model component
) : TweetsReceivedListener, OnTweetListItemClickListener {

    /**
     * Initialize the search presenter component
     */
    fun onInitializeRequested() {
        fetchQueryTweetsList(DEFAULT_QUERY_PARAM)
    }

    /**
     * Perform search with a specified query
     * @param query Search query
     */
    fun performSearchWithQuery(query: String) {
        fetchQueryTweetsList(if(query.isNotEmpty()) query else DEFAULT_QUERY_PARAM)
    }

    /**
     * Set Tweets list on tweet received success
     * @param tweetsList Tweets List
     */
    override fun onTweetReceivedSuccess(tweetsList: MutableList<Tweet>?) {
        view?.setTweetsList(tweetsList)
    }

    /**
     * Print error on tweet recieved fail
     */
    override fun onTweetReceivedFail() {
        showError(context?.getString(R.string.error_message_internet))
    }

    /**
     * Start tweet details activity on tweett item click
     * @param tweetId Tweet ID
     */
    override fun onTweetItemClick(tweetId: Long) {
        openTweetDetailsPage(tweetId)
    }

    /**
     * Fetch query tweets list
     * @param query Search query
     */
    private fun fetchQueryTweetsList(query: String) {
        context?.let {
            if (it.isNetworkAvailable()) {
                model?.fetchQueryTweetsList(query, this)
            } else {
                showError(it.getString(R.string.error_message_internet))
            }
        }
    }

    /**
     * Start the maps activity
     */
    fun openMapsPage() {
        context?.startActivity(Intent(context, MapActivity::class.java))
    }

    /**
     * Print error
     * @param errorMessage Error message
     */
    private fun showError(errorMessage: String?) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }

    /**
     * Start tweet details activity
     * @param tweetId Tweet ID
     */
    private fun openTweetDetailsPage(tweetId: Long) {
        val tweetDetails = Intent(context, TweetDetailsActivity::class.java)

        tweetDetails.apply {
            putExtra(TweetDetailsActivity.TWEET_EXTRA_KEY, tweetId) //include tweet ID
        }

        context?.startActivity(tweetDetails)
    }

    /**
     * Clean up serach presenter component
     */
    fun onCleanUpRequested() {

        model?.cleanUp()
        context = null
        view = null
    }
}