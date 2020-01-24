package ca.mobilelive.twitterapp.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ca.mobilelive.twitterapp.R

/**
 * Represents a search activity
 */
class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        supportFragmentManager.beginTransaction()
            .replace(R.id.search_container, SearchView()) //update search view
            .commit()
    }
}