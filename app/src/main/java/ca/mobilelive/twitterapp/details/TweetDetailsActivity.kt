package ca.mobilelive.twitterapp.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ca.mobilelive.twitterapp.R

/**
 * Represents tweet details activity
 */
class TweetDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweet_details)

        val tweetId: Long = intent?.extras?.getLong(TWEET_EXTRA_KEY) ?: 0L //get tweet ID

        supportFragmentManager.beginTransaction()
            .replace(R.id.details_container, TweetDetailsView(tweetId)) //update tweet details
            .commit()
    }

    companion object {
        const val TWEET_EXTRA_KEY: String = "tweet_key"
    }
}