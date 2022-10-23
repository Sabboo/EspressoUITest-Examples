package com.codingwithmitch.espressouitestexamples.ui.movie

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.request.RequestOptions
import com.codingwithmitch.espressouitestexamples.R
import com.codingwithmitch.espressouitestexamples.data.source.MoviesDataSource
import com.codingwithmitch.espressouitestexamples.data.source.MoviesRemoteDataSource
import com.codingwithmitch.espressouitestexamples.databinding.ActivityMainBinding
import com.codingwithmitch.espressouitestexamples.factory.MovieFragmentFactory
import com.codingwithmitch.espressouitestexamples.ui.UICommunicationListener

class MainActivity : AppCompatActivity(), UICommunicationListener {

    private lateinit var binding: ActivityMainBinding


    override fun loading(isLoading: Boolean) {
        if (isLoading)
            binding.progressBar.visibility = View.VISIBLE
        else
            binding.progressBar.visibility = View.INVISIBLE
    }

    // dependencies (typically would be injected with dagger)
    lateinit var requestOptions: RequestOptions
    lateinit var moviesDataSource: MoviesDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        initDependencies()
        supportFragmentManager.fragmentFactory = MovieFragmentFactory(
            requestOptions,
            moviesDataSource
        )

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        init()
    }

    private fun init() {
        if (supportFragmentManager.fragments.size == 0) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MovieListFragment::class.java, null)
                .commit()
        }
    }

    private fun initDependencies() {
        if (!::requestOptions.isInitialized) {
            // glide
            requestOptions = RequestOptions
                .placeholderOf(R.drawable.default_image)
                .error(R.drawable.default_image)
        }
        if (!::moviesDataSource.isInitialized) {
            // Data Source
            moviesDataSource = MoviesRemoteDataSource()
        }
    }


}







