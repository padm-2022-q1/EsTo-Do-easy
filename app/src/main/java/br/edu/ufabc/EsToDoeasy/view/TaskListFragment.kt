package br.edu.ufabc.EsToDoeasy.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import br.edu.ufabc.EsToDoeasy.R
import br.edu.ufabc.EsToDoeasy.databinding.FragmentTaskListBinding
import br.edu.ufabc.EsToDoeasy.viewmodel.MainViewModel

/**
 * Tasks list view.
 */
class TaskListFragment : Fragment() {
    private lateinit var binding: FragmentTaskListBinding
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
        binding = FragmentTaskListBinding.inflate(inflater, container, false)
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
        binding.recyclerviewTaskList.apply {
            adapter = TaskAdapter(
                viewModel.getAll(),
                viewModel
            )
        }
    }

    private fun initComponents() {
        val menu = binding.mainNavigation

        val allBadge = menu.getOrCreateBadge(R.id.menu_item_list_all)
        allBadge.isVisible = true
        allBadge.number = viewModel.getAll().size
    }

    private fun bindEvents() {
        binding.mainNavigation.setOnItemSelectedListener {
            val criteria = when (it.itemId) {
                R.id.menu_item_list_all -> FilterCriteria.ALL
                R.id.menu_item_list_favorite -> FilterCriteria.FAVORITE
                R.id.menu_item_list_archived -> FilterCriteria.ARCHIVED
                else -> FilterCriteria.ALL
            }

            updateRecyclerView(criteria)

            true
        }
    }
}
