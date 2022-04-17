package br.edu.ufabc.EsToDoeasy.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import br.edu.ufabc.EsToDoeasy.databinding.FragmentHomeBinding
import br.edu.ufabc.EsToDoeasy.viewmodel.MainViewModel

/**
 * Tasks list view.
 */
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
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
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        bindEvents()
        initComponents()

        activity?.let {
            updateRecyclerView(FilterCriteria.ALL)
        }
    }

    private fun updateRecyclerView(criteria: FilterCriteria) {

        binding.recyclerviewNextTasksList.apply {
            adapter = TaskAdapter(
                viewModel.getAll(),
                viewModel
            )
        }
    }

    private fun bindEvents(){
        binding.cardviewSuggestedTaskItem.setOnClickListener{
            viewModel.clickedItemId.value = viewModel.getSuggestTask().id
        }

        binding.cardviewStudyTechniquesItemSelector.setOnClickListener{
            viewModel.clickedSelection.value = true
        }

        binding.suggestedTaskItemPlay.setOnClickListener{
            viewModel.clickedTaskToPlay.value = true
        }

    }

    // Some test Here
    @SuppressLint("SetTextI18n")
    private fun initComponents() {
        binding.suggestedTaskItemTitle.text = "Task 1"
        binding.suggestedTaskItemGroup.text = "Group 1"
        binding.suggestedTaskItemTimeElapsed.text = "00:45"
    }
}
