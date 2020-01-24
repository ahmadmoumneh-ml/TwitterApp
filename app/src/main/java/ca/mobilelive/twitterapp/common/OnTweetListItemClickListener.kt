package ca.mobilelive.twitterapp.common

/**
 * Represents a tweet list item click listener
 */
interface OnTweetListItemClickListener {
     /**
     * Show tweet when tweet item is clicked
     * @param tweetId Tweet identification number
     */
     fun onTweetItemClick(tweetId: Long)
}