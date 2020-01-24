package ca.mobilelive.twitterapp.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ca.mobilelive.twitterapp.R
import ca.mobilelive.twitterapp.utils.*
import ca.mobilelive.twitterapp.utils.Constants.Companion.MEDIA_TYPE_IMAGE
import ca.mobilelive.twitterapp.utils.Constants.Companion.MEDIA_TYPE_VIDEO
import com.twitter.sdk.android.core.models.Tweet
import kotlinx.android.synthetic.main.fragment_tweet_details.*
import kotlinx.android.synthetic.main.layout_tweet_interaction.*

/**
 * Represents tweet details view component
 */
class TweetDetailsView

    constructor(private val tweetId: Long) : Fragment() { //pass tweet ID when view component is created

    private lateinit var model: TweetDetailsModel //tweet details model component
    private lateinit var presenter: TweetDetailsPresenter //tweet details presenter component

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tweet_details, container, false) //updated tweet details
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model = TweetDetailsModel() //create tweet details model component
        presenter = TweetDetailsPresenter(context, this, model) //create tweet details presenter component
        presenter.onInitializeRequested() //initialize presenter

        if (tweetId > 0) {
            presenter.showTweet(tweetId) //show tweet if tweet ID is greate than zero
        }
    }

    /**
     * Show updated Tweet
     * @param tweet Tweet
     */
    @SuppressLint("SetTextI18n")
    fun showUpdatedTweet(tweet: Tweet) {
        userAvatar.loadImageFromUrl(tweet.user.profileImageUrl) //load user profile image
        userName.text = tweet.user.name //set username
        twitterHandle.text = "@${tweet.user.screenName}" //set user screen name
        tweetText.text = tweet.text //set tweet text
        favoriteCount.text = tweet.favoriteCount.toString() //set favorite count
        retweetCount.text = tweet.retweetCount.toString() //set retweet count
        tweetTimeStamp.text = Utils.formatTime(tweet.createdAt) //set tweet time stamp
        if (tweet.favorited) {
            favoriteIcon.setImageResource(R.drawable.ic_favorite) //mark as favorite
        } else {
            favoriteIcon.setImageResource(R.drawable.ic_un_favorite) //mark as unfavorite
        }

        if (tweet.retweeted) {
            reTweetIcon.setImageResource(R.drawable.ic_retweeted) //mark as retweeted
        } else {
            reTweetIcon.setImageResource(R.drawable.ic_retweet) //mark as retweeet
        }

        favoriteIcon.setOnClickListener {
            if (tweet.favorited)
                presenter.markTweetUnFavorite(tweet.id) //mark tweet as unfavorite
            else
                presenter.markTweetFavorite(tweet.id) //mark tweet as favorite
        }

        reTweetIcon.setOnClickListener {
            if (tweet.retweeted)
                presenter.markReTweetUndo(tweet.id) //mark as retweet undo
            else
                presenter.markReTweet(tweet.id) //mark as retweet
        }

        mediaImage.apply {
            when {
                tweet.hasPhoto() -> { //show photo
                    visibility = View.VISIBLE
                    loadImageFromUrl(tweet.getImageUrl())
                    setOnClickListener {
                        presenter.openViewMediaPage(MEDIA_TYPE_IMAGE, tweet.getImageUrl(), "") //start view media activity including the photo
                    }
                }
                tweet.hasSingleVideo() -> { //show video
                    visibility = View.VISIBLE
                    loadImageFromUrl(tweet.getVideoCoverUrl())
                    setOnClickListener {
                        val pair = tweet.getVideoUrl()
                        val url = pair.first
                        val contentType = pair.second
                        presenter.openViewMediaPage(MEDIA_TYPE_VIDEO, url, contentType) //start view media activity including the video
                    }
                }
                else -> visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onCleanUpRequested() //clean up presenter
    }
}