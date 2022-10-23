package com.codingwithmitch.espressouitestexamples.ui.movie

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.codingwithmitch.espressouitestexamples.R
import com.codingwithmitch.espressouitestexamples.data.FakeMovieData.FAKE_NETWORK_DELAY
import com.codingwithmitch.espressouitestexamples.data.Movie
import com.codingwithmitch.espressouitestexamples.data.source.MoviesDataSource
import com.codingwithmitch.espressouitestexamples.databinding.FragmentMovieListBinding
import com.codingwithmitch.espressouitestexamples.ui.UICommunicationListener
import com.codingwithmitch.espressouitestingexamples.util.EspressoIdlingResource
import com.codingwithmitch.espressouitestexamples.util.TopSpacingItemDecoration
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import java.lang.ClassCastException


class MovieListFragment(val moviesDataSource: MoviesDataSource) : Fragment(),
    MoviesListAdapter.Interaction {

    private val TAG: String = "AppDebug"

    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!

    override fun onItemSelected(position: Int, item: Movie) {
        activity?.run {
            val bundle = Bundle()
            bundle.putInt("movie_id", item.id)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MovieDetailFragment::class.java, bundle)
                .addToBackStack("MovieDetailFragment")
                .commit()
        }
    }

    private lateinit var listAdapter: MoviesListAdapter
    lateinit var uiCommunicationListener: UICommunicationListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        getData()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun getData() {
        EspressoIdlingResource.increment()
        uiCommunicationListener.loading(true)
        val job = GlobalScope.launch(IO) {
            delay(FAKE_NETWORK_DELAY)
        }
        job.invokeOnCompletion {
            GlobalScope.launch(Main) {
                EspressoIdlingResource.decrement()
                uiCommunicationListener.loading(false)
                listAdapter.submitList(moviesDataSource.getMovies())
            }
        }
    }

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            removeItemDecoration(TopSpacingItemDecoration(30))
            addItemDecoration(TopSpacingItemDecoration(30))
            listAdapter = MoviesListAdapter(this@MovieListFragment)
            adapter = listAdapter
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            uiCommunicationListener = context as UICommunicationListener
        } catch (e: ClassCastException) {
            Log.e(TAG, "Must implement interface in $activity: ${e.message}")
        }
    }
}