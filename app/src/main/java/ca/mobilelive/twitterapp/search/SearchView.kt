package ca.mobilelive.twitterapp.search

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ca.mobilelive.twitterapp.R
import ca.mobilelive.twitterapp.common.OnTweetListItemClickListener
import ca.mobilelive.twitterapp.search.adapter.SearchTweetListAdapter
import com.twitter.sdk.android.core.models.Tweet
import kotlinx.android.synthetic.main.fragment_search.*

class SearchView : Fragment() {

    private lateinit var model: SearchModel
    private lateinit var presenter: SearchPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewManager = LinearLayoutManager(activity)

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            recyclerView.setHasFixedSize(true)
        }

        model = SearchModel()
        presenter = SearchPresenter(context, this, model)
        presenter.onInitializeRequested()

        searchButton.setOnClickListener {
            presenter.performSearchWithQuery(searchField.text.toString())
            searchField.setText("")
        }
    }

    fun setTweetsList(tweetsList: MutableList<Tweet>?) {
        activity?.let {
            tweetsList?.let { tweetList ->
                recyclerView.adapter =
                    SearchTweetListAdapter(
                        it,
                        tweetList,
                        presenter as OnTweetListItemClickListener
                    )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onCleanUpRequested()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu_options, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_maps -> presenter.openMapsPage()
        }
        return false
    }
}