package br.edu.ufabc.EsToDoeasy.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import br.edu.ufabc.EsToDoeasy.R
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

        initComponents()
        bindEvents()

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

    // Some test Here
    @SuppressLint("SetTextI18n")
    private fun initComponents() {
        val menu = binding.mainNavigation

        binding.studyTechniquesItemTitle.text = "Task 1"
        binding.studyTechniquesItemGroup.text = "Group 1"
        binding.studyTechniquesItemTimeElapsed.text = "00:45"
        val allBadge = menu.getOrCreateBadge(R.id.menu_item_list_home)
        allBadge.isVisible = true
        allBadge.number = viewModel.getAll().size
    }

    private fun bindEvents() {
        binding.mainNavigation.setOnItemSelectedListener {
            val criteria = when (it.itemId) {
                R.id.menu_item_list_home -> FilterCriteria.ALL
                R.id.menu_item_list_schedule -> FilterCriteria.FAVORITE
                R.id.menu_item_list_dash -> FilterCriteria.ARCHIVED
                else -> FilterCriteria.ALL
            }

            updateRecyclerView(criteria)

            true
        }
    }
}
