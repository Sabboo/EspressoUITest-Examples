package com.codingwithmitch.espressouitestexamples.ui.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.codingwithmitch.espressouitestexamples.databinding.FragmentStarActorsBinding
import java.lang.StringBuilder

class StarActorsFragment : Fragment() {

    private var _binding: FragmentStarActorsBinding? = null
    private val binding get() = _binding!!

    private val starActors: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            starActors.addAll(args.getStringArrayList("args_actors") as List<String>)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStarActorsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setActors()
    }

    private fun setActors() {
        binding.starActorsText.text = stringBuilderForStarActors(starActors)
    }

    companion object {
        fun stringBuilderForStarActors(actors: ArrayList<String>): String {
            val sb = StringBuilder()
            for (actor in actors) {
                sb.append(actor + "\n")
            }
            return sb.toString()
        }
    }

}














