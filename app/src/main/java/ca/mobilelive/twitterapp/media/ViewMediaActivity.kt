package ca.mobilelive.twitterapp.media

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import ca.mobilelive.twitterapp.R
import ca.mobilelive.twitterapp.utils.Constants.Companion.MEDIA_TYPE_GIF
import ca.mobilelive.twitterapp.utils.Constants.Companion.MEDIA_TYPE_IMAGE
import ca.mobilelive.twitterapp.utils.Constants.Companion.MEDIA_TYPE_VIDEO
import ca.mobilelive.twitterapp.utils.loadImageFromUrl
import kotlinx.android.synthetic.main.activity_view_media.*

/**
 * Represents a media view activity
 */
class ViewMediaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_media)

        intent.extras?.let { //get media including images and videos
            when (intent.getStringExtra(MEDIA_TYPE)) {
                MEDIA_TYPE_IMAGE -> { //get Image
                    imageView.apply {
                        visibility = View.VISIBLE
                        loadImageFromUrl(intent.getStringExtra(MEDIA_URL))
                    }
                }
                MEDIA_TYPE_VIDEO -> { //get video
                    videoView.apply {
                        visibility = View.VISIBLE
                        val mc = MediaController(this@ViewMediaActivity)
                        mc.setAnchorView(this)
                        mc.setMediaPlayer(this)

                        if (intent.getStringExtra(MEDIA_TYPE_CONTENT) == MEDIA_TYPE_GIF)
                            this.setOnCompletionListener { this.start() }

                        this.setMediaController(mc)
                        this.setVideoURI(Uri.parse(intent.getStringExtra(MEDIA_URL)))
                        this.requestFocus()
                        this.start()
                    }
                }
                else -> {
                }
            }
        }
    }

    companion object {
        const val MEDIA_TYPE: String = "media_type"
        const val MEDIA_URL: String = "url"
        const val MEDIA_TYPE_CONTENT: String = "content"
    }
}