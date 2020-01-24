package ca.mobilelive.twitterapp.search.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ca.mobilelive.twitterapp.R
import ca.mobilelive.twitterapp.common.OnTweetListItemClickListener
import ca.mobilelive.twitterapp.utils.*
import com.twitter.sdk.android.core.models.Tweet
import kotlinx.android.synthetic.main.item_tweet_row.view.*
import kotlinx.android.synthetic.main.layout_tweet_interaction.view.*

/**
 * Represents a search tweet list adapter for Twitter
 */
class SearchTweetListAdapter(

    private val context: Context, //context of the search tweet list adapter
    private var tweetsList: MutableList<Tweet>, //tweets list
    private val listItemCallBack: OnTweetListItemClickListener //on tweet list item click listener
) :
    RecyclerView.Adapter<SearchTweetListAdapter.TweetRowViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetRowViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_tweet_row, parent, false)
        return TweetRowViewHolder(itemView)
    }

    /**
     * Get tweets list size
     * @return Int Item count
     */
    override fun getItemCount(): Int {
        return tweetsList.size
    }

    /**
     * Bind tweets list to view holder
     * @param holder View holder
     * @param position Tweet position
     */
    override fun onBindViewHolder(holder: TweetRowViewHolder, position: Int) {
        holder.bind(tweetsList[position])
    }

    /**
     * Represents a tweet row view holder
     */
    inner class TweetRowViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        /**
         * Bind tweet with item view
         * @param tweet View holder
         */
        @SuppressLint("SetTextI18n")
        fun bind(tweet: Tweet) = with(itemView) {
            userAvatar.loadImageFromUrl(tweet.user.profileImageUrl) //set user avatar image
            userName.text = tweet.user.name //set username text
            twitterHandle.text = "@${tweet.user.screenName}" //set user screen name
            tweetText.text = tweet.text //set tweet text
            tweetText.setOnClickListener {
                listItemCallBack.onTweetItemClick(tweet.id) //set on click listener
            }
            favoriteCount.text = tweet.favoriteCount.toString() //set favorite count text
            favoriteIcon.setImageResource(R.drawable.ic_un_favorite) //set favorite icon image
            retweetCount.text = tweet.retweetCount.toString() //set retweeet count text
            reTweetIcon.setImageResource(R.drawable.ic_retweet) //set retweet icon image
            tweetTimeStamp.text = Utils.formatTime(tweet.createdAt) //set tweet time stamp text
            mediaImage.apply {
                when {
                    tweet.hasPhoto() -> {
                        visibility = View.VISIBLE
                        loadImageFromUrl(tweet.getImageUrl()) //load image
                    }
                    tweet.hasSingleVideo() -> {
                        visibility = View.VISIBLE
                        loadImageFromUrl(tweet.getVideoCoverUrl()) //load video
                    }
                    else -> visibility = View.GONE
                }
            }

            setOnClickListener {
                listItemCallBack.onTweetItemClick(tweet.id) //set on tweet item click listener
            }
        }
    }
}