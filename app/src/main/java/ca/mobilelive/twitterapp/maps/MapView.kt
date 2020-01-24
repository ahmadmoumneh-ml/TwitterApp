package ca.mobilelive.twitterapp.maps

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import ca.mobilelive.twitterapp.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.fragment_maps.*

/**
 * Represents map view component
 */
class MapView : Fragment(), OnMapReadyCallback,
    SeekBar.OnSeekBarChangeListener {

    private lateinit var model: MapModel //map model component
    private lateinit var presenter: MapPresenter //map presenter component

    private lateinit var mapFragment: SupportMapFragment //map fragment
    private var updatedRadius: Double = 0.0 //updated radius on the map

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false) //update maps fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        seekBar.setOnSeekBarChangeListener(this)

        model = MapModel() //set the map model component
        presenter = MapPresenter(context, this, model) //set the map presenter component
        presenter.onInitializeRequested() //initialized the map presenter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mapFragment = childFragmentManager.findFragmentById(R.id.google_maps) as SupportMapFragment //set map fragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Set goolge maps
     * @param googleMap Google map
     */
    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap?.let {
            presenter.setGoogleMaps(it)
        }
    }

    /**
     * Clean up map view component
     */
    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onCleanUpRequested()
    }

    /**
     * Update radius on progress change
     * @param seekBar Seek bar
     * @param seekProgress See progress
     * @param isUser condition for seek bar
     */
    override fun onProgressChanged(seekBar: SeekBar?, seekProgress: Int, isUser: Boolean) {
        updatedRadius = (seekProgress * 1000.0)
    }

    /**
     * Update seek bar on start tracking touch
     * @param p0 Seek bar
     */
    override fun onStartTrackingTouch(p0: SeekBar?) {
        //nothing to implement
    }

    /**
     * Update seek bar on stop tracking touch
     * @param p0 Seek bar
     */
    @SuppressLint("SetTextI18n")
    override fun onStopTrackingTouch(p0: SeekBar?) {
        radiusText.text = "${updatedRadius / 1000} KM" //set radius text
        presenter.loadTweetsAsPerUpdateRadius(updatedRadius) //load tweets as per updated radius
    }
}