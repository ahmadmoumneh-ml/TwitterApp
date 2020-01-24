package ca.mobilelive.twitterapp.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ca.mobilelive.twitterapp.R
import ca.mobilelive.twitterapp.utils.PermissionValidator
import com.twitter.sdk.android.core.DefaultLogger
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.TwitterConfig
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Represents a login activity
 */
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val config = TwitterConfig.Builder(this)
            .logger(DefaultLogger(Log.DEBUG))
            .twitterAuthConfig(
                TwitterAuthConfig(
                    resources.getString(R.string.TWITTER_CONSUMER_KEY),
                    resources.getString(R.string.TWITTER_CONSUMER_SECRET_KEY)
                )
            )
            .debug(true)
            .build()

        Twitter.initialize(config) //authenticates username and password

        if(!PermissionValidator.isAllPermissionsAllowed(this)) {
            PermissionValidator.requestPermission(this) //request permissions
        }

        twitterLoginButton.callback =  LoginPresenter(this) //set twitter login button callback
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        twitterLoginButton.onActivityResult(requestCode, resultCode, data)
    }
}
