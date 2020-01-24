package ca.mobilelive.twitterapp.details

import android.content.Context
import android.content.Intent
import ca.mobilelive.twitterapp.common.TweetStatusChangeListener
import ca.mobilelive.twitterapp.media.ViewMediaActivity
import com.twitter.sdk.android.core.models.Tweet

/**
 * Represents a tweet details presenter component
 */
class TweetDetailsPresenter

constructor(
    private var context: Context?, //context of the presenter
    private var view: TweetDetailsView?, //Tweet details view component
    private var model: TweetDetailsModel? // Tweet details model component
) : TweetStatusChangeListener {

    /**
     * Request initialization of the presenter
     */
    fun onInitializeRequested() {
        model?.setTweetStatusListener(this) //set the model's tweet status listener
    }

    /**
     * Request clean up of the presenter
     */
    fun onCleanUpRequested() {
        view = null
        model = null
    }

    /**
     * Show updated Tweet on tweet status change
     * @param tweet Tweet
     */
    override fun onTweetStatusChange(tweet: Tweet) {
        view?.showUpdatedTweet(tweet)
    }

    /**
     * Show Tweet
     * @param id Tweet ID
     */
    fun showTweet(id: Long) {
        model?.showTweet(id)
    }

    /**
     * Mark tweet favorite
     * @param id Tweet ID
     */
    fun markTweetFavorite(id: Long) {
        model?.markTweetFavorite(id)
    }

    /**
     * Mark tweet unfavorite
     * @param id Tweet ID
     */
    fun markTweetUnFavorite(id: Long) {
        model?.markTweetUnFavorite(id)
    }

    /**
     * Mark retweet
     * @param id Tweet ID
     */
    fun markReTweet(id: Long) {
        model?.markReTweet(id)
    }

    /**
     * Mark retweet undo
     * @param id Tweet ID
     */
    fun markReTweetUndo(id: Long) {
        model?.markReTweetUndo(id)
    }

    /**
     * Start view media activity
     * @param mediaType
     * @param mediaUrl
     * @param mediaContentTye
     */
    fun openViewMediaPage(mediaType: String, mediaUrl: String, mediaContentTye: String) {
        val viewMedia = Intent(context, ViewMediaActivity::class.java).apply {
            putExtra(ViewMediaActivity.MEDIA_TYPE, mediaType) //include media type
            putExtra(ViewMediaActivity.MEDIA_URL, mediaUrl) //include media URL
            putExtra(ViewMediaActivity.MEDIA_TYPE_CONTENT, mediaContentTye) //include media content type
        }

        view?.startActivity(viewMedia)
    }
}