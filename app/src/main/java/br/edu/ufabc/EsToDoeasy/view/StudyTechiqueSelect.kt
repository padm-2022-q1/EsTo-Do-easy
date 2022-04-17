package br.edu.ufabc.EsToDoeasy.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import br.edu.ufabc.EsToDoeasy.databinding.FragmentStudyTechniquesSelectBinding
import br.edu.ufabc.EsToDoeasy.viewmodel.MainViewModel

/**
 * Tasks list view.
 */
class StudyTechiqueSelect : Fragment() {
    private lateinit var binding: FragmentStudyTechniquesSelectBinding
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
        binding = FragmentStudyTechniquesSelectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        activity?.let {
            updateRecyclerView(FilterCriteria.ALL)
        }
    }

    private fun updateRecyclerView(criteria: FilterCriteria) {

        binding.recyclerviewStudyTechniquesList.apply {
            adapter = TechiniquesAdapter(
                listOf("Pomodoro","Getting Things Done (GTD)","Eat that Frog","Free"),
                viewModel
            )
        }
    }
}
