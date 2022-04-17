package br.edu.ufabc.EsToDoeasy.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import br.edu.ufabc.EsToDoeasy.databinding.FragmentHomeBinding
import br.edu.ufabc.EsToDoeasy.databinding.FragmentPomodoroBinding
import br.edu.ufabc.EsToDoeasy.viewmodel.MainViewModel

/**
 * Tasks list view.
 */
class PomodoroFragment : Fragment() {
    private lateinit var binding: FragmentPomodoroBinding
    private val viewModel: MainViewModel by activityViewModels()

    /**
     * Filter criteria for tasks listing.
     */
    enum class FilterCriteria { ALL, FAVORITE, ARCHIVED }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPomodoroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
    }
}
