package ca.mobilelive.twitterapp.utils

import android.text.format.DateUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    private const val INVALID_DATE: Long = -1
    private val dateFormat: SimpleDateFormat =
        SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH)

    private fun tweetCreateTimeToLong(apiTime: String?): Long {
        if (apiTime == null) return INVALID_DATE
        try {
            return dateFormat.parse(apiTime)!!.time
        } catch (e: ParseException) {
            return INVALID_DATE
        }
    }

    fun formatTime(time: String): CharSequence? {
        val createdAt = tweetCreateTimeToLong(time)
        if (createdAt != INVALID_DATE) {
            return DateUtils.getRelativeTimeSpanString(createdAt)
        }

        return null
    }
}