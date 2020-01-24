package ca.mobilelive.twitterapp.login

import android.content.Context
import android.content.Intent
import ca.mobilelive.twitterapp.search.SearchActivity
import com.twitter.sdk.android.core.*


class LoginPresenter
    constructor(private val context: Context?) : Callback<TwitterSession>() {

    private lateinit var twitterSession: TwitterSession

    override fun success(result: Result<TwitterSession>) {
        twitterSession = TwitterCore.getInstance().sessionManager.activeSession
        openSearchPage()
    }

    override fun failure(exception: TwitterException) {

    }

    private fun openSearchPage() {
        context?.startActivity(Intent(context, SearchActivity::class.java))
    }
}